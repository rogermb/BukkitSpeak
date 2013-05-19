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
		String cmd = info.get("msg");
		cmd = cmd.substring(BukkitSpeak.getStringManager().getTeamspeakCommandPrefix().length());
		String commandName = cmd.split(" ")[0].toLowerCase();
		
		ServerGroup sg = getServerGroup(getUser().get("client_servergroups"));
		if (sg == null) {
			BukkitSpeak.log().warning("Could not resolve server group(s) for user \""
					+ getUser().get("client_nickname") + "\".");
			BukkitSpeak.log().warning("Server groups: " + String.valueOf(getUser().get("client_servergroups")));
			return;
		}
		if (sg.isBlocked()) return;
		
		TeamspeakCommandSender tscs = new TeamspeakCommandSender(getUser(), sg.isOp(), sg.getPermissions());
		PluginCommand pc = Bukkit.getPluginCommand(commandName);
		
		// Vanilla and Bukkit commands don't need to be on the whitelist
		if (pc != null && !(sg.getPluginWhitelist().contains(pc.getPlugin().getName()))) {
			String m = BukkitSpeak.getStringManager().getMessage("PluginNotWhitelisted");
			if (m.isEmpty()) return;
			getUser().put("command_name", pc.getName());
			getUser().put("command_description", pc.getDescription());
			getUser().put("command_plugin", pc.getPlugin().getName());
			tscs.sendMessage(replaceValues(m, true));
			if (BukkitSpeak.getStringManager().getTeamspeakCommandLoggingEnabled()) {
				BukkitSpeak.log().info("TS client \"" + getClientName() + "\" tried executing command \"" + cmd + "\",");
				BukkitSpeak.log().info("but the plugin \"" + pc.getPlugin().getName() + "\" was not whitelisted.");
			}
			return;
		}
		if (sg.getCommandBlacklist().contains(commandName)) {
			String m = BukkitSpeak.getStringManager().getMessage("CommandBlacklisted");
			if (m.isEmpty()) return;
			getUser().put("command_name", pc.getName());
			getUser().put("command_description", pc.getDescription());
			getUser().put("command_plugin", pc.getPlugin().getName());
			tscs.sendMessage(replaceValues(m, true));
			if (BukkitSpeak.getStringManager().getTeamspeakCommandLoggingEnabled()) {
				BukkitSpeak.log().info("TS client \"" + getClientName() + "\" tried executing command \"" + cmd + "\",");
				BukkitSpeak.log().info("but the command was blacklisted.");
			}
			return;
		}
		
		if (BukkitSpeak.getStringManager().getTeamspeakCommandLoggingEnabled()) {
			BukkitSpeak.log().info("TS client \"" + getClientName() + "\" executed command \"" + cmd + "\".");
		}
		Bukkit.dispatchCommand(tscs, cmd);
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
				
				// If one group is blocked, the resulting group should be blocked, too
				if (sg.isBlocked()) return new ServerGroup(true);
				
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
