package net.but2002.minecraft.BukkitSpeak.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessageUtil {
	
	private static final String[] COLORS = {"", // 0
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
	
	private static final String URL_REGEX = "(?i)(^|[^\\w\\-\\.])(([\\w\\-]+://)?([\\w\\-]+\\.){0,2}[\\w\\-]+\\.\\w{2,4}(/[^\\s\\[]*)?)(?!\\S)";
	
	private MessageUtil() {};
	
	public static String toTeamspeak(String input, boolean color, boolean links) {
		if (input == null) return input;
		
		boolean colored = false, bold = false, underlined = false, italics = false;
		String s = input;
		if (color) {
			s = s.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "\u00A7$3");
			
			StringBuilder sb = new StringBuilder(s);
			Matcher m = Pattern.compile("(\u00A7([a-fk-orA-FK-OR0-9]))").matcher(sb);
			while (m.find()) {
				int i = m.start();
				int j = getIndex(sb.charAt(i + 1));
				
				if (j <= 15) {
					if (colored) {
						sb.insert(i, "[/color]");
						i += 8;
					}
					sb.replace(i, i + 2, COLORS[j]);
					colored = (j != 0);
				} else if (j == 16) {
					if (bold) {
						sb.insert(i, "[/b]");
						i += 4;
					}
					sb.replace(i, i + 2, COLORS[j]);
					bold = true;
				} else if (j == 17) {
					if (underlined) {
						sb.insert(i, "[/u]");
						i += 4;
					}
					sb.replace(i, i + 2, COLORS[j]);
					underlined = true;
				} else if (j == 18) {
					if (italics) {
						sb.insert(i, "[/i]");
						i += 4;
					}
					sb.replace(i, i + 2, COLORS[j]);
					italics = true;
				} else if (j == 19 || j == 20) {
					sb.replace(i, i + 2, "");
				} else {
					sb.replace(i, i + 2, "");
					if (underlined) sb.insert(i, "[/u]");
					if (italics) sb.insert(i, "[/i]");
					if (bold) sb.insert(i, "[/b]");
					if (colored) sb.insert(i, "[/color]");
					colored = false;
					bold = false;
					italics = false;
					underlined = false;
				}
				m.reset(sb);
			}
			if (colored) sb.append("[/color]");
			if (bold) sb.append("[/b]");
			if (italics) sb.append("[/i]");
			if (underlined) sb.append("[/u]");
			s = sb.toString();
		} else {
			s = s.replaceAll("((&|$|\u00A7)([a-fk-orA-FK-OR0-9]))", "");
		}
		
		if (links) {
			s = s.replaceAll(URL_REGEX, "$1\\[URL]$2\\[/URL]");
		} else {
			s = s.replaceAll(URL_REGEX, "$1");
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
			if (links) {
				s = s.replaceAll("(?i)\\[URL](\\S+)\\[/URL]", "$1$2");
			} else {
				s = s.replaceAll("(?i)\\[URL](\\S+)\\[/URL]", "$1");
				s = s.replaceAll(URL_REGEX, "");
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
