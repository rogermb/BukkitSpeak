package net.but2002.minecraft.BukkitSpeak;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.Commands.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSpeakCommandExecutor implements CommandExecutor {
	
	BukkitSpeak plugin;
	StringManager stringManager;
	TeamspeakHandler ts;
	
	BukkitSpeakCommand Help, List, Mute, Broadcast, Chat, Pm, Status;
	
	public BukkitSpeakCommandExecutor(BukkitSpeak plugin) {
		this.plugin = plugin;
		stringManager = plugin.getStringManager();
		ts = plugin.getTs();
		
		Help = new CommandHelp(plugin);
		List = new CommandList(plugin);
		Mute = new CommandMute(plugin);
		Broadcast = new CommandBroadcast(plugin);
		Chat = new CommandChat(plugin);
		Pm = new CommandPm(plugin);
		Status = new CommandStatus(plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!cmd.getName().equalsIgnoreCase("ts")) {
			return true;
		}
		if (args.length == 0) {
			Help.execute(sender, args);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list")) {
			if (!CheckPermissions(sender, "list")) return false;
			List.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("mute")) {
			if (!CheckPermissions(sender, "mute")) return false;
			Mute.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("broadcast")) {
			if (!CheckPermissions(sender, "broadcast")) return false;
			if (!stringManager.getUseTextServer()) {
				send(sender, Level.INFO, "&4You need to enable ListenToServerBroadcasts in the config to use this command.");
				return true;
			}
			Broadcast.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("chat")) {
			if (!CheckPermissions(sender, "chat")) return false;
			if (!stringManager.getUseTextChannel()) {
				send(sender, Level.INFO, "&4You need to enable ListenToChannelChat in the config to use this command.");
				return true;
			}
			Chat.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("pm")) {
			if (!CheckPermissions(sender, "pm")) return false;
			if (!stringManager.getUseTextChannel()) {
				send(sender, Level.INFO, "&4You need to enable ListenToPersonalMessages in the config to use this command.");
				return true;
			}
			Pm.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("status")) {
			if (!CheckPermissions(sender, "status")) return false;
			Status.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (!CheckPermissions(sender, "reload")) return false;
			plugin.reload(this, sender);
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
	
}
