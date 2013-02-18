package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetDefaultReason extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_DEFAULTREASON;
	private static final String ALLOWED_INPUT = "Any string";
	private static final String DESCRIPTION = "This will be the default if you don't add a reason to /tsa kick, channelkick or ban.";
	
	@Override
	public String getProperty() {
		return PROPERTY;
	}
	
	@Override
	public String getAllowedInput() {
		return ALLOWED_INPUT;
	}
	
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg) {
		tsSection.set(StringManager.TEAMSPEAK_DEFAULTREASON, arg);
		return true;
	}
}
