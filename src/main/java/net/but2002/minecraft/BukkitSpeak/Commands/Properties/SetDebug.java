package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SetDebug extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_DEBUG;
	private static final String ALLOWED_INPUT = "true or false";
	private static final String DESCRIPTION = "True sets the plugin to debug mode.";
	private static final String[] TAB_SUGGESTIONS = {"true", "false"};
	
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
			getTsSection().set(StringManager.TEAMSPEAK_DEBUG, true);
			send(sender, Level.INFO, "&aDebug mode was successfully enabled.");
		} else if (arg.equalsIgnoreCase("false")) {
			getTsSection().set(StringManager.TEAMSPEAK_DEBUG, false);
			send(sender, Level.INFO, "&aDebug mode was successfully disabled.");
		} else {
			send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
			return false;
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length != 3) return null;
		List<String> al = new ArrayList<String>();
		for (String s : TAB_SUGGESTIONS) {
			if (s.startsWith(args[2])) {
				al.add(s);
			}
		}
		return al;
	}
}
