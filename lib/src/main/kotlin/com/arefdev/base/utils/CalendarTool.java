package com.arefdev.base.utils;

import androidx.annotation.NonNull;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Updated on 15/10/2021
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
@SuppressWarnings("unused")
public class CalendarTool {

    //region Static Methods

    /**
     * @return new instance with current time with the device's default time zone.
     */
    public static CalendarTool now() {
        return new CalendarTool();
    }

    public static long diffInSeconds(CalendarTool date1, CalendarTool date2) {
        return date1.diffInSeconds(date2);
    }

    public static long diffInSeconds(Calendar date1, Calendar date2) {
        return diffInSeconds(new CalendarTool(date1), new CalendarTool(date2));
    }

    public static int diffInDays(CalendarTool date1, CalendarTool date2) {
        return date1.diffInDays(date2);
    }

    public static int diffInDays(Calendar date1, Calendar date2) {
        return diffInDays(new CalendarTool(date1), new CalendarTool(date2));
    }

    //endregion

    private static final Map<Integer, String> gregorianDayNames = new HashMap<Integer, String>() {{
        put(Calendar.SATURDAY, "Saturday");
        put(Calendar.SUNDAY, "Sunday");
        put(Calendar.MONDAY, "Monday");
        put(Calendar.TUESDAY, "Tuesday");
        put(Calendar.WEDNESDAY, "Wednesday");
        put(Calendar.THURSDAY, "Thursday");
        put(Calendar.FRIDAY, "Friday");
    }};
    private static final String[] gregorianMonthNames = new String[]{
            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };

    private final Calendar calendar = Calendar.getInstance(/*TimeZone.getTimeZone("GMT")*/);

    /**
     * New instance with current time in the device's default time zone.
     */
    public CalendarTool() {
        setDate(calendar);
    }

    public CalendarTool(Calendar calendar) {
        setDate(calendar);
    }

    /**
     * @param str Formatted in "yyyy-MM-dd'T'HH:mm:ssZ"
     */
    public CalendarTool(String str) {
        parse(str);
    }

    /**
     * New instance with specified parameters in the device's default time zone.
     */
    public CalendarTool(int year, int month, int day) {
        setDate(year, month, day);
    }

    /**
     * New instance with specified parameters in the device's default time zone.
     */
    public CalendarTool(int year, int month, int day, int hour, int minute, int second) {
        setDate(year, month, day, hour, minute, second);
    }

    /**
     * New instance with specified parameter in the device's default time zone.
     */
    public CalendarTool(long timeInMillis) {
        setTimeInMillis(timeInMillis);
    }

