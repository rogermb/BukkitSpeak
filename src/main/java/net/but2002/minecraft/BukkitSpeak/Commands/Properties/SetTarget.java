package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetTarget extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_TARGET;
	private static final String ALLOWED_INPUT = "none, channel or server";
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
		if (arg.equalsIgnoreCase("none") || arg.equalsIgnoreCase("nothing")) {
			tsSection.set(StringManager.TEAMSPEAK_TARGET, "none");
			send(sender, Level.INFO, "&aMinecraft chat won't be sent to the TeamSpeak server anymore.");
		} else if (arg.equalsIgnoreCase("channel") || arg.equalsIgnoreCase("chat")) {
			tsSection.set(StringManager.TEAMSPEAK_TARGET, "channel");
			send(sender, Level.INFO, "&aMinecraft chat will now be sent to the TeamSpeak channel.");
		} else if (arg.equalsIgnoreCase("server") || arg.equalsIgnoreCase("broadcast")) {
			tsSection.set(StringManager.TEAMSPEAK_TARGET, "server");
			send(sender, Level.INFO, "&aMinecraft chat will now be broadcasted on the TeamSpeak server.");
		} else {
			send(sender, Level.WARNING, "Only 'none', 'channel' or 'server' are accepted.");
			return false;
		}
		return true;
	}
}
