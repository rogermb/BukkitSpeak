package net.but2002.minecraft.BukkitSpeak.util;

import java.util.Calendar;
import java.util.Date;

public abstract class DateManager {
	
	public static String DateToString(Date date) {
		
		Calendar c = Calendar.getInstance();
		Date d = new Date();
		
		if (date == null) return null;
		if (d.getTime() - date.getTime() < 2000) {
			return "Now";
		}
		
		String s = String.format("%1$td:%1$tm:%1$tY %1$tH.%1$tM.%1$tS", c);
		
		return s;
	}
}
