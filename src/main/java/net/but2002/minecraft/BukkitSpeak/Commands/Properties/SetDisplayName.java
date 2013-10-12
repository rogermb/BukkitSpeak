package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;

import org.bukkit.command.CommandSender;

public class SetDisplayName extends SetProperty {
	
	private static final Configuration PROPERTY = Configuration.TS_NICKNAME;
	private static final String ALLOWED_INPUT = "Any string";
	private static final String DESCRIPTION = "This name will prefix every message in TeamSpeak. "
			+ "It's the nickname of the server query.";
	
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
		if (arg.contains(" ")) {
			send(sender, Level.WARNING, "&4The display name can't contain any spaces.");
			return false;
		}
		if (BukkitSpeak.getQuery().setDisplayName(arg)) {
			PROPERTY.set(arg);
			return true;
		} else {
			send(sender, Level.WARNING, "&4The display name could not be set.");
			send(sender, Level.WARNING, "&4" + BukkitSpeak.getQuery().getLastError());
			return false;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
