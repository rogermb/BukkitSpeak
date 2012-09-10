package net.but2002.minecraft.BukkitSpeak.util;

import java.util.Calendar;
import java.util.Date;

public abstract class DateManager {
	
	private static final int TIME_TOLERANCE = 2000;
	
	public static String dateToString(Date date) {
		
		Calendar c = Calendar.getInstance();
		Date d = new Date();
		c.setTime(date);
		
		if (date == null) return null;
		if (d.getTime() - date.getTime() < TIME_TOLERANCE) {
			return "Just now";
		}
		
		String s = String.format("%1$td.%1$tm.%1$tY %1$tH:%1$tM:%1$tS", c);
		
		return s;
	}
}
