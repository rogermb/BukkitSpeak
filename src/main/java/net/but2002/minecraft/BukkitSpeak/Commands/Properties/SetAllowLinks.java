package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetAllowLinks extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_ALLOWLINKS;
	private static final String ALLOWED_INPUT = "true or false";
	private static final String DESCRIPTION = "If set to false, any links in messages will be filtered out.";
	
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
		if (arg.equalsIgnoreCase("true")) {
			tsSection.set(StringManager.TEAMSPEAK_ALLOWLINKS, true);
			send(sender, Level.INFO, "&aLinks in messages are now allowed.");
		} else if (arg.equalsIgnoreCase("false")) {
			tsSection.set(StringManager.TEAMSPEAK_ALLOWLINKS, false);
			send(sender, Level.INFO, "&aLinks in messages are now forbidden.");
		} else {
			send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
			return false;
		}
		return true;
	}
}
