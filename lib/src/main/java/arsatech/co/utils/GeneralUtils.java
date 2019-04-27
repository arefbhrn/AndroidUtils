package arsatech.co.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class GeneralUtils {

	public interface Callback<T> {
		void onFinish(T arg);
	}

	public static void showToastShort(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showToastLong(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * Returns timestamp from formatted strTime.
	 *
	 * @param strTime a string that must be formatted like: 2019-02-19T09-07-59Z
	 * @return parsed timestamp
	 */
	public static long getTimestamp(String strTime) {
		String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		Date date;
		try {
			date = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US).parse(strTime);
			return date.getTime() / 1000;
		} catch (ParseException ignored) {
		}
		return -1;
	}

	public static void restartApp(Class<Activity> classActivityToStart) {
		Intent mStartActivity = new Intent(LibApplication.getContext(), classActivityToStart);
		int mPendingIntentId = 123456;
		PendingIntent mPendingIntent = PendingIntent.getActivity(LibApplication.getContext(), mPendingIntentId, mStartActivity,
				PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager mgr = (AlarmManager) LibApplication.getContext().getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
		System.exit(0);
	}

	public static LinearSmoothScroller createSmoothScroll(Context context) {
		return new LinearSmoothScroller(context) {
			private final float MILLISECONDS_PER_PX = 10;// Change this value (default=25f)

			@Override
			protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
				return MILLISECONDS_PER_PX / displayMetrics.densityDpi;
			}

		};
	}

	public static String getPackageName() {
		return LibApplication.getContext().getPackageName();
	}

	public static String getAppVersion() {
		try {
			PackageInfo pInfo = LibApplication.getContext().getPackageManager()
					.getPackageInfo(LibApplication.getContext().getPackageName(), 0);
			return pInfo.versionName;
		} catch (Exception e) {
			return "UNKNOWN";
		}
	}

}
