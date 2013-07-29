package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.ClientList;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

public class CommandList extends BukkitSpeakCommand {
	
	private static final String[] VALUES = {"server", "channel"};
	
	public CommandList() {
		super("list");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!BukkitSpeak.getQuery().isConnected() || BukkitSpeak.getClientList() == null) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		if (args.length < 2 || args[1].equalsIgnoreCase("server")) {
			ClientList clientList = BukkitSpeak.getClientList();
			StringBuilder online = new StringBuilder();
			
			for (HashMap<String, String> user : clientList.getClients().values()) {
				if (user.get("client_type").equals("0")) {
					if (online.length() != 0) online.append(", ");
					online.append(user.get("client_nickname"));
				}
			}
			
			String mcMsg = BukkitSpeak.getStringManager().getMessage("OnlineList");
			String list = "-";
			if (online.length() > 0) {
				list = online.toString();
			}
			
			mcMsg = new Replacer().addSender(sender).addList(list).replace(mcMsg);
			
			if (mcMsg == null || mcMsg.isEmpty()) return;
			send(sender, Level.INFO, mcMsg);
		} else if (args.length == 2 && BukkitSpeak.getStringManager().getUseChannel() && args[1].equalsIgnoreCase("channel")) {
			ClientList clientList = BukkitSpeak.getClientList();
			StringBuilder online = new StringBuilder();
			String id = String.valueOf(BukkitSpeak.getStringManager().getChannelID());
			
			for (HashMap<String, String> user : clientList.getClients().values()) {
				if (user.get("client_type").equals("0") && user.get("cid").equals(id)) {
					if (online.length() != 0) online.append(", ");
					online.append(user.get("client_nickname"));
				}
			}
			
			String mcMsg = BukkitSpeak.getStringManager().getMessage("ChannelList");
			String list = "-";
			if (online.length() > 0) {
				list = online.toString();
			}
			
			mcMsg = new Replacer().addSender(sender).addList(list).replace(mcMsg);
			
			if (mcMsg == null || mcMsg.isEmpty()) return;
			send(sender, Level.INFO, mcMsg);
		} else {
			send(sender, Level.INFO, "&4Usage:");
			send(sender, Level.INFO, "&4/ts list (server / channel)");
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length != 2) return null;
		List<String> al = new ArrayList<String>();
		for (String n : VALUES) {
			if (n.startsWith(args[1])) al.add(n);
		}
		return al;
	}
}
