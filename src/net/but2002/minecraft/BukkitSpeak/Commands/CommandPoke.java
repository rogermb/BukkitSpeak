package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPoke extends BukkitSpeakCommand {
	
	public CommandPoke(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 3) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts poke target message");
			return;
		} else if (!plugin.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		HashMap<String, String> client;
		try {
			client = plugin.getClients().getByPartialName(args[1]);
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
		
		String tsMsg = stringManager.getMessage("PokeMessage");
		String mcMsg = stringManager.getMessage("Poke");
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
		repl.put("%target%", client.get("client_nickname"));
		repl.put("%msg%", sb.toString());
		
		tsMsg = convertToTeamspeak(replaceKeys(tsMsg, repl), true, stringManager.getAllowLinks());
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		if (tsMsg.length() > 100) {
			send(sender, Level.WARNING, "&4The message is too long! (> 100 characters)");
			return;
		}
		
		Integer i = Integer.valueOf(client.get("clid"));
		plugin.getQuery().pokeClient(i, tsMsg);
		if (mcMsg == null || mcMsg.isEmpty()) return;
		for (Player pl : plugin.getServer().getOnlinePlayers()) {
			if (!plugin.getMuted(pl)) pl.sendMessage(convertToMinecraft(mcMsg, true, stringManager.getAllowLinks()));
		}
		plugin.getLogger().info(convertToMinecraft(mcMsg, false, stringManager.getAllowLinks()));
	}
}
