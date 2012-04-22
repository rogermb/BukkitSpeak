package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		} else if (!ts.getAlive()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		} 
		
		TeamspeakUser user = ts.getUserByPartialName(args[1]);
		if (user == null) {
			send(sender, Level.WARNING, "&4Can't find the user you want to PM.");
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
		repl.put("%target%", user.getName());
		repl.put("%msg%", sb.toString());
		
		tsMsg = replaceKeys(tsMsg, repl);
		mcMsg = replaceKeys(mcMsg, repl);
		
		if (tsMsg.isEmpty()) return;
		ts.SendTextMessage(1, user.getID(), convertToTeamspeak(tsMsg, true, stringManager.getAllowLinks()));
		plugin.registerRecipient(Name, user);
		if (mcMsg.isEmpty()) return;
		if (sender instanceof Player) {
			sender.sendMessage(convertToMinecraft(mcMsg, true, stringManager.getAllowLinks()));
		} else {
			plugin.getLogger().info(convertToMinecraft(mcMsg, false, stringManager.getAllowLinks()));
		}
	}
}
