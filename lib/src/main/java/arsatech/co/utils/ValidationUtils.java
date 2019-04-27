package arsatech.co.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class ValidationUtils {

	/**
	 * Password must be between 6 and 20 characters.
	 */
	public static boolean isPasswordValid(String password) {
		return password.length() >= 6;
	}

	/**
	 * Password must contains lowercase, UPPERCASE, number and special characters. It must be
	 * between 8 and 20 characters.
	 */
	public static boolean isComplexPassword(String password) {
		Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})");
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	/**
	 * Password must contains lowercase, UPPERCASE, number and special characters. It must be
	 * more than 8.
	 *
	 * @return integer between 0 to 10
	 */
	public static int getPasswordStrength(String password) {
		int iPasswordScore = 0;

		if (password.length() < 8)
			return 0;
		else if (password.length() >= 10)
			iPasswordScore += 2;
		else
			iPasswordScore += 1;

		//if it contains one digit, add 2 to total score
		if (password.matches("(?=.*[0-9]).*"))
			iPasswordScore += 2;

		//if it contains one lower case letter, add 2 to total score
		if (password.matches("(?=.*[a-z]).*"))
			iPasswordScore += 2;

		//if it contains one upper case letter, add 2 to total score
		if (password.matches("(?=.*[A-Z]).*"))
			iPasswordScore += 2;

		//if it contains one special character, add 2 to total score
		if (password.matches("(?=.*[~!@#$%^&*()_-]).*"))
			iPasswordScore += 2;

		return iPasswordScore;
	}

	public static boolean isEmailValid(String emailAddress) {
		Pattern pattern = Pattern.compile("^[a-z-0-9\\.\\_]+@([a-z-0-9]+(\\.[a-z])*)+\\.[a-z]{2,4}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(emailAddress);
		return matcher.matches();
	}

	/**
	 * Mobile number must start with "09" or "9" and followed by 9 numbers.
	 */
	public static boolean isMobileNumberValid(String mobileNumber) {
		Pattern pattern = Pattern.compile("^0?9[0-9]{2}[0-9]{7}$");
		Matcher matcher = pattern.matcher(mobileNumber);
		return matcher.matches();
	}

}
