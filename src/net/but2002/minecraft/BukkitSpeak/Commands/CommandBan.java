package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBan extends BukkitSpeakCommand {
	
	public CommandBan() {
		super();
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts ban client (message)");
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
			send(sender, Level.WARNING, "&4Can't find the user you want to ban from the server.");
			return;
		}
		
		String tsMsg = BukkitSpeak.getStringManager().getMessage("BanMessage");
		String mcMsg = BukkitSpeak.getStringManager().getMessage("Ban");
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
		if (args.length > 2) {
			repl.put("%msg%", combineSplit(2, args, " "));
		} else {
			repl.put("%msg%", BukkitSpeak.getStringManager().getDefaultReason());
		}
		
		tsMsg = convertToTeamspeak(replaceKeys(tsMsg, repl), false, BukkitSpeak.getStringManager().getAllowLinks());
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		if (tsMsg.length() > 100) {
			send(sender, Level.WARNING, "&4The message is too long! (> 100 characters)");
			return;
		}
		
		//FIXME
		
		Integer i = Integer.valueOf(client.get("clid"));
		BukkitSpeak.getDQuery().banClient(i, tsMsg);
		broadcastMessage(mcMsg, sender);
	}
}
