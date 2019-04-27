package arsatech.co.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class NetworkUtils {

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null;
	}

	public static boolean isOnline(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	public static boolean isInternetAvailable() {
		try {
			InetAddress ipAddr = InetAddress.getByName("http://google.com");
			return ipAddr.isReachable(5000);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean haveVpn() {
		try {
			for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if (networkInterface.getName().equals("tun0") && networkInterface.isUp())
					return true;
			}
		} catch (Exception ignored) {
		}
		return false;
	}

}
