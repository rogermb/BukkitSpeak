package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.ClientList;

public class CommandList extends BukkitSpeakCommand {
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!BukkitSpeak.getQuery().isConnected() || BukkitSpeak.getClients() == null) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		if (args.length < 2 || args[1].equalsIgnoreCase("server")) {
			ClientList clients = BukkitSpeak.getClients();
			StringBuilder online = new StringBuilder();
			
			for (HashMap<String, String> user : clients.values()) {
				if (user.get("client_type").equals("0")) {
					if (online.length() != 0) online.append(", ");
					online.append(user.get("client_nickname"));
				}
			}
			
			String message = BukkitSpeak.getStringManager().getMessage("OnlineList");
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
			if (online.length() == 0) {
				repl.put("%list%", "-");
			} else {
				repl.put("%list%", online.toString());
			}
			
			message = replaceKeys(message, repl);
			
			if (message == null || message.isEmpty()) return;
			send(sender, Level.INFO, message);
			
		} else if (args.length == 2 && BukkitSpeak.getStringManager().getUseChannel() && args[1].equalsIgnoreCase("channel")) {
			ClientList clients = BukkitSpeak.getClients();
			StringBuilder online = new StringBuilder();
			String id = String.valueOf(BukkitSpeak.getStringManager().getChannelID());
			
			for (HashMap<String, String> user : clients.values()) {
				if (user.get("client_type").equals("0") && user.get("cid").equals(id)) {
					if (online.length() != 0) online.append(", ");
					online.append(user.get("client_nickname"));
				}
			}
			
			String message = BukkitSpeak.getStringManager().getMessage("ChannelList");
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
			if (online.length() == 0) {
				repl.put("%list%", "-");
			} else {
				repl.put("%list%", online.toString());
			}
			
			message = replaceKeys(message, repl);
			
			if (message == null || message.isEmpty()) return;
			send(sender, Level.INFO, message);
		} else {
			send(sender, Level.INFO, "&4Usage:");
			send(sender, Level.INFO, "&4/ts list (server / channel)");
		}
	}
}
