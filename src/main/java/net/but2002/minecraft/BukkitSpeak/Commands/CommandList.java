package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.ClientList;

public class CommandList extends BukkitSpeakCommand {
	
	private static final String[] NAMES = {"list"};
	
	@Override
	public String getName() {
		return NAMES[0];
	}
	
	@Override
	public String[] getNames() {
		return NAMES;
	}
	
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
			String name, displayName;
			if (sender instanceof Player) {
				name = ((Player) sender).getName();
				displayName = ((Player) sender).getDisplayName();
			} else {
				name = convertToMinecraft(BukkitSpeak.getStringManager().getConsoleName(), false, false);
				displayName = BukkitSpeak.getStringManager().getConsoleName();
			}
			
			HashMap<String, String> repl = new HashMap<String, String>();
			repl.put("%player_name%", name);
			repl.put("%player_displayname%", displayName);
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
			String name, displayName;
			if (sender instanceof Player) {
				name = ((Player) sender).getName();
				displayName = ((Player) sender).getDisplayName();
			} else {
				name = convertToMinecraft(BukkitSpeak.getStringManager().getConsoleName(), false, false);
				displayName = BukkitSpeak.getStringManager().getConsoleName();
			}
			
			HashMap<String, String> repl = new HashMap<String, String>();
			repl.put("%player_name%", name);
			repl.put("%player_displayname%", displayName);
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
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		return null;
	}
}
