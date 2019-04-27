package arsatech.co.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class CustomTypeFaceSpan extends TypefaceSpan {

	private final Typeface newType;

	CustomTypeFaceSpan(Typeface type) {
		super("");
		newType = type;
	}

	private static void applyCustomTypeFace(Paint paint, Typeface tf) {
		int oldStyle;
		Typeface old = paint.getTypeface();
		if (old == null) {
			oldStyle = 0;
		} else {
			oldStyle = old.getStyle();
		}

		int fake = oldStyle & ~tf.getStyle();
		if ((fake & Typeface.BOLD) != 0) {
			paint.setFakeBoldText(true);
		}

		if ((fake & Typeface.ITALIC) != 0) {
			paint.setTextSkewX(-0.25f);
		}

		paint.setTypeface(tf);
	}

	@Override
	public void updateDrawState(@NonNull TextPaint ds) {
		applyCustomTypeFace(ds, newType);
	}

	@Override
	public void updateMeasureState(@NonNull TextPaint paint) {
		applyCustomTypeFace(paint, newType);
	}

}
