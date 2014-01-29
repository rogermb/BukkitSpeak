package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

public class CommandList extends BukkitSpeakCommand {

	private static final String[] VALUES = {"server", "channel"};

	public CommandList() {
		super("list");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!isConnected(sender)) return;

		if (args.length < 2 || args[1].equalsIgnoreCase("server")) {
			StringBuilder online = new StringBuilder();

			for (HashMap<String, String> user : BukkitSpeak.getClientList().getClients().values()) {
				if (user.get("client_type").equals("0")) {
					if (online.length() != 0) online.append(", ");
					online.append(user.get("client_nickname"));
				}
			}

			String mcMsg = Messages.MC_COMMAND_LIST_SERVER.get();
			String list = "-";
			if (online.length() > 0) {
				list = online.toString();
			}

			mcMsg = new Replacer().addSender(sender).addList(list).replace(mcMsg);

			if (mcMsg == null || mcMsg.isEmpty()) return;
			send(sender, Level.INFO, mcMsg);
		} else if (args.length == 2 && Configuration.TS_ENABLE_CHANNEL_EVENTS.getBoolean()
				&& args[1].equalsIgnoreCase("channel")) {
			StringBuilder online = new StringBuilder();
			String id = String.valueOf(BukkitSpeak.getQuery().getCurrentQueryClientChannelID());

			for (HashMap<String, String> user : BukkitSpeak.getClientList().getClients().values()) {
				if (user.get("client_type").equals("0") && user.get("cid").equals(id)) {
					if (online.length() != 0) online.append(", ");
					online.append(user.get("client_nickname"));
				}
			}

			String mcMsg = Messages.MC_COMMAND_LIST_CHANNEL.get();
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
