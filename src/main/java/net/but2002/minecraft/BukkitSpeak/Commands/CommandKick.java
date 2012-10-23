package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QueryKick;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKick extends BukkitSpeakCommand {
	
	private static final String[] NAMES = {"kick"};
	
	@Override
	public String getName() {
		return NAMES[0];
	}
	
	@Override
	public String[] getNames() {
		return NAMES;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts kick client (message)");
			return;
		} else if (!BukkitSpeak.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		HashMap<String, String> client;
		try {
			client = BukkitSpeak.getClients().getByPartialName(args[1]);
		} catch (IllegalArgumentException e) {
			send(sender, Level.WARNING, "&4There are more than one clients matching &e" + args[1] + "&4.");
			return;
		}
		
		if (client == null) {
			send(sender, Level.WARNING, "&4Can't find the user you want to kick from the server.");
			return;
		}
		
		String tsMsg = BukkitSpeak.getStringManager().getMessage("KickMessage");
		String mcMsg = BukkitSpeak.getStringManager().getMessage("Kick");
		String name, displayName;
		if (sender instanceof Player) {
			name = ((Player) sender).getName();
			displayName = ((Player) sender).getDisplayName();
		} else {
			name = convertToMinecraft(BukkitSpeak.getStringManager().getConsoleName(), false, false);
			displayName = BukkitSpeak.getStringManager().getConsoleName();
		}
		
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", name);
		repl.put("%player_displayname%", displayName);
		repl.put("%target%", client.get("client_nickname"));
		if (args.length > 2) {
			repl.put("%msg%", combineSplit(2, args, " "));
		} else {
			repl.put("%msg%", BukkitSpeak.getStringManager().getDefaultReason());
		}
		
		tsMsg = convertToTeamspeak(replaceKeys(tsMsg, repl), false, BukkitSpeak.getStringManager().getAllowLinks());
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		if (tsMsg.length() > TS_MAXLENGHT) {
			send(sender, Level.WARNING, "&4The message is too long! (> 100 characters)");
			return;
		}
		
		Integer i = Integer.valueOf(client.get("clid"));
		QueryKick qk = new QueryKick(i, false, tsMsg);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(BukkitSpeak.getInstance(), qk);
		broadcastMessage(mcMsg, sender);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
