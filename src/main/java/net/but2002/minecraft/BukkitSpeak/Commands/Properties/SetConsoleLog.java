package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetConsoleLog extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_CONSOLE;
	private static final String ALLOWED_INPUT = "true or false";
	private static final String DESCRIPTION = "If set to false, none of the actions will be logged in the console. Exceptions will still be logged as usual.";
	
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
			tsSection.set(StringManager.TEAMSPEAK_CONSOLE, true);
			send(sender, Level.INFO, "&aTeamspeak actions will now be logged in the console.");
		} else if (arg.equalsIgnoreCase("false")) {
			tsSection.set(StringManager.TEAMSPEAK_CONSOLE, false);
			send(sender, Level.INFO, "&aTeamspeak actions won't be logged in the console anymore.");
		} else {
			send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
			return false;
		}
		return true;
	}
}
