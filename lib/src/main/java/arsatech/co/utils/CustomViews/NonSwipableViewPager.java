package arsatech.co.utils.CustomViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class NonSwipableViewPager extends ViewPager {

	private boolean isSwipeEnabled = true;

	public NonSwipableViewPager(@NonNull Context context) {
		super(context);
	}

	public NonSwipableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.isSwipeEnabled && super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		return this.isSwipeEnabled && super.onInterceptTouchEvent(event);
	}

	public void setSwipeEnabled(boolean isSwipeEnabled) {
		this.isSwipeEnabled = isSwipeEnabled;
	}

	public boolean isSwipeEnabled() {
		return isSwipeEnabled;
	}

}
