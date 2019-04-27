package arsatech.co.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;

import java.security.SecureRandom;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class StringUtils {

	private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_+/=";

	/**
	 * Generates random string of given length from Base65 alphabet (numbers, lowercase letters, uppercase letters).
	 *
	 * @param length length
	 * @return random string of given length
	 */
	public static String generateRandomString(int length) {
		final SecureRandom secureRandom = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			sb.append(ALPHABET.charAt(secureRandom.nextInt(ALPHABET.length())));
		}
		return sb.toString();
	}

	public static String getPhoneNumberWithPlus(String number, String prefixNumber) {
		if (number == null) {
			return "";
		}
		if (number.length() == 0) {
			return "";
		}

		if (number.charAt(0) == '+') {
			return number;
		}

		String retString;

		if (number.charAt(0) == '0') {
			while (number.charAt(0) == '0') {
				number = number.substring(1);
			}

			retString = number;
		} else {
			retString = number;
		}

		if (retString.startsWith(prefixNumber)) {
			retString = retString.substring(2);
		}

		retString = retString.replaceAll("-", "");
		retString = retString.replaceAll(" ", "");
		retString = retString.replaceAll("#", "");
		retString = retString.replaceAll("[+]", "");
		retString = retString.replaceAll("[*]", "");

		retString = "+98" + retString;

		return retString;
	}

	public static String getPhoneNumberWithoutPlus(String number) {
		if (number == null || number.length() == 0) {
			return "";
		}

		if (number.charAt(0) == '0') {
			while (number.charAt(0) == '0') {
				number = number.substring(1);
			}

			number = "0" + number;

			return number;
		}

		String retString = "";

		if (number.charAt(0) == '+') {
			retString = number.substring(3);
		}

		retString = "0" + retString;

		retString = retString.replaceAll("-", "");
		retString = retString.replaceAll(" ", "");
		retString = retString.replaceAll("#", "");
		retString = retString.replaceAll("[+]", "");
		retString = retString.replaceAll("[*]", "");

		return retString;
	}

	public static int getNumberWithoutSeparator(String input) {
		if (input.length() == 0)
			return 0;

		if (input.replaceAll(",", "").length() > 8) {
			return 100 * 1000 * 1000;
		}
		return Integer.valueOf(input.replaceAll(",", ""));
	}

	public static SpannableString boldString(SpannableString str) {
		StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
		SpannableString txtSpannable = new SpannableString(str);
		txtSpannable.setSpan(boldSpan, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return txtSpannable;
	}

	public static SpannableString boldString(String str) {
		return boldString(new SpannableString(str));
	}

	public static SpannableString strikethroughString(SpannableString str) {
		StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
		SpannableString txtSpannable = new SpannableString(str);
		txtSpannable.setSpan(strikethroughSpan, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return txtSpannable;
	}

	public static SpannableString strikethroughString(String str) {
		return strikethroughString(new SpannableString(str));
	}

	public static SpannableString colorizeString(SpannableString str, int color) {
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
		SpannableString txtSpannable = new SpannableString(str);
		txtSpannable.setSpan(colorSpan, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return txtSpannable;
	}

	public static SpannableString colorizeString(String str, int color) {
		return colorizeString(new SpannableString(str), color);
	}

	public static String getTimeAgo(Context context, long time) {
		final int SECOND_MILLIS = 1000;
		final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
		final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
		final int DAY_MILLIS = 24 * HOUR_MILLIS;

		if (time < 1000000000000L) {
			// if timestamp given in seconds, convert to millis
			time *= 1000;
		}

		long now = System.currentTimeMillis();
		if (time > now || time <= 0) {
			return null;
		}

		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			return context.getString(R.string.time_just_now);
		} else if (diff < 2 * MINUTE_MILLIS) {
			return String.format(context.getString(R.string.time_minutes_ago), "1");
		} else if (diff < 50 * MINUTE_MILLIS) {
			return String.format(context.getString(R.string.time_minutes_ago), String.valueOf(diff / MINUTE_MILLIS));
		} else if (diff < 90 * MINUTE_MILLIS) {
			return String.format(context.getString(R.string.time_hours_ago), "1");
		} else if (diff < 24 * HOUR_MILLIS) {
			return String.format(context.getString(R.string.time_hours_ago), String.valueOf(diff / HOUR_MILLIS));
		} else if (diff < 48 * HOUR_MILLIS) {
			return context.getString(R.string.time_yesterday);
		} else {
			return String.format(context.getString(R.string.time_days_ago), String.valueOf(diff / DAY_MILLIS));
		}
	}

	public static String charsToArabic(String str) {
		if (str == null)
			return "";
		str = str.replace("ی", "ي");
		str = str.replace("ک", "ك");
		return str;
	}

	public static String charsToPersian(String str) {
		if (str == null)
			return "";
		str = str.replace("ي", "ی");
		str = str.replace("ك", "ک");
		return str;
	}

	public static String strToArabic(String str) {
		if (str == null)
			return "";
		str = str.replace("ی", "ي");
		str = str.replace("ک", "ك");
		str = numToPersian(str);
		return str;
	}

	public static String strToPersian(String str) {
		if (str == null)
			return "";
		str = str.replace("ي", "ی");
		str = str.replace("ك", "ک");
		str = numToPersian(str);
		return str;
	}

	public static String numToPersian(String str) {
		str = str.replace("0", "۰");
		str = str.replace("1", "۱");
		str = str.replace("2", "۲");
		str = str.replace("3", "۳");
		str = str.replace("4", "۴");
		str = str.replace("5", "۵");
		str = str.replace("6", "۶");
		str = str.replace("7", "۷");
		str = str.replace("8", "۸");
		str = str.replace("9", "۹");
		return str;
	}

	public static String numToEnglish(String str) {
		str = str.replace("۰", "0");
		str = str.replace("۱", "1");
		str = str.replace("۲", "2");
		str = str.replace("۳", "3");
		str = str.replace("۴", "4");
		str = str.replace("۵", "5");
		str = str.replace("۶", "6");
		str = str.replace("۷", "7");
		str = str.replace("۸", "8");
		str = str.replace("۹", "9");
		return str;
	}

	public static String capitalizeWords(String str) {
		String[] splits = str.split(" ");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < splits.length; i++) {
			if (splits[i].length() > 1)
				splits[i] = (splits[i].charAt(0) + "").toUpperCase() + splits[i].substring(1);
			else if (splits[i].length() > 0)
				splits[i] = (splits[i].charAt(0) + "").toUpperCase();
			result.append(" ").append(splits[i]);
		}
		return result.substring(1);
	}

	public static String unescapeJavaString(String str) {
		StringBuilder sb = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch == '\\') {
				char nextChar = (i == str.length() - 1) ? '\\' : str
						.charAt(i + 1);
				// Octal escape?
				if (nextChar >= '0' && nextChar <= '7') {
					String code = "" + nextChar;
					i++;
					if ((i < str.length() - 1) && str.charAt(i + 1) >= '0'
							&& str.charAt(i + 1) <= '7') {
						code += str.charAt(i + 1);
						i++;
						if ((i < str.length() - 1) && str.charAt(i + 1) >= '0'
								&& str.charAt(i + 1) <= '7') {
							code += str.charAt(i + 1);
							i++;
						}
					}
					sb.append((char) Integer.parseInt(code, 8));
					continue;
				}
				switch (nextChar) {
					case '\\':
						ch = '\\';
						break;
					case 'b':
						ch = '\b';
						break;
					case 'f':
						ch = '\f';
						break;
					case 'n':
						ch = '\n';
						break;
					case 'r':
						ch = '\r';
						break;
					case 't':
						ch = '\t';
						break;
					case '\"':
						ch = '\"';
						break;
					case '\'':
						ch = '\'';
						break;
					// Hex Unicode: u????
					case 'u':
						if (i >= str.length() - 5) {
							ch = 'u';
							break;
						}
						int code = Integer.parseInt(
								"" + str.charAt(i + 2) + str.charAt(i + 3)
										+ str.charAt(i + 4) + str.charAt(i + 5), 16);
						sb.append(Character.toChars(code));
						i += 5;
						continue;
				}
				i++;
			}
			sb.append(ch);
		}
		return sb.toString();
	}

}
