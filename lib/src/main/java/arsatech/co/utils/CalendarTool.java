package arsatech.co.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Aref Bahreini Nejad on 27/04/2019.
 * Updated on 27/04/2019
 */
public class CalendarTool {

	/**
	 * Gregorian & Jalali (Hijri_Shamsi,Solar) Date Converter Functions
	 * Author: JDF.SCR.IR =>> Download Full Version : http://jdf.scr.ir/jdf
	 * License: GNU/LGPL _ Open Source & Free _ Version: 2.72 : [2017=1396]
	 * --------------------------------------------------------------------
	 * 1461 = 365*4 + 4/4   &  146097 = 365*400 + 400/4 - 400/100 + 400/400
	 * 12053 = 365*33 + 32/4    &    36524 = 365*100 + 100/4 - 100/100
	 */

	private int jYear;
	private int jMonth;
	private int jDay;
	private int gYear;
	private int gMonth;
	private int gDay;
	private Calendar gregorianCalendar = Calendar.getInstance();
	private String[] persianMonthNames = new String[]{
			"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
	};
	private String[] gregorianMonthNames = new String[]{
			"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
	};

	public CalendarTool() {
		gregorianCalendar.set(Calendar.HOUR, 0);
		gregorianCalendar.set(Calendar.MINUTE, 0);
		gregorianCalendar.set(Calendar.SECOND, 0);
		gYear = gregorianCalendar.get(Calendar.YEAR);
		gMonth = gregorianCalendar.get(Calendar.MONTH) + 1;
		gDay = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
		int[] jDate = gregorian_to_jalali(gYear, gMonth, gDay);
		jYear = jDate[0];
		jMonth = jDate[1];
		jDay = jDate[2];
	}

	public CalendarTool(Calendar gregorianCalendar) {
		gYear = gregorianCalendar.get(Calendar.YEAR);
		gMonth = gregorianCalendar.get(Calendar.MONTH) + 1;
		gDay = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
		int[] jDate = gregorian_to_jalali(gYear, gMonth, gDay);
		jYear = jDate[0];
		jMonth = jDate[1];
		jDay = jDate[2];
		this.gregorianCalendar.set(gYear, gMonth, gDay, 0, 0, 0);
	}

	public CalendarTool(int year, int month, int day, boolean thisIsGregorian) {
		if (thisIsGregorian) {
			this.gYear = year;
			this.gMonth = month;
			this.gDay = day;
			int[] jDate = gregorian_to_jalali(gYear, gMonth, gDay);
			jYear = jDate[0];
			jMonth = jDate[1];
			jDay = jDate[2];
		} else {
			this.jYear = year;
			this.jMonth = month;
			this.jDay = day;
			int[] gDate = jalali_to_gregorian(jYear, jMonth, jDay);
			gYear = gDate[0];
			gMonth = gDate[1];
			gDay = gDate[2];
		}
		gregorianCalendar.set(gYear, gMonth, gDay, 0, 0, 0);
	}

	public void addDays(int days) {
		gregorianCalendar.add(Calendar.DAY_OF_YEAR, days);
		setGregorianDate(gregorianCalendar);
	}

	public void subDays(int days) {
		gregorianCalendar.add(Calendar.DAY_OF_YEAR, -days);
		setGregorianDate(gregorianCalendar);
	}

	public void setGregorianDate(Calendar gregorianCalendar) {
		gYear = gregorianCalendar.get(Calendar.YEAR);
		gMonth = gregorianCalendar.get(Calendar.MONTH) + 1;
		gDay = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
		int[] jDate = gregorian_to_jalali(gYear, gMonth, gDay);
		jYear = jDate[0];
		jMonth = jDate[1];
		jDay = jDate[2];
		this.gregorianCalendar.set(gYear, gMonth, gDay, 0, 0, 0);
	}

	public void setGregorianDate(int year, int month, int day) {
		this.gYear = year;
		this.gMonth = month;
		this.gDay = day;
		int[] jDate = gregorian_to_jalali(gYear, gMonth, gDay);
		jYear = jDate[0];
		jMonth = jDate[1];
		jDay = jDate[2];
		gregorianCalendar.set(gYear, gMonth, gDay, 0, 0, 0);
	}

	public int getGregorianYear() {
		return gYear;
	}

	public int getGregorianMonth() {
		return gMonth;
	}

	public int getGregorianDay() {
		return gDay;
	}

	public String getGregorianMonthName() {
		return gregorianMonthNames[gMonth - 1];
	}

	public String getGregorianDate(String format) {
		String jDate = format;
		if (jDate.contains("yyyy"))
			jDate = jDate.replaceAll("yyyy", String.format("%02d", gYear));
		if (jDate.contains("yy"))
			jDate = jDate.replaceAll("yy", (gYear % 100) + "");
		if (jDate.contains("mm"))
			jDate = jDate.replaceAll("mm", String.format("%02d", gMonth));
		if (jDate.contains("m"))
			jDate = jDate.replaceAll("m", gMonth + "");
		if (jDate.contains("M"))
			jDate = jDate.replaceAll("M", getGregorianMonthName() + "");
		if (jDate.contains("dd"))
			jDate = jDate.replaceAll("dd", String.format("%02d", gDay));
		if (jDate.contains("d"))
			jDate = jDate.replaceAll("d", gDay + "");
		return jDate;
	}

