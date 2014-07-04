package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.PermissionsHelper;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.ServerGroup;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
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
			Collection<HashMap<String, String>> clientCollection = BukkitSpeak.getClientList().getClients().values();
			List<HashMap<String, String>> clients = new ArrayList<HashMap<String, String>>(clientCollection);
			Iterator<HashMap<String, String>> iterator = clients.iterator();
			while (iterator.hasNext()) {
				HashMap<String, String> user = iterator.next();
				if (!user.get("client_type").equals("0")) {
					iterator.remove();
				}
			}

			String mcMsg = Messages.MC_COMMAND_LIST_SERVER.get();
			String color = MessageUtil.getFormatString(mcMsg);
			String userList = createClientList(clients, Configuration.TS_LIST_GROUPING.getBoolean(), color);
			mcMsg = new Replacer().addSender(sender).addList(userList).addCount(clients.size()).replace(mcMsg);

			if (mcMsg == null || mcMsg.isEmpty()) return;
			send(sender, Level.INFO, mcMsg);
		} else if (args.length == 2 && Configuration.TS_ENABLE_CHANNEL_EVENTS.getBoolean()
				&& args[1].equalsIgnoreCase("channel")) {
			String id = String.valueOf(BukkitSpeak.getQuery().getCurrentQueryClientChannelID());

			Collection<HashMap<String, String>> clientCollection = BukkitSpeak.getClientList().getClients().values();
			List<HashMap<String, String>> clients = new ArrayList<HashMap<String, String>>(clientCollection);
			Iterator<HashMap<String, String>> iterator = clients.iterator();
			while (iterator.hasNext()) {
				HashMap<String, String> user = iterator.next();
				if (!user.get("client_type").equals("0") || !user.get("cid").equals(id)) {
					iterator.remove();
				}
			}

			String mcMsg = Messages.MC_COMMAND_LIST_CHANNEL.get();
			String cc = MessageUtil.getFormatString(mcMsg);
			String userList = createClientList(clients, Configuration.TS_LIST_GROUPING.getBoolean(), cc);
			mcMsg = new Replacer().addSender(sender).addList(userList).addCount(clients.size()).replace(mcMsg);

			if (mcMsg == null || mcMsg.isEmpty()) return;
			send(sender, Level.INFO, mcMsg);
		} else {
			send(sender, Level.INFO, "&4Usage:");
			send(sender, Level.INFO, "&4/ts list (server / channel)");
		}
	}

	private static String createClientList(Collection<HashMap<String, String>> map, boolean grouping, String color) {
		if (map.isEmpty()) return "-";

		if (grouping) {
			PermissionsHelper permHelper = BukkitSpeak.getPermissionsHelper();
			Map<String, List<HashMap<String, String>>> groups = new HashMap<String, List<HashMap<String, String>>>();

			// Group
			for (HashMap<String, String> user : map) {
				ServerGroup sg = permHelper.getServerGroup(user.get("client_servergroups"));
				if (sg == null) {
					BukkitSpeak.log().warning("Could not resolve server group(s) for user \""
							+ user.get("client_nickname") + "\".");
					BukkitSpeak.log().warning("Server groups: " + String.valueOf(user.get("client_servergroups")));
					continue;
				}

				if (groups.containsKey(sg.getName())) {
					groups.get(sg.getName()).add(user);
				} else {
					List<HashMap<String, String>> newGroup = new ArrayList<HashMap<String, String>>();
					newGroup.add(user);
					groups.put(sg.getName(), newGroup);
				}
			}
			if (groups.isEmpty()) return "-";

			// Stringify
			StringBuilder output = new StringBuilder("\n");
			for (Entry<String, List<HashMap<String, String>>> entry : groups.entrySet()) {
				output.append("&r&a").append(entry.getKey()).append(": ");
				for (HashMap<String, String> user : entry.getValue()) {
					output.append(color).append(user.get("client_nickname"));
					output.append("&r&a").append(", ");
				}
				output.setLength(output.length() - 6);
				output.append("\n");
			}
			output.setLength(output.length() - 1);
			return output.toString();
		} else {
			StringBuilder list = new StringBuilder();
			for (HashMap<String, String> user : BukkitSpeak.getClientList().getClients().values()) {
				if (user.get("client_type").equals("0")) {
					list.append(color).append(user.get("client_nickname"));
					list.append("&r&a").append(", ");
				}
			}

			list.setLength(list.length() - 6);
			return list.toString();
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
