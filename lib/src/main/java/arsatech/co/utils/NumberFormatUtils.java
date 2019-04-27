package arsatech.co.utils;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class NumberFormatUtils {

	@SuppressLint("DefaultLocale")
	public static String digitDelimiter(long number) {
		// The comma in the format specifier does the trick
		return String.format("%,d", number);
	}

	@SuppressLint("DefaultLocale")
	public static String numberFormatter(double number, int decimalPrecision) {
		return String.format("%,." + decimalPrecision + "f", number);
	}

	@SuppressLint("DefaultLocale")
	public static String decimalFormatter(double number, int decimalPrecision) {
		if (decimalPrecision == 0)
			return String.format("%,." + decimalPrecision + "f", number);
		String precision = String.format("%0" + decimalPrecision + "d", 0);
		DecimalFormat formatter = new DecimalFormat("#,###." + precision);
		return formatter.format(number);
	}

}
