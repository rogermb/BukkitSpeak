package net.but2002.minecraft.BukkitSpeak.util;

public enum FormatString {

	BLACK('0', "color", "#000000"),
	DARK_BLUE('1', "color", "#0000AA"),
	DARK_GREEN('2', "color", "#00AA00"),
	DARK_AQUA('3', "color", "#00AAAA"),
	DARK_RED('4', "color", "#AA0000"),
	PURPLE('5', "color", "#AA00AA"),
	GOLD('6', "color", "#EEAA00"),
	GRAY('7', "color", "#999999"),
	LIGHT_GRAY('8', "color", "#555555"),
	BLUE('9', "color", "#4444FF"),
	GREEN('a', "color", "#44DD44"),
	AQUA('b', "color", "#3399FF"),
	RED('c', "color", "#FF3333"),
	PINK('d', "color", "#FF33FF"),
	YELLOW('e', "color", "#DDBB00"),
	WHITE('f', "color", "#FFFFFF"),
	MAGIC('k', null),
	BOLD('l', "b"),
	STRIKETHROUGH('m', null),
	UNDERLINE('n', "u"),
	ITALIC('o', "i"),
	RESET('r', null);

	private final char minecraftChar;
	private final String teamspeakBB;
	private final String colorValue;

	FormatString(char mcChar, String tsBB) {
		this(mcChar, tsBB, null);
	}

	FormatString(char mcChar, String tsBB, String color) {
		minecraftChar = mcChar;
		teamspeakBB = tsBB;
		colorValue = color;
	}

	public static FormatString fromChar(char c) {
		for (FormatString fs : values()) {
			if (c == fs.minecraftChar) return fs;
		}
		return null;
	}

	public char getMinecraftChar() {
		return minecraftChar;
	}

	public String getMinecraftFormatString() {
		return "\u00A7" + String.valueOf(minecraftChar);
	}

	public String getTeamspeakBBTag() {
		return teamspeakBB;
	}

	public String getColorValue() {
		return colorValue;
	}

	public String getOpeningTeamspeakBB() {
		if (teamspeakBB == null) return "";

		StringBuilder sb = new StringBuilder();
		sb.append("[").append(teamspeakBB);
		if (colorValue != null) sb.append("=").append(colorValue);
		sb.append("]");

		return sb.toString();
	}

	public String getClosingTeamspeakBB() {
		if (teamspeakBB == null) return "";

		StringBuilder sb = new StringBuilder();
		sb.append("[/").append(teamspeakBB).append("]");
		return sb.toString();
	}

	public boolean sharesEqualTag(FormatString other) {
		if (other == null) return false;
		if ("color".equals(teamspeakBB) && "color".equals(other.teamspeakBB)) return true;
		return this == other;
	}
}
