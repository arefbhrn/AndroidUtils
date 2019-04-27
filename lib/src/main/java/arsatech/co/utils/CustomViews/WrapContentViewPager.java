package arsatech.co.utils.CustomViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class WrapContentViewPager extends ViewPager {

	private int mCurrentPagePosition = 0;

	public WrapContentViewPager(Context context) {
		super(context);
		addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				reMeasureCurrentPage(position);
			}
		});
	}

	public WrapContentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				reMeasureCurrentPage(position);
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		try {
			View child = getChildAt(mCurrentPagePosition);
			if (child != null) {
				child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
				int h = child.getMeasuredHeight();
				heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.EXACTLY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void reMeasureCurrentPage(int position) {
		mCurrentPagePosition = position;
		requestLayout();
	}

}
