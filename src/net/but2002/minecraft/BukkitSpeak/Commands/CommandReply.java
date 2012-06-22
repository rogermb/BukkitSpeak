package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class CommandReply extends BukkitSpeakCommand {
	
	public CommandReply(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts r(eply) message");
			return;
		} else if (!plugin.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		Integer clid;
		if (sender instanceof Player) {
			clid = plugin.getSender(((Player) sender).getName());
		} else {
			clid = plugin.getSender(convertToMinecraft(stringManager.getConsoleName(), false, false)); 
		}
		
		if (clid == null || !plugin.getClients().containsKey(clid)) {
			send(sender, Level.WARNING, "&4Nobody has sent you a PM yet.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String tsMsg = stringManager.getMessage("PrivateMessage");
		String mcMsg = stringManager.getMessage("Pm");
		String Name, DisplayName;
		if (sender instanceof Player) {
			Name = ((Player) sender).getName();
			DisplayName = ((Player) sender).getDisplayName();
		} else {
			Name = convertToMinecraft(stringManager.getConsoleName(), false, false);
			DisplayName = stringManager.getConsoleName();
		}
		
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", Name);
		repl.put("%player_displayname%", DisplayName);
		repl.put("%target%", plugin.getClients().get(clid).get("client_nickname"));
		repl.put("%msg%", sb.toString());
		
		tsMsg = convertToTeamspeak(replaceKeys(tsMsg, repl), true, stringManager.getAllowLinks());
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		plugin.getQuery().sendTextMessage(clid, JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, tsMsg);
		plugin.registerRecipient(Name, clid);
		if (mcMsg == null || mcMsg.isEmpty()) return;
		if (sender instanceof Player) {
			sender.sendMessage(convertToMinecraft(mcMsg, true, stringManager.getAllowLinks()));
		} else {
			plugin.getLogger().info(convertToMinecraft(mcMsg, false, stringManager.getAllowLinks()));
		}
	}
}
