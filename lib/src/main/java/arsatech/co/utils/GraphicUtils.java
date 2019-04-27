package arsatech.co.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class GraphicUtils {

	/**
	 * This method converts dp unit to equivalent pixels, depending on device density.
	 *
	 * @param context Context to get resources and device specific display metrics
	 * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @return An integer value to represent px equivalent to dp depending on device density
	 */
	public static int convertDpToPx(Context context, float dp) {
//		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
		return (int) (dp * context.getResources().getDisplayMetrics().density);
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 *
	 * @param context Context to get resources and device specific display metrics
	 * @param px      A value in px (pixels) unit. Which we need to convert into dp
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPxToDp(Context context, float px) {
		return px / context.getResources().getDisplayMetrics().density;
	}

	/**
	 * @return {@link Point} object which:<br>
	 * -> {@code point.x} : contains width in px unit<br>
	 * -> {@code point.y} : contains height in px unit<br>
	 * Also returns {@code null} if there is problem. In such case use {@link #getViewSize(View, GeneralUtils.Callback)}
	 */
	public static Point getViewSize(View view) {
		Point size = null;
		if (ViewCompat.isLaidOut(view)) {
			size = new Point();
			size.set(view.getWidth(), view.getHeight());
		}
		return size;
	}

	/**
	 * @param callback after getting view's size, {@link GeneralUtils.Callback#onFinish(Object)} will be called. {@link Point}
	 *                 object in callback contains:<br>
	 *                 -> {@code point.x} : width in px unit<br>
	 *                 -> {@code point.y} : height in px unit
	 */
	public static void getViewSize(View view, GeneralUtils.Callback<Point> callback) {
		view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				Point size = new Point();
				size.set(view.getWidth(), view.getHeight());
				callback.onFinish(size);
			}
		});
	}

	/**
	 * @return Screen size in pixel unit in a Point object containing:<br>
	 * -> {@code point.x} : screen width<br>
	 * -> {@code point.y} : screen height
	 */
	public static Point getScreenSize(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}

	public static int getScreenWidth(Activity activity) {
		return getScreenSize(activity).x;
	}

	public static int getScreenHeight(Activity activity) {
		return getScreenSize(activity).y;
	}

	public static int getRealScreenHeight(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		return metrics.heightPixels;
	}

	public static int getNavigationBarHeight(Activity activity) {
		int realHeight = getRealScreenHeight(activity);
		int usableHeight = getScreenHeight(activity);
		if (realHeight > usableHeight)
			return realHeight - usableHeight;
		else
			return 0;
	}

	public static void hideNavigationBar(Activity activity) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			View decorView = activity.getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}

	public static void showNavigationBar(Activity activity) {
		View decorView = activity.getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.VISIBLE);
	}

	public static void makeFullscreen(Activity activity) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			View v = activity.getWindow().getDecorView();
			v.setSystemUiVisibility(View.GONE);
		} else {
			View decorView = activity.getWindow().getDecorView();
			int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
			decorView.setSystemUiVisibility(uiOptions);
		}
	}

	public static void unmakeFullscreen(Activity activity) {
		View v = activity.getWindow().getDecorView();
		v.setSystemUiVisibility(View.VISIBLE);
	}

	/**
	 * Used to get deep child offset.
	 * <p/>
	 * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
	 * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
	 *
	 * @param mainParent        Main Top parent.
	 * @param parent            Parent.
	 * @param child             Child.
	 * @param accumulatedOffset Accumulated Offset.
	 */
	public static void getDeepChildOffset(ViewGroup mainParent, ViewParent parent, View child, Point accumulatedOffset) {
		ViewGroup parentGroup = (ViewGroup) parent;
		accumulatedOffset.x += child.getLeft();
		accumulatedOffset.y += child.getTop();
		if (parentGroup.equals(mainParent)) {
			return;
		}
		getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
	}

}
