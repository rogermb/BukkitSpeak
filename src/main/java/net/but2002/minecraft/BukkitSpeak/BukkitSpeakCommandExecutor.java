package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.List;

import net.but2002.minecraft.BukkitSpeak.Commands.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class BukkitSpeakCommandExecutor implements CommandExecutor, TabCompleter {
	
	private List<BukkitSpeakCommand> userCommands;
	private List<BukkitSpeakCommand> adminCommands;
	
	public BukkitSpeakCommandExecutor() {
		userCommands = new ArrayList<BukkitSpeakCommand>();
		userCommands.add(new CommandHelp());
		userCommands.add(new CommandInfo());
		userCommands.add(new CommandList());
		userCommands.add(new CommandMute());
		userCommands.add(new CommandBroadcast());
		userCommands.add(new CommandChat());
		userCommands.add(new CommandPm());
		userCommands.add(new CommandPoke());
		userCommands.add(new CommandReply());
		
		adminCommands = new ArrayList<BukkitSpeakCommand>();
		adminCommands.add(new CommandAdminHelp());
		adminCommands.add(new CommandBan());
		adminCommands.add(new CommandChannelKick());
		adminCommands.add(new CommandKick());
		adminCommands.add(new CommandReload());
		adminCommands.add(new CommandSet());
		adminCommands.add(new CommandStatus());
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
		String m = msg;
		if (sender instanceof Player) {
			m = m.replaceAll("&", "\u00A7").replaceAll("$", "\u00A7");
			sender.sendMessage(BukkitSpeak.getFullName() + m);
		} else {
			m = m.replaceAll("&[a-fA-F0-9]", "").replaceAll("$[a-fA-F0-9]", "");
			BukkitSpeak.log().log(level, m);
		}
	}
	
	public Boolean checkPermissions(CommandSender sender, String perm) {
		if (sender instanceof Player) {
			return sender.hasPermission("bukkitspeak.commands." + perm); 
		} else {
			return true;
		}
	}
	
	public boolean onTeamspeakCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		String s = "help";
		if (args.length > 0) {
			s = args[0];
		}
		
		for (BukkitSpeakCommand bsc : userCommands) {
			for (String name : bsc.getNames()) {
				if (name.equalsIgnoreCase(s)) {
					if (!checkPermissions(sender, bsc.getName())) return false;
					bsc.execute(sender, args);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean onTeamspeakAdminCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String s = "adminhelp";
		if (args.length > 0) {
			s = args[0];
		}
		
		for (BukkitSpeakCommand bsc : adminCommands) {
			for (String name : bsc.getNames()) {
				if (name.equalsIgnoreCase(s)) {
					if (!checkPermissions(sender, bsc.getName())) return false;
					bsc.execute(sender, args);
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		
		switch (args.length) {
		case 0:
			return null;
		case 1:
			if (cmd.getName().equals("ts")) {
				List<String> al = new ArrayList<String>();
				for (BukkitSpeakCommand uc : userCommands) {
					if (uc.getName().startsWith(args[0])) {
						if (checkPermissions(sender, uc.getName())) al.add(uc.getName());
					}
				}
				return al;
			} else if (cmd.getName().equals("tsa")) {
				List<String> al = new ArrayList<String>();
				for (BukkitSpeakCommand ac : adminCommands) {
					if (ac.getName().startsWith(args[0])) {
						if (checkPermissions(sender, ac.getName())) al.add(ac.getName());
					}
				}
				return al;
			} else {
				return null;
			}
		default:
			if (cmd.getName().equals("ts")) {
				for (BukkitSpeakCommand bsc : userCommands) {
					for (String name : bsc.getNames()) {
						if (name.equalsIgnoreCase(args[0])) {
							if (!checkPermissions(sender, bsc.getName())) return null;
							return bsc.onTabComplete(sender, args);
						}
					}
				}
				return null;
			} else if (cmd.getName().equals("tsa")) {
				for (BukkitSpeakCommand bsc : adminCommands) {
					for (String name : bsc.getNames()) {
						if (name.equalsIgnoreCase(args[0])) {
							if (!checkPermissions(sender, bsc.getName())) return null;
							return bsc.onTabComplete(sender, args);
						}
					}
				}
				return null;
			} else {
				return null;
			}
		}
	}
}
