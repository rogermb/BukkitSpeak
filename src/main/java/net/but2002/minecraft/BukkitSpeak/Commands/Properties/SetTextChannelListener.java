package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;

import org.bukkit.command.CommandSender;

public class SetTextChannelListener extends SetProperty {
	
	private static final Configuration PROPERTY = Configuration.TS_ENABLE_CHANNEL_MESSAGES;
	private static final String ALLOWED_INPUT = "true or false";
	private static final String DESCRIPTION = "If this is set to true, chat in the TeamSpeak channel "
			+ "will be sent to the Minecraft server.";
	private static final String[] TAB_SUGGESTIONS = {"true", "false"};
	
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
		boolean o = (Configuration.TS_ENABLE_CHANNEL_EVENTS.getBoolean())
				|| (Configuration.TS_ENABLE_CHANNEL_MESSAGES.getBoolean());
		boolean n = false;
		if (arg.equalsIgnoreCase("true")) {
			PROPERTY.set(true);
			n = true;
			send(sender, Level.INFO, "&aChat messages from the channel will now be broadcasted in Minecraft.");
		} else if (arg.equalsIgnoreCase("false")) {
			PROPERTY.set(false);
			send(sender, Level.INFO, "&aChat messages from the channel won't be broadcasted in Minecraft anymore.");
		} else {
			send(sender, Level.WARNING, "&4Only 'true' or 'false' are accepted.");
			return false;
		}
		Configuration.save();
		reloadListener();
		if (!o && n) connectChannel(sender);
		return false;
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