	public Calendar getGregorianCalendar() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(gYear, gMonth - 1, gDay, 0, 0, 0);
		return calendar;
	}

	public void setJalaliDate(int year, int month, int day) {
		this.jYear = year;
		this.jMonth = month;
		this.jDay = day;
		int[] gDate = jalali_to_gregorian(jYear, jMonth, jDay);
		gYear = gDate[0];
		gMonth = gDate[1];
		gDay = gDate[2];
		gregorianCalendar.set(gYear, gMonth, gDay, 0, 0, 0);
	}

	public int getJalaliYear() {
		return jYear;
	}

	public int getJalaliMonth() {
		return jMonth;
	}

	public int getJalaliDay() {
		return jDay;
	}

	public String getJalaliDate(String format) {
		String jDate = format;
		if (jDate.contains("yyyy"))
			jDate = jDate.replaceAll("yyyy", String.format("%02d", jYear));
		if (jDate.contains("yy"))
			jDate = jDate.replaceAll("yy", (jYear % 100) + "");
		if (jDate.contains("mm"))
			jDate = jDate.replaceAll("mm", String.format("%02d", jMonth));
		if (jDate.contains("m"))
			jDate = jDate.replaceAll("m", jMonth + "");
		if (jDate.contains("M"))
			jDate = jDate.replaceAll("M", getJalaliMonthName() + "");
		if (jDate.contains("dd"))
			jDate = jDate.replaceAll("dd", String.format("%02d", jDay));
		if (jDate.contains("d"))
			jDate = jDate.replaceAll("d", jDay + "");
		return jDate;
	}

	public String getJalaliMonthName() {
		return persianMonthNames[jMonth - 1];
	}

	public Calendar setTimestamp(long timestamp) {
		gregorianCalendar.setTimeInMillis(timestamp * 1000L);
		setGregorianDate(gregorianCalendar);
		return gregorianCalendar;
	}

	public long getTimestamp() {
		return getGregorianCalendar().getTimeInMillis() / 1000;
	}

	public long dateDifferenceInDays(Calendar calendar) {
		long diff = calendar.getTimeInMillis() - (gregorianCalendar.getTimeInMillis() - 1000);
		return diff / (24 * 60 * 60 * 1000);
	}

	public long dateDifferenceInSeconds(Calendar calendar) {
		long diff = calendar.getTimeInMillis() - (gregorianCalendar.getTimeInMillis() - 1000);
		return diff / 1000;
	}

	private static int[] gregorian_to_jalali(int gy, int gm, int gd) {
		int[] g_d_m = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
		int jy;
		if (gy > 1600) {
			jy = 979;
			gy -= 1600;
		} else {
			jy = 0;
			gy -= 621;
		}
		int gy2 = (gm > 2) ? (gy + 1) : gy;
		int days = (365 * gy) + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400 - 80 + gd + g_d_m[gm - 1];
		jy += 33 * (days / 12053);
		days %= 12053;
		jy += 4 * (days / 1461);
		days %= 1461;
		if (days > 365) {
			jy += (days - 1) / 365;
			days = (days - 1) % 365;
		}
		int jm = (days < 186) ? 1 + (days / 31) : 7 + ((days - 186) / 30);
		int jd = 1 + ((days < 186) ? (days % 31) : ((days - 186) % 30));
		int[] out = {jy, jm, jd};
		return out;
	}

	private static int[] jalali_to_gregorian(int jy, int jm, int jd) {
		int gy;
		if (jy > 979) {
			gy = 1600;
			jy -= 979;
		} else {
			gy = 621;
		}
		int days = (365 * jy) + ((jy / 33) * 8) + ((jy % 33) + 3) / 4 + 78 + jd + ((jm < 7) ? (jm - 1) * 31 : ((jm - 7) * 30) + 186);
		gy += 400 * (days / 146097);
		days %= 146097;
		if (days > 36524) {
			gy += 100 * (--days / 36524);
			days %= 36524;
			if (days >= 365) days++;
		}
		gy += 4 * (days / 1461);
		days %= 1461;
		if (days > 365) {
			gy += (days - 1) / 365;
			days = (days - 1) % 365;
		}
		int gd = days + 1;
		int[] sal_a = {0, 31, ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int gm;
		for (gm = 0; gm < 13; gm++) {
			int v = sal_a[gm];
			if (gd <= v) break;
			gd -= v;
		}
		int[] out = {gy, gm, gd};
		return out;
	}

	public static Calendar timestampToCalendar(long timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp * 1000L);
		return calendar;
	}

	public static long dateDifferenceInDays(Calendar newDate, Calendar oldDate) {
		long diff = newDate.getTimeInMillis() - (oldDate.getTimeInMillis() - 1000); //ms
		return diff / (24 * 60 * 60 * 1000);
	}

	public static long dateDifferenceInSeconds(Calendar newDate, Calendar oldDate) {
		long diff = newDate.getTimeInMillis() - (oldDate.getTimeInMillis() - 1000); //ms
		return diff / 1000;
	}

}
