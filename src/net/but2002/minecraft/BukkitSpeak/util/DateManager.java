package net.but2002.minecraft.BukkitSpeak.util;

import java.util.Calendar;
import java.util.Date;

public abstract class DateManager {
	
	public static String DateToString(Date date) {
		
		
		StringBuilder sb = new StringBuilder();
		Calendar c = Calendar.getInstance();
		Date d = new Date();
		
		if (d.getTime() - date.getTime() < 2000) {
			return "Now";
		}
		
		sb.append(c.get(Calendar.YEAR) + ":");
		sb.append(c.get(Calendar.MONTH) + ":");
		sb.append(c.get(Calendar.DAY_OF_MONTH) + " ");
		sb.append(c.get(Calendar.HOUR_OF_DAY) + ".");
		sb.append(c.get(Calendar.MINUTE) + ".");
		sb.append(c.get(Calendar.SECOND));
		
		return sb.toString();
	}
	
	
}
