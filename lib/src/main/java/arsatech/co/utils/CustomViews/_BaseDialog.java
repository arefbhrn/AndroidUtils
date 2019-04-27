package arsatech.co.utils.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import arsatech.co.utils.GraphicUtils;
import arsatech.co.utils.R;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class _BaseDialog extends Dialog {

	private boolean dim = true;
	private boolean animate = false;

	public _BaseDialog(Context context) {
		super(context);
	}

	@Override
	public void show() {
		if (animate) {
			Window window = getWindow();
			window.setBackgroundDrawableResource(android.R.color.transparent);
			WindowManager.LayoutParams wlp = window.getAttributes();
			wlp.windowAnimations = R.style.DialogAnimationFromBottom;
//			wlp.gravity = Gravity.BOTTOM;
			if (!dim)
				wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
			window.setAttributes(wlp);

			if (wlp.gravity == Gravity.BOTTOM) {
				int margin = GraphicUtils.convertDpToPx(getContext(), 30);
				window.getDecorView().setPadding(0, 0, 0, margin);
			}
		}

		super.show();
	}

	public void setDim(boolean dim) {
		this.dim = dim;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}

}