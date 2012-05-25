package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.ClientList;

public class CommandList extends BukkitSpeakCommand {
	
	String id;
	
	public CommandList(BukkitSpeak plugin) {
		super(plugin);
		id = String.valueOf(stringManager.getChannelID());
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!plugin.getQuery().isConnected() || plugin.getClients() == null) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		if (args.length < 2 || args[1].equalsIgnoreCase("server")) {
			ClientList clients = plugin.getClients();
			StringBuilder online = new StringBuilder();
			
			for (HashMap<String, String> user : clients.values()) {
				if (user.get("client_type").equals("0")) {
					if (online.length() != 0) online.append(", ");
					online.append(user.get("client_nickname"));
				}
			}
			
			String message = stringManager.getMessage("OnlineList");
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
			if (online.length() == 0) {
				repl.put("%list%", "-");
			} else {
				repl.put("%list%", online.toString());
			}
			
			message = replaceKeys(message, repl);
			
			send(sender, Level.INFO, message);
			
		} else if (args.length == 2 && stringManager.getUseChannel() && args[1].equalsIgnoreCase("channel")) {
			ClientList clients = plugin.getClients();
			StringBuilder online = new StringBuilder();
			
			for (HashMap<String, String> user : clients.values()) {
				if (user.get("client_type").equals("0") && user.get("cid").equals(id)) {
					if (online.length() != 0) online.append(", ");
					online.append(user.get("client_nickname"));
				}
			}
			
			String message = stringManager.getMessage("ChannelList");
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
			if (online.length() == 0) {
				repl.put("%list%", "-");
			} else {
				repl.put("%list%", online.toString());
			}
			
			message = replaceKeys(message, repl);
			
			send(sender, Level.INFO, message);
		} else {
			send(sender, Level.INFO, "&4Usage:");
			send(sender, Level.INFO, "&4/ts list (server / channel)");
		}
	}
}
