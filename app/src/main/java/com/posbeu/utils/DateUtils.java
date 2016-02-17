package com.posbeu.utils;

import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.text.format.DateFormat;

public class DateUtils {
	public static String getDate(Context context) {

		java.text.DateFormat df = DateFormat.getDateFormat(context);
		return df.format(new Date());
	}

	public static String getTime() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		int ora = cal.get(GregorianCalendar.HOUR_OF_DAY);
		int min = cal.get(GregorianCalendar.MINUTE);
		String sora = "" + ora;
		String smin = "" + min;
		if (ora < 10)
			sora = "0" + ora;
		if (min < 10)
			smin = "0" + smin;
		return sora + ":" + smin;

	}
}
