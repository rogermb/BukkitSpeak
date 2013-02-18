package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetChannelPassword extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_CHANNELPW;
	private static final String ALLOWED_INPUT = "Any string";
	private static final String DESCRIPTION = "BukkitSpeak will use this password to enter the selected channel. "
			+ "'' means no password.";
	
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
		if (!(BukkitSpeak.getStringManager().getUseChannel()) && !(BukkitSpeak.getStringManager().getUseTextChannel())) {
			send(sender, Level.WARNING, "&4Set " + StringManager.TEAMSPEAK_CHANNEL 
					+ " or " + StringManager.TEAMSPEAK_TEXTCHANNEL + " to true to use this feature.");
			return false;
		}
		String s = arg;
		if (arg.equals("\'\'")) s = "";
		getTsSection().set(StringManager.TEAMSPEAK_CHANNELPW, s);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
