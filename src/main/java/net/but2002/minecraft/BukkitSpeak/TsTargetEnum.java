package net.but2002.minecraft.BukkitSpeak;

public enum TsTargetEnum {
	NONE("nobody", "null", "nothing"),
	CHANNEL("chat"),
	SERVER("broadcast");
	
	private final String[] names;
	
	TsTargetEnum(String... aliases) {
		if (aliases == null) {
			names = new String[1];
		} else {
			names = new String[aliases.length + 1];
			for (int i = 0; i < aliases.length; i++) {
				names[i + 1] = aliases[i];
			}
		}
		names[0] = name().toLowerCase();
	}
	
	public static TsTargetEnum getFromString(String input) {
		if (input == null) return null;
		for (TsTargetEnum value : TsTargetEnum.values()) {
			for (String name : value.names) {
				if (name.equalsIgnoreCase(input)) return value;
			}
		}
		return null;
	}
}
