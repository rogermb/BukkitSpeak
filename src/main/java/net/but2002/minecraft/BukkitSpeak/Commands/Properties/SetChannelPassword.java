package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;

import org.bukkit.command.CommandSender;

public class SetChannelPassword extends SetProperty {

	private static final Configuration PROPERTY = Configuration.TS_CHANNEL_PASSWORD;
	private static final String ALLOWED_INPUT = "Any string";
	private static final String DESCRIPTION = "BukkitSpeak will use this password to enter the selected channel. "
			+ "'' means no password.";

	@Override
	public Configuration getProperty() {
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
		if (!(Configuration.TS_ENABLE_CHANNEL_EVENTS.getBoolean())
				&& !(Configuration.TS_ENABLE_CHANNEL_MESSAGES.getBoolean())) {
			send(sender, Level.WARNING, "&4Set " + Configuration.TS_ENABLE_CHANNEL_EVENTS.getConfigPath() + " or "
					+ Configuration.TS_ENABLE_CHANNEL_MESSAGES.getConfigPath() + " to true to use this feature.");
			return false;
		}
		String s = arg;
		if (arg.equals("\'\'")) s = "";
		PROPERTY.set(s);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
