package arsatech.co.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class PermissionUtils {

	public static boolean isPermissionGranted(Context context, String permission) {
		int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
		return permissionCheck == PackageManager.PERMISSION_GRANTED;
	}

	public static void getNetworkStatePermission(Activity activity, int requestCode) {
		if (!isPermissionGranted(activity, Manifest.permission.ACCESS_NETWORK_STATE)) {
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, requestCode);
		}
	}

	public static void getPhoneStatePermission(Activity activity, int requestCode) {
		if (!isPermissionGranted(activity, Manifest.permission.READ_PHONE_STATE)) {
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, requestCode);
		}
	}

	public static void getContactPermission(Activity activity, int requestCode) {
		if (!isPermissionGranted(activity, Manifest.permission.READ_CONTACTS)) {
			ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, requestCode);
		}
	}

}
