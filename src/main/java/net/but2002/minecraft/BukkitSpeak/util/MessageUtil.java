package net.but2002.minecraft.BukkitSpeak.util;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessageUtil {

	private static final String URL_REGEX = "(?i)(^|[^\\w\\-\\.])(([\\w\\-]+://)?"
			+ "([\\w\\-]+\\.){1,3}[a-z]{2,4}(/[^\\s\\[]*)?)(?!\\S)";

	private MessageUtil() {};

	public static String toTeamspeak(String input, boolean color, boolean links) {
		if (input == null) return null;

		String s = input;
		if (color) {
			s = s.replaceAll("(&|$)([a-fk-orA-FK-OR0-9])", "\u00A7$2");
			s = s.replaceAll("\\[", "\\\\[");

			StringBuilder out = new StringBuilder();
			Deque<FormatString> deque = new LinkedList<FormatString>();
			Matcher m = Pattern.compile("\u00A7[a-fk-orA-FK-OR0-9]").matcher(s);

			int previousIndex = 0;
			while (m.find()) {
				FormatString format = FormatString.fromChar(s.charAt(m.start() + 1));
				out.append(s.substring(previousIndex, m.start()));

				if (format == FormatString.RESET) {
					out.append(resetStack(deque));
				} else {
					out.append(pushStack(deque, format));
				}

				previousIndex = m.start() + 2;
			}

			out.append(s.substring(previousIndex));
			out.append(resetStack(deque));
			s = out.toString();
		} else {
			s = s.replaceAll("(&|$|\u00A7)[a-fk-orA-FK-OR0-9]", "");
		}

		if (links) {
			s = s.replaceAll(URL_REGEX, "$1\\[URL]$2\\[/URL]");
		} else {
			s = s.replaceAll(URL_REGEX, "$1");
		}

		return s;
	}

	public static String getFormatString(String input) {
		String s = input.replaceAll("(&|$)([a-fk-orA-FK-OR0-9])", "\u00A7$2");
		Deque<FormatString> deque = new LinkedList<FormatString>();
		Matcher m = Pattern.compile("\u00A7[a-fk-orA-FK-OR0-9]").matcher(s);

		while (m.find()) {
			FormatString format = FormatString.fromChar(s.charAt(m.start() + 1));

			if (format == FormatString.RESET) {
				resetStack(deque);
			} else {
				pushStack(deque, format);
			}
		}

		StringBuilder result = new StringBuilder();
		for (Iterator<FormatString> reverse = deque.descendingIterator(); reverse.hasNext();) {
			result.append(reverse.next().getMinecraftFormatString());
		}
		return result.toString();
	}

	public static String toMinecraft(String input, boolean color, boolean links) {
		if (input != null) {
			String s = input;
			if (color) {
				s = s.replaceAll("(&|$)([a-fk-orA-FK-OR0-9])", "\u00A7$2");
			} else {
				s = s.replaceAll("((&|$|\u00A7)([a-fk-orA-FK-OR0-9]))", "");
			}
			if (links) {
				s = s.replaceAll("(?i)\\[URL]([\\S\\n]+)\\[/URL]", "$1");
			} else {
				s = s.replaceAll("(?i)\\[URL](\\S+)\\[/URL]", "");
				s = s.replaceAll(URL_REGEX, "$1");
			}
			return s;
		}
		return null;
	}

	private static String pushStack(Deque<FormatString> s, FormatString value) {
		StringBuilder sb = new StringBuilder();

		sb.append(popStack(s, value));
		sb.append(value.getOpeningTeamspeakBB());
		s.addFirst(value);
		return sb.toString();
	}

	private static String popStack(Deque<FormatString> s, FormatString value) {
		StringBuilder sb = new StringBuilder();
		LinkedList<FormatString> previousTags = new LinkedList<FormatString>();
		FormatString match = null;

		for (FormatString element : s) {
			if (element.sharesEqualTag(value)) {
				match = element;
				break;
			}
			previousTags.add(element);
		}

		if (match == null) return "";

		for (Iterator<FormatString> forward = previousTags.iterator(); forward.hasNext();) {
			sb.append(forward.next().getClosingTeamspeakBB());
		}
		sb.append(match.getClosingTeamspeakBB());
		s.removeFirstOccurrence(match);
		for (Iterator<FormatString> reverse = previousTags.descendingIterator(); reverse.hasNext();) {
			sb.append(reverse.next().getOpeningTeamspeakBB());
		}

		return sb.toString();
	}

	private static String resetStack(Deque<FormatString> s) {
		StringBuilder sb = new StringBuilder();
		for (FormatString element : s) {
			sb.append(element.getClosingTeamspeakBB());
		}
		s.clear();
		return sb.toString();
	}
}
