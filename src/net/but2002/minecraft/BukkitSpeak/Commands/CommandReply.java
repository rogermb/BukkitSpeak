package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class CommandReply extends BukkitSpeakCommand {
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts r(eply) message");
			return;
		} else if (!BukkitSpeak.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		Integer clid;
		if (sender instanceof Player) {
			clid = BukkitSpeak.getInstance().getSender(((Player) sender).getName());
		} else {
			String n = convertToMinecraft(BukkitSpeak.getStringManager().getConsoleName(), false, false);
			clid = BukkitSpeak.getInstance().getSender(n); 
		}
		
		if (clid == null || !BukkitSpeak.getClients().containsKey(clid)) {
			send(sender, Level.WARNING, "&4Nobody has sent you a PM yet.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
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
		repl.put("%target%", BukkitSpeak.getClients().get(clid).get("client_nickname"));
		repl.put("%msg%", sb.toString());
		
		tsMsg = convertToTeamspeak(replaceKeys(tsMsg, repl), true, BukkitSpeak.getStringManager().getAllowLinks());
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		BukkitSpeak.getQuery().sendTextMessage(clid, JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, tsMsg);
		BukkitSpeak.registerRecipient(Name, clid);
		broadcastMessage(mcMsg, sender);
	}
}
