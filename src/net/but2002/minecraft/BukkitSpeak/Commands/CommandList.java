package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

public class CommandList extends BukkitSpeakCommand {
	
	public CommandList(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		String online = "";
		for (TeamspeakUser user : ts.getUsers().values()) {
			if (online.length() != 0) online += ", ";
			online += user.getName();
		}
		
		String message = stringManager.getMessage("OnlineList");
		message = message.replaceAll("%list%", online);
		
		send(sender, Level.INFO, message);
	}
}
