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
		if (args.length < 2 || args[1].equalsIgnoreCase("server")) {
			StringBuilder online = new StringBuilder();
			for (TeamspeakUser user : ts.getUsers().values()) {
				if (online.length() != 0) online.append(", ");
				online.append(user.getName());
			}
			
			String message = stringManager.getMessage("OnlineList");
			message = message.replaceAll("%list%", online.toString());
			
			send(sender, Level.INFO, message);
			
		} else if (args.length == 2 && stringManager.getUseChannel() && args[1].equalsIgnoreCase("channel")) {
			StringBuilder online = new StringBuilder();
			String id = String.valueOf(plugin.getStringManager().getChannelID());
			for (TeamspeakUser user : ts.getUsers().values()) {
				if (user.getValue("ctid").equals(id)) {
					if (online.length() != 0) online.append(", ");
					online.append(user.getName());
				}
			}
			
			String message = stringManager.getMessage("ChannelList");
			message = message.replaceAll("%list%", online.toString());
			
			send(sender, Level.INFO, message);
		} else {
			send(sender, Level.INFO, "&4Usage:");
			send(sender, Level.INFO, "&4/ts list (server / channel)");
		}
	}
}
