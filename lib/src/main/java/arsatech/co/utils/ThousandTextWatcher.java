package arsatech.co.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.StringTokenizer;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class ThousandTextWatcher implements TextWatcher {

	protected EditText editText;

	public ThousandTextWatcher(EditText editText) {
		this.editText = editText;
	}

	protected static String getDecimalFormattedString(String value) {
		StringTokenizer lst = new StringTokenizer(value, ".");
		String str1 = value;
		String str2 = "";
		if (lst.countTokens() > 1) {
			str1 = lst.nextToken();
			str2 = lst.nextToken();
		}
		StringBuilder str3 = new StringBuilder();
		int i = 0;
		int j = -1 + str1.length();
		if (str1.charAt(-1 + str1.length()) == '.') {
			j--;
			str3 = new StringBuilder(".");
		}
		for (int k = j; ; k--) {
			if (k < 0) {
				if (str2.length() > 0)
					str3.append(".").append(str2);
				return str3.toString();
			}
			if (i == 3) {
				str3.insert(0, ",");
				i = 0;
			}
			str3.insert(0, str1.charAt(k));
			i++;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (editText.getText().toString().length() == 1 && editText.getText().toString().equals("0"))
			editText.setText("");
	}

	@Override
	public void afterTextChanged(Editable s) {
		editText.removeTextChangedListener(this);
		String value = editText.getText().toString();
		if (!value.equals("")) {
			String str = editText.getText().toString().replaceAll(",", "");
			editText.setText(getDecimalFormattedString(str));
			editText.setSelection(editText.getText().length());
		}
		editText.addTextChangedListener(this);
	}

}
