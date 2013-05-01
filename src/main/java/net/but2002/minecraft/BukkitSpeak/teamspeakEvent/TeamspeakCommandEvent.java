package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.ServerGroup;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.TeamspeakCommandSender;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;

public class TeamspeakCommandEvent extends TeamspeakEvent {
	
	private HashMap<String, String> info;
	
	public TeamspeakCommandEvent(HashMap<String, String> infoMap) {
		setUser(Integer.parseInt(infoMap.get("invokerid")));
		info = infoMap;
		
		if (getUser() == null) return;
		performAction();
	}
	
	@Override
	protected void performAction() {
		String command = info.get("msg");
		command = command.substring(BukkitSpeak.getStringManager().getTeamspeakCommandPrefix().length());
		String commandName = command.split(" ")[0].toLowerCase();
		
		ServerGroup sg = getServerGroup(getUser().get("client_servergroups"));
		if (sg == null) {
			BukkitSpeak.log().warning("Could not resolve server group(s) for user \""
					+ getUser().get("client_nickname") + "\".");
			BukkitSpeak.log().warning("Server groups: " + String.valueOf(getUser().get("client_servergroups")));
			return;
		}
		TeamspeakCommandSender tscs = new TeamspeakCommandSender(getUser(), sg.isOp(), sg.getPermissions());
		PluginCommand pc = Bukkit.getPluginCommand(commandName);
		
		// Vanilla and Bukkit commands don't need to be on the whitelist
		if (pc != null && !(sg.getPluginWhitelist().contains(pc.getPlugin().getName()))) {
			tscs.sendMessage(BukkitSpeak.getStringManager().getMessage("PluginNotWhitelisted"));
			return;
		}
		if (sg.getCommandBlacklist().contains(commandName)) {
			tscs.sendMessage(BukkitSpeak.getStringManager().getMessage("CommandBlacklisted"));
			return;
		}
		
		if (BukkitSpeak.getStringManager().getTeamspeakCommandLoggingEnabled()) {
			BukkitSpeak.log().info("Teamspeak client \"" + getClientName() + "\" executed command \"" + command + "\".");
		}
		Bukkit.dispatchCommand(tscs, command);
	}
	
	private ServerGroup getServerGroup(String entry) {
		if ((entry == null) || (entry.isEmpty())) return null;
		if (entry.contains(",")) {
			ServerGroup combined = new ServerGroup();
			for (String group : entry.split(",")) {
				ServerGroup sg = BukkitSpeak.getPermissionsHelper().getServerGroup(group);
				if (sg == null) {
					BukkitSpeak.log().warning("Could not resolve server group " + group);
					continue;
				}
				combined.setOp(combined.isOp() || sg.isOp());
				combined.getPermissions().putAll(sg.getPermissions());
				combined.getPluginWhitelist().addAll(sg.getPluginWhitelist());
				combined.getCommandBlacklist().addAll(sg.getCommandBlacklist());
			}
			return combined;
		} else {
			return BukkitSpeak.getPermissionsHelper().getServerGroup(entry);
		}
	}
}
