package net.but2002.minecraft.BukkitSpeak.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MessageUtil {
	
	public static final String[] COLORS = {"", // 0
			"[color=#0000AA]", // 1
			"[color=#00AA00]", // 2
			"[color=#00AAAA]", // 3
			"[color=#AA0000]", // 4
			"[color=#AA00AA]", // 5
			"[color=#EEAA00]", // 6
			"[color=#999999]", // 7
			"[color=#555555]", // 8
			"[color=#4444FF]", // 9
			"[color=#44DD44]", // 10
			"[color=#3399FF]", // 11
			"[color=#FF3333]", // 12
			"[color=#FF33FF]", // 13
			"[color=#DDBB00]", // 14
			"[color=#FFFFFF]", // 15
			"[b]", // 16
			"[u]", // 17
			"[i]"}; // 18
	
	private MessageUtil() {};
	
	public static String toTeamspeak(String input, boolean color, boolean links) {
		if (input == null) return input;
		
		boolean colored = false, bold = false, underlined = false, italics = false;
		String s = input;
		if (color) {
			s = s.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "\u00A7$3");
			Matcher m = Pattern.compile("(\u00A7([a-fk-orA-FK-OR0-9]))").matcher(s);
			while (m.find()) {
				int i = m.start();
				int j = getIndex(s.charAt(i + 1));
				;
				
				if (j <= 15) {
					if (colored) {
						s = s.substring(0, i) + "[/color]" + s.substring(i);
						colored = false;
					}
					s = s.replaceFirst("\u00A7([a-fA-F0-9])", COLORS[j]);
					if (j != 0) colored = true;
				} else if (j == 16) {
					if (bold) {
						s = s.substring(0, i) + "[/b]" + s.substring(i);
					}
					s = s.replaceFirst("\u00A7[lL]", COLORS[j]);
					bold = true;
				} else if (j == 17) {
					if (underlined) {
						s = s.substring(0, i) + "[/u]" + s.substring(i);
					}
					s = s.replaceFirst("\u00A7[nN]", COLORS[j]);
					underlined = true;
				} else if (j == 18) {
					if (italics) {
						s = s.substring(0, i) + "[/i]" + s.substring(i);
					}
					s = s.replaceFirst("\u00A7[oO]", COLORS[j]);
					italics = true;
				} else if (j == 19 || j == 20) {
					s = s.replaceFirst("\u00A7[mMkK]", "");
				} else {
					s = s.replaceFirst("\u00A7r", "");
					if (colored) s = s.substring(0, i) + "[/color]" + s.substring(i);
					if (bold) s = s.substring(0, i) + "[/b]" + s.substring(i);
					if (italics) s = s.substring(0, i) + "[/i]" + s.substring(i);
					if (underlined) s = s.substring(0, i) + "[/u]" + s.substring(i);
					colored = false;
					bold = false;
					italics = false;
					underlined = false;
				}
			}
			
			if (colored) s += "[/color]";
			if (bold) s += "[/b]";
			if (italics) s += "[/i]";
			if (underlined) s += "[/u]";
		} else {
			s = s.replaceAll("((&|$|\u00A7)([a-fk-orA-FK-OR0-9]))", "");
		}
		
		if (links) {
			s = s.replaceAll("(?i)((http://|ftp://).*\\.?.+\\..+(/.*)?)", "\\[URL]$1\\[/URL]");
		} else {
			s = s.replaceAll("(?i)((http://|ftp://).*\\.?.+\\..+(/.*)?)", "");
		}
		
		return s;
	}
	
	public static String toMinecraft(String input, boolean color, boolean links) {
		if (input != null) {
			String s = input;
			if (color) {
				s = s.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "\u00A7$3");
			} else {
				s = s.replaceAll("((&|$|\u00A7)([a-fk-orA-FK-OR0-9]))", "");
			}
			if (!links) {
				s = s.replaceAll("(?i)((http://|ftp://).*\\.?.+\\..+(/.*)?)", "");
			}
			return s;
		}
		return null;
	}
	
	private static Integer getIndex(char c) {
		String s = String.valueOf(c);
		if (s.matches("[0-9a-fA-F]")) {
			return Integer.valueOf(s, 16);
		} else if (s.equalsIgnoreCase("l")) {
			return 16;
		} else if (s.equalsIgnoreCase("n")) {
			return 17;
		} else if (s.equalsIgnoreCase("o")) {
			return 18;
		} else if (s.equalsIgnoreCase("m")) {
			return 19;
		} else if (s.equalsIgnoreCase("k")) {
			return 20;
		} else {
			return 21;
		}
	}
}
