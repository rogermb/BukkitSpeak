package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class CommandBroadcast extends BukkitSpeakCommand {
	
	public CommandBroadcast(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts broadcast message");
			return;
		} else if (!plugin.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String tsMsg = stringManager.getMessage("ServerMessage");
		String mcMsg = stringManager.getMessage("Broadcast");
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
		repl.put("%msg%", sb.toString());
		
		tsMsg = convertToTeamspeak(replaceKeys(tsMsg, repl), true, stringManager.getAllowLinks());
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		plugin.getQuery().sendTextMessage(plugin.getQuery().getCurrentQueryClientServerID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
		if (tsMsg == null || mcMsg.isEmpty()) return;
		for (Player pl : plugin.getServer().getOnlinePlayers()) {
			if (!plugin.getMuted(pl)) pl.sendMessage(convertToMinecraft(mcMsg, true, stringManager.getAllowLinks()));
		}
		if (!(sender instanceof Player) || (stringManager.getLogInConsole())) {
			plugin.getLogger().info(convertToMinecraft(mcMsg, false, stringManager.getAllowLinks()));
		}
	}
}
