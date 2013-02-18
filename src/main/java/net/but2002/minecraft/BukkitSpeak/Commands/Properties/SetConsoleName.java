package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.List;

import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetConsoleName extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_CONSOLENAME;
	private static final String ALLOWED_INPUT = "Any string";
	private static final String DESCRIPTION = "This name will be used if a message is sent by the console.";
	
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
		getTsSection().set(StringManager.TEAMSPEAK_CONSOLENAME, arg);
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