    private SimpleDateFormat getFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH) {
            @NonNull
            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
                StringBuffer rfcFormat = super.format(date, toAppendTo, pos);
                return rfcFormat.insert(rfcFormat.length() - 2, ":");
            }

            @Override
            public Date parse(String text, ParsePosition pos) {
                if (text.length() > 3)
                    text = text.substring(0, text.length() - 3) + text.substring(text.length() - 2);
                return super.parse(text, pos);
            }
        };
    }

    /**
     * @param str Formatted in "yyyy-MM-dd'T'HH:mm:ssZ" like "2021-10-14T23:17:31.624318+03:30"
     */
    public CalendarTool parse(String str) throws RuntimeException {
        if (str.lastIndexOf(".") >= 0) {
            str = str.substring(0, str.lastIndexOf(".")) +
                    str.substring(str.lastIndexOf("+") >= 0 ?
                            str.lastIndexOf("+") :
                            str.lastIndexOf("-")
                    );
        }
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(Objects.requireNonNull(getFormatter().parse(str)));
            return setDate(cal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Formatted in "yyyy-MM-dd'T'HH:mm:ssZ" like "2021-10-14T23:17:31.624318+03:30"
     */
    public String toStandardFormat() {
        return getFormatter().format(calendar.getTime());
    }

    public CalendarTool setTimeZoneGMT() {
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        return this;
    }

    /**
     * @return new instance with the GMT time zone.
     */
    public CalendarTool withTimeZoneGMT() {
        return clone().setTimeZoneGMT();
    }

    public CalendarTool setDefaultTimeZone() {
        calendar.setTimeZone(TimeZone.getDefault());
        return this;
    }

    /**
     * @return new instance with the default <code>TimeZone</code> for this host.
     */
    public CalendarTool withDefaultTimeZone() {
        return clone().setDefaultTimeZone();
    }

    /**
     * @see Calendar#add(int, int)
     */
    public CalendarTool add(int field, int amount) {
        calendar.add(field, amount);
        setDate(calendar);
        return this;
    }

    public CalendarTool addDays(int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days);
        setDate(calendar);
        return this;
    }

    public CalendarTool subDays(int days) {
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        setDate(calendar);
        return this;
    }

    public CalendarTool setDate(Calendar calendar) {
        this.calendar.setTimeZone(calendar.getTimeZone());
        this.calendar.setTimeInMillis(calendar.getTimeInMillis());

        int[] jDate = gregorianToJalali(getYear(), getMonth(), getDay());
        jYear = jDate[0];
        jMonth = jDate[1];
        jDay = jDate[2];

        return this;
    }

    public CalendarTool setDate(int year, int month, int day) {
        return setDate(year, month, day, 0, 0, 0);
    }

    public CalendarTool setDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = getCalendar();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return setDate(calendar);
    }

    public CalendarTool setTimeInMillis(long timeInMillis) {
        calendar.setTimeInMillis(timeInMillis);
        setDate(calendar);
        return this;
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    public String getDayName() {
        return gregorianDayNames.get(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public String getMonthName() {
        return gregorianMonthNames[getMonth() - 1];
    }

    /**
     * <p>yyyy : Long Year</p>
     * <p>yy : Short Year</p>
     * <p>mm : Long Month Number</p>
     * <p>m : Short Month Number</p>
     * <p>MMMM : Long Month Name</p>
     * <p>MM : Short Month Name</p>
     * <p>DDDD : Long Day Name</p>
     * <p>DD : Short Day Name</p>
     * <p>dd : Long Day Number</p>
     * <p>d : Short Day Number</p>
     * <p>hh : Long Hour</p>
     * <p>h : Short Hour</p>
     * <p>nn : Long Minute</p>
     * <p>n : Short Minute</p>
     * <p>ss : Long Seconds</p>
     * <p>s : Short Seconds</p>
     */
    public String format(String format) {
        String jDate = format;

        if (jDate.contains("yyyy"))    // Long Year
            jDate = jDate.replace("yyyy", String.format(Locale.ENGLISH, "%02d", getYear()));

        if (jDate.contains("yy"))    // Short Year
            jDate = jDate.replace("yy", String.valueOf(getYear() % 100));

        if (jDate.contains("mm"))    // Long Month Number
            jDate = jDate.replace("mm", String.format(Locale.ENGLISH, "%02d", getMonth()));

        if (jDate.contains("m"))    // Short Month Number
            jDate = jDate.replace("m", String.valueOf(getMonth()));

        if (jDate.contains("dd"))    // Long Day Number
            jDate = jDate.replace("dd", String.format(Locale.ENGLISH, "%02d", getDay()));

        if (jDate.contains("d"))    // Short Day Number
            jDate = jDate.replace("d", String.valueOf(getDay()));

        if (jDate.contains("hh"))    // Long Hour
            jDate = jDate.replace("hh", String.format(Locale.ENGLISH, "%02d", getHour()));

        if (jDate.contains("h"))    // Short Hour
            jDate = jDate.replace("h", String.valueOf(getHour()));

        if (jDate.contains("nn"))    // Long Minute
            jDate = jDate.replace("nn", String.format(Locale.ENGLISH, "%02d", getMinute()));

        if (jDate.contains("n"))    // Short Minute
            jDate = jDate.replace("n", String.valueOf(getMinute()));

        if (jDate.contains("ss"))    // Long Seconds
            jDate = jDate.replace("ss", String.format(Locale.ENGLISH, "%02d", getSecond()));

        if (jDate.contains("s"))    // Short Seconds
            jDate = jDate.replace("s", String.valueOf(getSecond()));

        if (jDate.contains("MMMM"))    // Long Month Name
            jDate = jDate.replace("MMMM", getMonthName());

        if (jDate.contains("MM"))    // Short Month Name
            jDate = jDate.replace("MM", getMonthName().substring(0, 3));

        if (jDate.contains("DDDD"))    // Long Day Name
            jDate = jDate.replace("DDDD", getDayName());

        if (jDate.contains("DD"))    // Short Day Name
            jDate = jDate.replace("DD", getDayName().substring(0, 3));

        return jDate;
    }

    public String formatFullYearMonthDay() {
        return format("yyyy/mm/dd");
    }

    public String formatFullDayMonthYear() {
        return format("dd/mm/yyyy");
    }

    public String formatShortDateOnly() {
        return format("yy/m/d");
    }

    public String formatFullTimeOnly() {
        return format("hh:nn:ss");
    }

    public String formatFullDateTime() {
        return format("yyyy/mm/dd hh:nn:ss");
    }

    public String formatFullHourMinute() {
        return format("hh:nn");
    }

    public String formatShortHourMinute() {
        return format("h:n");
    }

    public Calendar getCalendar() {
        return (Calendar) calendar.clone();
    }

    public long diffInSeconds(CalendarTool ct) {
        return diffInSeconds(ct.calendar);
    }

    public long diffInSeconds(Calendar calendar) {
        long diff = getTimeInMillis() - calendar.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toSeconds(diff);
    }

    public int diffInDays(CalendarTool ct) {
        return diffInDays(ct.calendar);
    }

    public int diffInDays(Calendar calendar) {
        CalendarTool temp = new CalendarTool(calendar);
        temp.setDate(temp.getYear(), temp.getMonth(), temp.getDay(), getHour(), getMinute(), getSecond());
        long diffInSeconds = diffInSeconds(temp.getCalendar());
        return (int) (diffInSeconds / TimeUnit.DAYS.toSeconds(1));
    }

    public boolean isBefore(CalendarTool ct) {
        return withTimeZoneGMT().getTimeInMillis() - ct.withTimeZoneGMT().getTimeInMillis() < 0;
    }

    public boolean isAfter(CalendarTool ct) {
        return withTimeZoneGMT().getTimeInMillis() - ct.withTimeZoneGMT().getTimeInMillis() > 0;
    }

    @NonNull
    @Override
    public CalendarTool clone() {
        return new CalendarTool(calendar);
    }

    //region Jalali Calendar

    /**
     * Gregorian & Jalali (Hijri_Shamsi,Solar) Date Converter Functions
     * Author: JDF.SCR.IR =>> Download Full Version : http://jdf.scr.ir/jdf
     * License: GNU/LGPL _ Open Source & Free _ Version: 2.72 : [2017=1396]
     * --------------------------------------------------------------------
     * 1461 = 365*4 + 4/4   &  146097 = 365*400 + 400/4 - 400/100 + 400/400
     * 12053 = 365*33 + 32/4    &    36524 = 365*100 + 100/4 - 100/100
     */

    private static final Map<Integer, String> persianDayNames = new HashMap<Integer, String>() {{
        put(Calendar.SATURDAY, "شنبه");
        put(Calendar.SUNDAY, "یکشنبه");
        put(Calendar.MONDAY, "دوشنبه");
        put(Calendar.TUESDAY, "سه‌شنبه");
        put(Calendar.WEDNESDAY, "چهارشنبه");
        put(Calendar.THURSDAY, "پنجشنبه");
        put(Calendar.FRIDAY, "جمعه");
    }};
    private static final String[] persianMonthNames = new String[]{
            "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    };
    // Jalali values
    private int jYear;
    private int jMonth;
    private int jDay;

    public CalendarTool setJalaliDate(int year, int month, int day) {
        return setJalaliDate(year, month, day, 0, 0, 0);
    }

    public CalendarTool setJalaliDate(int year, int month, int day, int hour, int minute, int second) {
        this.jYear = year;
        this.jMonth = month;
        this.jDay = day;
        int[] gDate = jalaliToGregorian(jYear, jMonth, jDay);
        int gYear = gDate[0];
        int gMonth = gDate[1];
        int gDay = gDate[2];
        return setDate(gYear, gMonth, gDay, hour, minute, second);
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

    public String getJalaliDayName() {
        return persianDayNames.get(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public String getJalaliMonthName() {
        return persianMonthNames[getJalaliMonth() - 1];
    }

    /**
     * <p>yyyy : Long Year</p>
     * <p>yy : Short Year</p>
     * <p>mm : Long Month Number</p>
     * <p>m : Short Month Number</p>
     * <p>MMMM : Long Month Name</p>
     * <p>DDDD : Long Day Name</p>
     * <p>dd : Long Day Number</p>
     * <p>d : Short Day Number</p>
     * <p>hh : Long Hour</p>
     * <p>h : Short Hour</p>
     * <p>nn : Long Minute</p>
     * <p>n : Short Minute</p>
     * <p>ss : Long Seconds</p>
     * <p>s : Short Seconds</p>
     */
    public String formatJalali(String format) {
        String jDate = format;

        if (jDate.contains("yyyy"))    // Long Year
            jDate = jDate.replace("yyyy", String.format(Locale.ENGLISH, "%02d", getJalaliYear()));

        if (jDate.contains("yy"))    // Short Year
            jDate = jDate.replace("yy", String.valueOf(getJalaliYear() % 100));

        if (jDate.contains("mm"))    // Long Month Number
            jDate = jDate.replace("mm", String.format(Locale.ENGLISH, "%02d", getJalaliMonth()));

        if (jDate.contains("m"))    // Short Month Number
            jDate = jDate.replace("m", String.valueOf(getJalaliMonth()));

        if (jDate.contains("dd"))    // Long Day Number
            jDate = jDate.replace("dd", String.format(Locale.ENGLISH, "%02d", getJalaliDay()));

        if (jDate.contains("d"))    // Short Day Number
            jDate = jDate.replace("d", String.valueOf(getJalaliDay()));

        if (jDate.contains("hh"))    // Long Hour
            jDate = jDate.replace("hh", String.format(Locale.ENGLISH, "%02d", getHour()));

        if (jDate.contains("h"))    // Short Hour
            jDate = jDate.replace("h", String.valueOf(getHour()));

        if (jDate.contains("nn"))    // Long Minute
            jDate = jDate.replace("nn", String.format(Locale.ENGLISH, "%02d", getMinute()));

        if (jDate.contains("n"))    // Short Minute
            jDate = jDate.replace("n", String.valueOf(getMinute()));

        if (jDate.contains("ss"))    // Long Seconds
            jDate = jDate.replace("ss", String.format(Locale.ENGLISH, "%02d", getSecond()));

        if (jDate.contains("s"))    // Short Seconds
            jDate = jDate.replace("s", String.valueOf(getSecond()));

        if (jDate.contains("MMMM"))    // Long Month Name
            jDate = jDate.replace("MMMM", getJalaliMonthName());

        if (jDate.contains("MM"))    // Short Month Name
            jDate = jDate.replace("MM", getJalaliMonthName());

        if (jDate.contains("DDDD"))    // Long Day Name
            jDate = jDate.replace("DDDD", getJalaliDayName());

        if (jDate.contains("DD"))    // Short Day Name
            jDate = jDate.replace("DD", getJalaliDayName());

        return jDate;
    }

    private static int[] gregorianToJalali(int gy, int gm, int gd) {
        int[] gdm = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
        int jy;
        if (gy > 1600) {
            jy = 979;
            gy -= 1600;
        } else {
            jy = 0;
            gy -= 621;
        }
        int gy2 = (gm > 2) ? (gy + 1) : gy;
        int days = (365 * gy) + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400 - 80 + gd + gdm[gm - 1];
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
        return new int[]{jy, jm, jd};
    }

    private static int[] jalaliToGregorian(int jy, int jm, int jd) {
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
        int[] salA = {0, 31, ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int gm;
        for (gm = 0; gm < 13; gm++) {
            int v = salA[gm];
            if (gd <= v) break;
            gd -= v;
        }
        return new int[]{gy, gm, gd};
    }

    //endregion
}
