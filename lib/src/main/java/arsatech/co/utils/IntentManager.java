package arsatech.co.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class IntentManager {

	public static void restartApp(Activity activity) {
		Intent i = activity.getPackageManager().getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
		assert i != null;
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(i);
	}

	public static void shareText(Context context, String title, String body) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, title);
		i.putExtra(Intent.EXTRA_TEXT, body);
		context.startActivity(Intent.createChooser(i, context.getString(R.string.share)));
	}

	public static void openURL(Context context, String url) {
		try {
			context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		} catch (Exception ignored) {
		}
	}

	public static void openDialer(Context context, String number) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
		try {
			context.startActivity(intent);
		} catch (Exception ignored) {
		}
	}

	public static void copyStringToClipBoard(Context context, String text) {
		ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText(text, text);
		assert clipboard != null;
		clipboard.setPrimaryClip(clip);
	}

	public static void wakeScreen(Context context) {
		try {
			PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			assert powerManager != null;
			PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "Tag:");
			wakeLock.acquire(200);
			wakeLock.release();
		} catch (Exception ignored) {
		}
	}

	public static void exitApp(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
	}

}
