package arsatech.co.utils.CustomViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import arsatech.co.utils.R;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public abstract class _BaseAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

	private Context context;
	private boolean animationEnabled;
	private int lastPosition = -1;

	public _BaseAdapter(Context context) {
		this.context = context;
		animationEnabled = true;
	}

	public Context getContext() {
		return context;
	}

	public void setAnimationEnabled(boolean animationEnabled) {
		this.animationEnabled = animationEnabled;
	}

	protected void startAnimation(View view, int position) {
		// If the bound view wasn't previously displayed on screen, it's animated
		if (position > lastPosition && animationEnabled) {
			Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left);
			view.startAnimation(animation);
			lastPosition = position;
		}
	}

}
