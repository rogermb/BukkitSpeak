package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetPrivateMessagesListener extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_PRIVATEMESSAGES;
	private static final String ALLOWED_INPUT = "true or false";
	private static final String DESCRIPTION = "If this is set to true, people can send private messages to people on the TS3 and they can text back.";
	
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
			tsSection.set(StringManager.TEAMSPEAK_PRIVATEMESSAGES, true);
			send(sender, Level.INFO, "&aPrivate messages can now be sent and received.");
		} else if (arg.equalsIgnoreCase("false")) {
			tsSection.set(StringManager.TEAMSPEAK_PRIVATEMESSAGES, false);
			send(sender, Level.INFO, "&aPrivate messages can't be sent or received anymore.");
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
