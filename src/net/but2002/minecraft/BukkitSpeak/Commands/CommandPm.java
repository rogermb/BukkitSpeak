package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.QuerySender;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class CommandPm extends BukkitSpeakCommand {
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 3) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts pm target message");
			return;
		} else if (!BukkitSpeak.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		HashMap<String, String> client;
		try {
			client = BukkitSpeak.getClients().getByPartialName(args[1]);
		} catch (Exception e) {
			send(sender, Level.WARNING, "&4There are more than one clients matching &e" + args[1] + "&4.");
			return;
		}
		
		if (client == null) {
			send(sender, Level.WARNING, "&4Can't find the user you want to PM.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 2, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String tsMsg = BukkitSpeak.getStringManager().getMessage("PrivateMessage");
		String mcMsg = BukkitSpeak.getStringManager().getMessage("Pm");
		String Name, DisplayName;
		if (sender instanceof Player) {
			Name = ((Player) sender).getName();
			DisplayName = ((Player) sender).getDisplayName();
		} else {
			Name = convertToMinecraft(BukkitSpeak.getStringManager().getConsoleName(), false, false);
			DisplayName = BukkitSpeak.getStringManager().getConsoleName();
		}
		
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", Name);
		repl.put("%player_displayname%", DisplayName);
		repl.put("%target%", client.get("client_nickname"));
		repl.put("%msg%", sb.toString());
		
		tsMsg = convertToTeamspeak(replaceKeys(tsMsg, repl), true, BukkitSpeak.getStringManager().getAllowLinks());
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		Integer i = Integer.valueOf(client.get("clid"));
		QuerySender qs = new QuerySender(i, JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, tsMsg);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(BukkitSpeak.getInstance(), qs);
		BukkitSpeak.registerRecipient(Name, i);
		if (mcMsg == null || mcMsg.isEmpty()) return;
		if (sender instanceof Player) {
			sender.sendMessage(convertToMinecraft(mcMsg, true, BukkitSpeak.getStringManager().getAllowLinks()));
		} else {
			BukkitSpeak.log().info(convertToMinecraft(mcMsg, false, BukkitSpeak.getStringManager().getAllowLinks()));
		}
	}
}
