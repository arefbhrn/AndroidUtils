package arsatech.co.utils;

import android.app.Activity;
import android.os.Build;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class DeviceUtils {

	/**
	 * @return OS API version(ex: 16, 28, etc.)
	 */
	public static int getOsApiVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * @return OS version name (ex: JELLY_BEAN, O, P, etc.)
	 */
	public static String getOsVersionName() {
		Field[] fields = Build.VERSION_CODES.class.getFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			int fieldValue = -1;

			try {
				fieldValue = field.getInt(new Object());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			if (fieldValue == Build.VERSION.SDK_INT) {
				return fieldName;
			}
		}
		return "UNKNOWN";
	}

	/**
	 * @return OS release version (ex: 4.1.1, 7.0.1, etc.)
	 */
	public static String getOsReleaseVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * @return the consumer friendly device full name
	 */
	public static String getDeviceFullName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model;
		}
		return manufacturer + " " + model;
	}

	/**
	 * @return the consumer friendly device manufacturer name
	 */
	public static String getDeviceManufacturerName() {
		return Build.MANUFACTURER;
	}

	/**
	 * @return the consumer friendly device name
	 */
	public static String getDeviceName() {
		return Build.MODEL;
	}

	public static boolean hasSoftNavigationBar(Activity activity) {
		boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		return !hasMenuKey && !hasBackKey;
	}

}
