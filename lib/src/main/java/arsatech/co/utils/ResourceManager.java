package arsatech.co.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.EditText;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class ResourceManager {

	private Typeface font, fontBold;

	private void loadFonts() {
		Context context = LibApplication.getContext();
		font = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font_main));
		fontBold = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.font_main_bold));
	}

	public Typeface getFont() {
		if (font == null)
			loadFonts();
		return font;
	}

	public Typeface getFontBold() {
		if (font == null)
			loadFonts();
		return fontBold;
	}

	public void createErrorSpan(EditText editText, String errorString) {
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(errorString);
		ssbuilder.setSpan(new CustomTypeFaceSpan(getFont()),
				0, ssbuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

		editText.setError(ssbuilder);
	}

	public void createErrorSpan(TextInputLayout inputText, String errorString) {
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(errorString);
		ssbuilder.setSpan(new CustomTypeFaceSpan(getFont()),
				0, ssbuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

		inputText.setError(ssbuilder);
	}

	public void createHintSpan(EditText editText, String hintString) {
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(hintString);
		ssbuilder.setSpan(new CustomTypeFaceSpan(getFont()),
				0, ssbuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

		editText.setHint(ssbuilder);
	}

	public void createHintSpan(TextInputLayout inputText, String hintString) {
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(hintString);
		ssbuilder.setSpan(new CustomTypeFaceSpan(getFont()),
				0, ssbuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

		inputText.setHint(ssbuilder);
	}

}
