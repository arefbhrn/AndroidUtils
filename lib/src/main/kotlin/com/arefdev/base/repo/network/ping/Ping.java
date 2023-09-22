package com.arefdev.base.repo.network.ping;

/*
 * Copyright (C) 2019 Charter Communications
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static com.arefdev.base.utils.UtilsKt.isOsAtLeast;

import android.net.Network;
import android.os.Build;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructPollfd;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import timber.log.Timber;

public class Ping extends Observable<Integer> {

    public static final int DEFAULT_COUNT = 8;
    public static final int TIMED_OUT_MS = -1;
    private static final String TAG = Ping.class.getSimpleName();

    private static final int IPTOS_LOWDELAY = 0x10;

    private static final int ECHO_PORT = 7;
    //POLLIN isn't populated correctly in test stubs
    protected static final short POLLIN = (short) (OsConstants.POLLIN == 0 ? 1 : OsConstants.POLLIN);
    private static final int MSG_DONTWAIT = 0x40;
    private final InetAddress mDest;

    private int mTimeoutMs = 4000;
    private int mDelayMs = 1000;
    private int mCount = DEFAULT_COUNT;
    private EchoPacketBuilder mEchoPacketBuilder;
    private Network mNetwork;
    private Observer<? super Integer> observer;

    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        this.observer = observer;
        run();
    }

    /**
     * @param dest Can be of type <code>Inet6Address</code> or <code>Inet4Address</code>
     */
    public Ping(final InetAddress dest) {
        mDest = dest;
        final byte type = dest instanceof Inet6Address ? EchoPacketBuilder.TYPE_ICMP_V6 : EchoPacketBuilder.TYPE_ICMP_V4;
        setEchoPacketBuilder(new EchoPacketBuilder(type, "abcdefghijklmnopqrstuvwabcdefghi".getBytes()));
    }

    public void setTimeoutMs(final int timeoutMs) {
        if (timeoutMs < 0) {
            throw new IllegalArgumentException("Timeout must not be negative: " + timeoutMs);
        }
        mTimeoutMs = timeoutMs;
    }

    public int getTimeoutMs() {
        return mTimeoutMs;
    }

    public int getDelayMs() {
        return mDelayMs;
    }

    public void setDelayMs(final int delayMs) {
        mDelayMs = delayMs;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(final int count) {
        mCount = count;
    }

    public Network getNetwork() {
        return mNetwork;
    }

    public void setNetwork(final Network network) {
        mNetwork = network;
    }

    public void setEchoPacketBuilder(final EchoPacketBuilder echoPacketBuilder) {
        mEchoPacketBuilder = echoPacketBuilder;
    }

    /**
     * Ping an IP address for N times
     */
    public void run() {
        final int inet, proto;
        if (mDest instanceof Inet6Address) {
            inet = OsConstants.AF_INET6;
            proto = OsConstants.IPPROTO_ICMPV6;
        } else {
            inet = OsConstants.AF_INET;
            proto = OsConstants.IPPROTO_ICMP;
        }
        try {
            final FileDescriptor fd = socket(inet, proto);
            if (fd.valid()) {
                try {
                    if (isOsAtLeast(Build.VERSION_CODES.M) && mNetwork != null) {
                        mNetwork.bindSocket(fd);
                    }
                    setLowDelay(fd);

                    final StructPollfd structPollfd = new StructPollfd();
                    structPollfd.fd = fd;
                    structPollfd.events = POLLIN;
                    final StructPollfd[] structPollfds = {structPollfd};
                    for (int i = 0; i < mCount; i++) {
                        final ByteBuffer byteBuffer = mEchoPacketBuilder.build();
                        final byte[] buffer = new byte[byteBuffer.limit()];

                        try {
                            // Note: it appears that the OS updates the Checksum, Identifier, and Sequence number.  The payload appears to be untouched.
                            // These changes are not reflected in the buffer, but in the returning packet.
                            final long start = System.currentTimeMillis();
                            int rc = sendto(fd, byteBuffer);
                            if (rc >= 0) {
                                rc = poll(structPollfds);
                                final int time = calcLatency(start, System.currentTimeMillis());
                                if (rc >= 0) {
                                    if (structPollfd.revents == POLLIN) {
                                        structPollfd.revents = 0;
                                        rc = recvfrom(fd, buffer);
                                        if (rc < 0) {
                                            Timber.tag(TAG).d("recvfrom() return failure: %s", rc);
                                        }
                                        observer.onNext(time);
                                    } else {
                                        observer.onNext(TIMED_OUT_MS);
                                    }
                                    if (i == mCount - 1)
                                        observer.onComplete();
                                } else {
                                    observer.onError(new IOException("poll() failed"));
                                    break;
                                }
                            } else {
                                observer.onError(new IOException("sendto() failed"));
                                break;
                            }
                        } catch (ErrnoException e) {
                            observer.onError(e);
                            break;
                        }
                        sleep();
                    }
                } finally {
                    close(fd);
                }
            } else {
                observer.onError(new IOException("Invalid FD " + fd));
            }
        } catch (ErrnoException | IOException e) {
            observer.onError(e);
        }
    }

    /*
     * Testability methods
     */

    protected int calcLatency(final long startTimestamp, final long endTimestamp) {
        return (int) (endTimestamp - startTimestamp);
    }

    protected FileDescriptor socket(final int inet, final int proto) throws ErrnoException {
        return Os.socket(inet, OsConstants.SOCK_DGRAM, proto);
    }

    protected void setLowDelay(final FileDescriptor fd) throws ErrnoException {
        if (isOsAtLeast(Build.VERSION_CODES.O)) {
            Os.setsockoptInt(fd, OsConstants.IPPROTO_IP, OsConstants.IP_TOS, IPTOS_LOWDELAY);
        } else {
            try {
                final Method method = Os.class.getMethod("setsockoptInt", FileDescriptor.class, int.class, int.class, int.class);
                method.invoke(null, fd, OsConstants.IPPROTO_IP, OsConstants.IP_TOS, IPTOS_LOWDELAY);

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                Timber.tag(TAG).e(e, "Could not setsockOptInt()");
            }
        }
    }

    protected int sendto(final FileDescriptor fd, final ByteBuffer byteBuffer) throws ErrnoException, SocketException {
        return Os.sendto(fd, byteBuffer, 0, mDest, ECHO_PORT);
    }

    protected int poll(final StructPollfd[] structPollfds) throws ErrnoException {
        return Os.poll(structPollfds, mTimeoutMs);
    }

    protected int recvfrom(final FileDescriptor fd, final byte[] buffer) throws ErrnoException, SocketException {
        return Os.recvfrom(fd, buffer, 0, buffer.length, MSG_DONTWAIT, null);
    }

    protected void close(final FileDescriptor fd) throws ErrnoException {
        Os.close(fd);
    }

    protected void sleep() {
        try {
            Thread.sleep(mDelayMs);
        } catch (InterruptedException e) {
            //Intentionally blank
        }
    }
}