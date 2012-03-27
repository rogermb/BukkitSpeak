package net.but2002.minecraft.BukkitSpeak;

import java.util.Arrays;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.util.DateManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSpeakCommandExecutor implements CommandExecutor {
	
	BukkitSpeak plugin;
	StringManager stringManager;
	TeamspeakHandler ts;
	
	public BukkitSpeakCommandExecutor(BukkitSpeak plugin) {
		this.plugin = plugin;
		stringManager = plugin.getStringManager();
		ts = plugin.getTs();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			send(sender, Level.INFO, "&aHelp");
			if (CheckPermissions(sender, "list")) send(sender, Level.INFO, "&e/ts list &a- Displays a list of people who are currently online");
			if (CheckPermissions(sender, "mute")) send(sender, Level.INFO, "&e/ts mute &a- Mutes / unmutes BukkitSpeak for you");
			if (CheckPermissions(sender, "chat") && stringManager.getUseTextChannel()) send(sender, Level.INFO, "&e/ts chat &a- Displays a message only in the TS channel.");
			if (CheckPermissions(sender, "broadcast")) send(sender, Level.INFO, "&e/ts broadcast &a- Broadcasts a global message in TeamSpeak.");
			if (CheckPermissions(sender, "status")) send(sender, Level.INFO, "&e/ts status &a- Shows you some info about BukkitSpeak.");
			if (CheckPermissions(sender, "reload")) send(sender, Level.INFO, "&e/ts reload &a- Reloads the BukkitSpeak config and restarts the TS listener");
			return true;
		}
		if (!cmd.getName().equalsIgnoreCase("ts")) {
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list")) {
			if (!CheckPermissions(sender, "list")) return false;
			List(sender, args);
		} else if (args[0].equalsIgnoreCase("mute")) {
			if (!CheckPermissions(sender, "mute")) return false;
			Mute(sender, args);
		} else if (args[0].equalsIgnoreCase("chat")) {
			if (!stringManager.getUseTextChannel()) {
				send(sender, Level.INFO, "You need to enable ListenToServerBroadcasts in the config to use this command.");
				return true;
			}
			if (!CheckPermissions(sender, "chat")) return false;
			Chat(sender, args);
		} else if (args[0].equalsIgnoreCase("broadcast")) {
			if (!stringManager.getUseTextServer()) {
				send(sender, Level.INFO, "You need to enable ListenToChannelChat in the config to use this command.");
				return true;
			}
			if (!CheckPermissions(sender, "broadcast")) return false;
			Broadcast(sender, args);
		} else if (args[0].equalsIgnoreCase("status")) {
			if (!CheckPermissions(sender, "status")) return false;
			Status(sender, args);
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (!CheckPermissions(sender, "reload")) return false;
			Reload(sender, args);
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
	
	public Boolean CheckPermissions(CommandSender sender, String perm) {
		if (sender instanceof Player) {
			return sender.hasPermission("bukkitspeak.commands." + perm); 
		} else {
			return true;
		}
	}
	
	public void List(CommandSender sender, String[] args) {
		String online = "";
		for (TeamspeakUser user : ts.getUsers().values()) {
			if (online.length() != 0) online += ", ";
			online += user.getName();
		}
		
		String message = stringManager.getMessage("OnlineList");
		message = message.replaceAll("%list%", online);
		
		send(sender, Level.INFO, message);
	}
	
	public void Mute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (plugin.getMuted((Player) sender)) {
				plugin.setMuted((Player) sender, false);
				send(sender, Level.INFO, stringManager.getMessage("Unmute"));
			} else {
				plugin.setMuted((Player) sender, true);
				send(sender, Level.INFO, stringManager.getMessage("Mute"));
			}
		} else {
			send(sender, Level.INFO, "Can only mute BukkitSpeak for players!");
		}
	}
	
	public void Reload(CommandSender sender, String[] args) {
		plugin.reload(this, sender);
	}
	
	public void Status(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&eBukkitSpeak Version: &av" + plugin.getDescription().getVersion());
		if (ts.getAlive()) {
			send(sender, Level.INFO, "&eTeamspeak Listener: &arunning");
			if (ts.getStarted() != null) send(sender, Level.INFO, "&eRunning since: &a" + DateManager.DateToString(ts.getStarted()));
		} else {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &4dead");
			send(sender, Level.WARNING, "&eRunning since: &4" + DateManager.DateToString(ts.getStarted()));
			send(sender, Level.WARNING, "&eStopped since: &4" + DateManager.DateToString(ts.getStopped()));
			send(sender, Level.WARNING, "&eUse &a/ts reload &eto restart the listener!");
		}
	}
	
	public void Broadcast(CommandSender sender, String[] args) {
		if (args.length == 1) {
			send(sender, Level.WARNING, "Too few arguments!");
			send(sender, Level.WARNING, "Usage: /ts broadcast message");
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
			sb.append(s);
			sb.append("\\s");
		}
		ts.pushMessage("sendtextmessage targetmode=3 target=0 msg=" + sb.toString(), stringManager.getTeamspeakNickname());
	}
	
	public void Chat(CommandSender sender, String[] args) {
		if (args.length == 1) {
			send(sender, Level.WARNING, "Too few arguments!");
			send(sender, Level.WARNING, "Usage: /ts chat message");
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
			sb.append(s);
			sb.append("\\s");
		}
		String SenderName;
		if (sender instanceof Player) {
			SenderName = sender.getName();
		} else {
			SenderName = stringManager.getTeamspeakNickname();
		}
		ts.pushMessage("sendtextmessage targetmode=2 target=" + stringManager.getChannelID() + " msg=" + sb.toString(), SenderName);
	}
}
