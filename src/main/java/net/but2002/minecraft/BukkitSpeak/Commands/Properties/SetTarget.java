package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.TsTarget;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;

import org.bukkit.command.CommandSender;

public class SetTarget extends SetProperty {

	private static final Configuration PROPERTY = Configuration.TS_MESSAGES_TARGET;
	private static final String ALLOWED_INPUT = "none, channel or server";
	private static final String DESCRIPTION = "If set to channel, Minecraft chat will be sent to the channel, "
			+ "if set to server the messages will be broadcasted.";
	private static final String[] TAB_SUGGESTIONS = {"none", "channel", "server"};

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
		TsTarget tsTarget = TsTarget.getFromString(arg);
		if (tsTarget == null) {
			send(sender, Level.WARNING, "&4Only 'none', 'channel' or 'server' are accepted.");
			return false;
		}
		PROPERTY.set(tsTarget.name().toLowerCase());
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length != 3) return null;
		List<String> al = new ArrayList<String>();
		for (String s : TAB_SUGGESTIONS) {
			if (s.startsWith(args[2].toLowerCase())) {
				al.add(s);
			}
		}
		return al;
	}
}
