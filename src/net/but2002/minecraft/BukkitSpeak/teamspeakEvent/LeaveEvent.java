package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class LeaveEvent extends TeamspeakEvent{
	
	HashMap<String, String> info;
	
	public LeaveEvent(BukkitSpeak plugin, HashMap<String, String> info) {
		super(plugin, Integer.parseInt(info.get("clid")));
		plugin.getClients().removeClient(Integer.parseInt(info.get("clid")));
		this.info = info;
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if (user != null && !getClientName().startsWith("Unknown from") && getClientType() == 0) {
			if (!info.get("reasonid").equals("5")) {
				String m = plugin.getStringManager().getMessage("Quit");
				if (m.isEmpty()) return;
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					if (!plugin.getMuted(pl) && checkPermissions(pl, "leave")) {
						pl.sendMessage(replaceValues(m, true));
					}
				}
				if (plugin.getStringManager().getLogInConsole()) plugin.getLogger().info(replaceValues(m, false));
			}
		}
	}
}
