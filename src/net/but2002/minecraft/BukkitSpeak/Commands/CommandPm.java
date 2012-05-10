package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class CommandPm extends BukkitSpeakCommand {
	
	public CommandPm(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 3) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts pm target message");
			return;
		} else if (!plugin.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		} 
		
		Vector<HashMap<String, String>> users = plugin.getQuery().getList(JTS3ServerQuery.LISTMODE_CLIENTLIST);
		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for (HashMap<String, String> user : users) {
			if (user.get("client_nickname").startsWith(args[1]) && user.get("client_type").equals("0")) {
				result.add(user);
			}
		}
		
		if (result.size() == 0) {
			send(sender, Level.WARNING, "&4Can't find the user you want to PM.");
			return;
		} else if (result.size() > 1) {
			send(sender, Level.WARNING, "&4There are more than one clients matching &e" + args[1] + "&4.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 2, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		
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
		repl.put("%target%", result.get(0).get("client_nickname"));
		repl.put("%msg%", sb.toString());
		
		tsMsg = convertToTeamspeak(replaceKeys(tsMsg, repl), true, stringManager.getAllowLinks());
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg.isEmpty()) return;
		Integer i = Integer.valueOf(result.get(0).get("clid"));
		plugin.getQuery().sendTextMessage(i, JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, tsMsg);
		plugin.registerRecipient(Name, i);
		if (mcMsg.isEmpty()) return;
		if (sender instanceof Player) {
			sender.sendMessage(convertToMinecraft(mcMsg, true, stringManager.getAllowLinks()));
		} else {
			plugin.getLogger().info(convertToMinecraft(mcMsg, false, stringManager.getAllowLinks()));
		}
	}
}
