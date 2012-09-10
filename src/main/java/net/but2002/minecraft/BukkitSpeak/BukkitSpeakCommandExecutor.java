package net.but2002.minecraft.BukkitSpeak;

import java.util.Arrays;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.Commands.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitSpeakCommandExecutor implements CommandExecutor {
	
	private BukkitSpeakCommand Help, Info, List, Mute, Broadcast, Chat, Pm, Poke, Reply;
	private BukkitSpeakCommand AdminHelp, Ban, ChannelKick, Kick, Set, Status;
	
	public BukkitSpeakCommandExecutor() {
		AdminHelp = new CommandAdminHelp();
		Ban = new CommandBan();
		ChannelKick = new CommandChannelKick();
		Help = new CommandHelp();
		Info = new CommandInfo();
		Kick = new CommandKick();
		List = new CommandList();
		Mute = new CommandMute();
		Broadcast = new CommandBroadcast();
		Chat = new CommandChat();
		Pm = new CommandPm();
		Poke = new CommandPoke();
		Reply = new CommandReply();
		Set = new CommandSet();
		Status = new CommandStatus();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (cmd.getName().equals("ts")) {
			if (args.length >= 1 && args[0].equals("admin")) {
				return onTeamspeakAdminCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
			} else {
				return onTeamspeakCommand(sender, cmd, label, args);
			}
		} else if (cmd.getName().equals("tsa")) {
			return onTeamspeakAdminCommand(sender, cmd, label, args);
		} else {
			return true;
		}
	}
	
	public void send(CommandSender sender, Level level, String msg) {
		if (sender instanceof Player) {
			msg = msg.replaceAll("&", "\u00A7").replaceAll("$", "\u00A7");
			sender.sendMessage(BukkitSpeak.getFullName() + msg);
		} else {
			msg = msg.replaceAll("&[a-fA-F0-9]", "").replaceAll("$[a-fA-F0-9]", "");
			BukkitSpeak.log().log(level, msg);
		}
	}
	
	public Boolean CheckPermissions(CommandSender sender, String perm) {
		if (sender instanceof Player) {
			return sender.hasPermission("bukkitspeak.commands." + perm); 
		} else {
			return true;
		}
	}
	
	public boolean onTeamspeakCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
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
			if (!BukkitSpeak.getStringManager().getUseTextServer()) {
				send(sender, Level.INFO, "&4You need to enable ListenToServerBroadcasts in the config to use this command.");
				return true;
			}
			Broadcast.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("chat")) {
			if (!CheckPermissions(sender, "chat")) return false;
			if (!BukkitSpeak.getStringManager().getUseTextChannel()) {
				send(sender, Level.INFO, "&4You need to enable ListenToChannelChat in the config to use this command.");
				return true;
			}
			Chat.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("pm")) {
			if (!CheckPermissions(sender, "pm")) return false;
			if (!BukkitSpeak.getStringManager().getUsePrivateMessages()) {
				send(sender, Level.INFO, "&4You need to enable ListenToPrivateMessages in the config to use this command.");
				return true;
			}
			Pm.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("poke")) {
			if (!CheckPermissions(sender, "poke")) return false;
			Poke.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("info")) {
			if (!CheckPermissions(sender, "info")) return false;
			Info.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("reply") || args[0].equalsIgnoreCase("r")) {
			if (!CheckPermissions(sender, "reply")) return false;
			Reply.execute(sender, args);
		} else {
			return false;
		}
		
		return true;
	}
	
	public boolean onTeamspeakAdminCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			AdminHelp.execute(sender, args);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("ban")) {
			if (!CheckPermissions(sender, "ban")) return false;
			Ban.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("channelkick")) {
			if (!CheckPermissions(sender, "channelkick")) return false;
			ChannelKick.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("kick")) {
			if (!CheckPermissions(sender, "kick")) return false;
			Kick.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("set")) {
			if (!CheckPermissions(sender, "set")) return false;
			Set.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("status")) {
			if (!CheckPermissions(sender, "status")) return false;
			Status.execute(sender, args);
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (!CheckPermissions(sender, "reload")) return false;
			BukkitSpeak.getInstance().reload(sender);
		} else {
			return false;
		}
		
		return true;
	}
}
