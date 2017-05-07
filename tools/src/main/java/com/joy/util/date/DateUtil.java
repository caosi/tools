package com.joy.util.date;

import java.util.Calendar;

public final class DateUtil {

	private DateUtil() {
	}

	public static final long SECOND = 1000;

	public static final long MINUTES = 60 * SECOND;

	public static final long HOUR = 60 * MINUTES;

	public static final long DAY = 24 * HOUR;


	/**
	 * 距离现在多少天，（只比较日期差）
	 */
	public static int diffDays(long time) {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int day = now.get(Calendar.DAY_OF_YEAR);

		Calendar other = Calendar.getInstance();
		other.setTimeInMillis(time);
		int otherYear = other.get(Calendar.YEAR);
		int otherDay = other.get(Calendar.DAY_OF_YEAR);

		int diffYear = year - otherYear;

		if (diffYear == 0) {
			return day > otherDay ? day - otherDay : otherDay - day;
		}
		int min = diffYear > 0 ? otherYear : year;
		int max = diffYear > 0 ? year : otherYear;
		int sum = 0;
		Calendar calendar = Calendar.getInstance();
		for (int by = min + 1; by < max; by++) {
			calendar.clear();
			calendar.set(Calendar.YEAR, by);
			int day1 = calendar.get(Calendar.DAY_OF_YEAR);

			calendar.add(Calendar.YEAR, 1);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			int day2 = calendar.get(Calendar.DAY_OF_YEAR);
			sum += (day2 - day1);
		}
		if (diffYear > 0) {
			other.clear();
			other.set(Calendar.YEAR, otherYear+1);
			other.add(Calendar.DAY_OF_MONTH, -1);
			return day + other.get(Calendar.DAY_OF_YEAR) - otherDay + sum;
		} else {
			now.clear();
			now.set(Calendar.YEAR, year+1);
			now.add(Calendar.DAY_OF_MONTH, -1);
			return otherDay + now.get(Calendar.DAY_OF_YEAR) - day + sum;
		}

	}

}
