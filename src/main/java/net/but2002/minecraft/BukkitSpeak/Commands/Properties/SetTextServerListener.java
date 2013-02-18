package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetTextServerListener extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_TEXTSERVER;
	private static final String ALLOWED_INPUT = "true or false";
	private static final String DESCRIPTION = "If this is set to true, BukkitSpeak will notice if somebody writes a global message on TeamSpeak.";
	
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
			tsSection.set(StringManager.TEAMSPEAK_TEXTSERVER, true);
			send(sender, Level.INFO, "&aTeamSpeak broadcasts will now be sent to Minecraft.");
		} else if (arg.equalsIgnoreCase("false")) {
			tsSection.set(StringManager.TEAMSPEAK_TEXTSERVER, false);
			send(sender, Level.INFO, "&aTeamSpeak broadcasts won't be sent to Minecraft anymore.");
		} else {
			send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
			return false;
		}
		BukkitSpeak.getInstance().saveConfig();
		BukkitSpeak.getInstance().reloadStringManager();
		reloadListener();
		return false;
	}
}
