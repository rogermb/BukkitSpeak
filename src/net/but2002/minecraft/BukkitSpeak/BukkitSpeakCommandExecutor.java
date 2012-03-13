package net.but2002.minecraft.BukkitSpeak;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSpeakCommandExecutor implements CommandExecutor {
	
	BukkitSpeak plugin;
	
	public BukkitSpeakCommandExecutor(BukkitSpeak plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			send(sender, Level.INFO, "&a[&eBukkitSpeak&a] &aHelp");
			send(sender, Level.INFO, "&e/ts list &a- Displays a list of people who are currently online");
			send(sender, Level.INFO, "&e/ts mute &a- Mutes / unmutes BukkitSpeak for you");
			send(sender, Level.INFO, "&e/ts broadcast &a- Broadcasts a global message in TeamSpeak.");
			send(sender, Level.INFO, "&e/ts reload &a- Reloads the BukkitSpeak config and restarts the TS listener");
			return true;
		}
		if (!cmd.getName().equalsIgnoreCase("ts")) {
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list")) {
			
			String online = "";
			for (TeamspeakUser user : plugin.getTs().getUsers().values()) {
				if (online.length() != 0) online += ", ";
				online += user.getName();
			}
			
			send(sender, Level.INFO, plugin.getStringManager().getMessage("msg_list") + online);
			
		} else if (args[0].equalsIgnoreCase("mute")) {
			
			if (sender instanceof Player) {
				if (plugin.getMuted((Player) sender)) {
					plugin.setMuted((Player) sender, false);
					send(sender, Level.INFO, plugin.getStringManager().getMessage("msg_unmute"));
				} else {
					plugin.setMuted((Player) sender, true);
					send(sender, Level.INFO, plugin.getStringManager().getMessage("msg_mute"));
				}
			} else {
				send(sender, Level.INFO, "Can only mute BukkitSpeak for players!");
				return true;
			}
			
		} else if (args[0].equalsIgnoreCase("reload")) {
			plugin.reload();
			send(sender, Level.INFO, "reloaded.");
		} else if (args[0].equalsIgnoreCase("broadcast")) {
			if (args.length == 1) {
				send(sender, Level.WARNING, "Too few arguments!");
				send(sender, Level.WARNING, "Usage: /ts broadcast message");
			}
			
			StringBuilder sb = new StringBuilder();
			for (String s : Arrays.copyOfRange(args, 1, args.length)) {
				sb.append(s);
				sb.append("\\s");
			}
			plugin.getTs().pushMessage("sendtextmessage targetmode=3 target=0 msg=" + sb.toString());
			
		} else {
			return false;
		}
		
		return true;
		
	}
	
	public void send(CommandSender sender, Level level, String msg) {
		if (sender instanceof Player) {
			msg = msg.replaceAll("&", "§").replaceAll("$", "§");
			sender.sendMessage(plugin + msg);
		} else {
			msg = msg.replaceAll("&[a-fA-F0-9]", "").replaceAll("$[a-fA-F0-9]", "");
			plugin.getLogger().log(level, msg);
		}
	}
		
}
