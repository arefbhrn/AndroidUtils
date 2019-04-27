package arsatech.co.utils.CustomViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class NonFadableScrollbarsRecyclerView extends RecyclerView {

	public NonFadableScrollbarsRecyclerView(@NonNull Context context) {
		super(context);
		init();
	}

	public NonFadableScrollbarsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public NonFadableScrollbarsRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setScrollBarFadeDuration(0);
		setScrollbarFadingEnabled(false);
	}

}
