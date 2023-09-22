package com.arefdev.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.arefdev.base.BaseApp
import com.arefdev.base.enums.SDK_CODES
import com.arefdev.base.extensions.subscribeOnIOThread
import com.arefdev.base.repo.Constants
import com.arefdev.base.repo.network.ping.Ping
import io.reactivex.rxjava3.core.Observable
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

/**
 * Updated on 27/07/2023
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object NetworkUtils {

    /**
     * Checks if device is online
     *
     * @param context Context
     * @return True if is online, otherwise returns false
     */
    @JvmStatic
    fun isOnline(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (cm != null) {
                @Suppress("DEPRECATION")
                return if (isOsAtLeast(SDK_CODES.Q)) {
                    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                    if (capabilities != null) {
                        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                            true
                        else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                            true
                        else
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    } else {
                        false
                    }
                } else {
                    val info = cm.activeNetworkInfo
                    info != null && info.isConnected
                }
            }
            false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks if internet is available
     *
     * @return True if is available, otherwise returns false
     */
    val isInternetAvailable: Boolean
        get() = try {
            val ipAddr = InetAddress.getByName("https://google.com")
            ipAddr.isReachable(3000)
        } catch (e: Exception) {
            false
        }

    /**
     * Checks if device is connected using VPN
     *
     * @return True if is using VPN, otherwise returns false
     */
    val haveVpn: Boolean
        get() {
            // tap, tun, ppp, ipsec, utun1, utun0
            try {
                val list = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (networkInterface in list) {
                    if (networkInterface.name.startsWith("tun") && networkInterface.isUp) return true
                }
            } catch (ignored: Exception) {
            }
            return false
        }

    fun ping(count: Int = 8): Observable<Pair<InetAddress, MutableList<Int>>> {
        if (isOnline(BaseApp.context).not())
            return Observable.error(Throwable("Connection Error"))

        lateinit var inet: InetAddress
        val pings = mutableListOf<Int>()
        return Observable.fromCallable {
            InetAddress.getByName(Constants.BASE_URL_DOMAIN)
                .also { inet = it }
        }
            .subscribeOnIOThread()
            .flatMap {
                Ping(it).also { ping ->
                    ping.timeoutMs = 4000
                    ping.delayMs = 1000
                    ping.count = count
                }
            }
            .subscribeOnIOThread()
            .map {
                pings += it
                Pair(inet, pings)
            }
    }
}
