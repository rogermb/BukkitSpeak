package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class ClientMovedEvent extends TeamspeakEvent {
	
	HashMap<String, String> info;
	
	public ClientMovedEvent(BukkitSpeak plugin, HashMap<String, String> info) {
		super(plugin, Integer.parseInt(info.get("clid")));
		this.info = info;
		plugin.getClients().asyncUpdateClient(Integer.parseInt(info.get("clid")));
		user.put("cid", info.get("ctid"));
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if (user != null && !getClientName().startsWith("Unknown from") && getClientType() == 0) {
			if (!info.get("reasonid").equals("4")) {
				if (Integer.parseInt(info.get("ctid")) == plugin.getStringManager().getChannelID()) {
					String m = plugin.getStringManager().getMessage("ChannelEnter");
					if (m.isEmpty()) return;
					for (Player pl : plugin.getServer().getOnlinePlayers()) {
						if (!plugin.getMuted(pl) && CheckPermissions(pl, "channelenter")) {
							pl.sendMessage(replaceValues(m, true));
						}
					}
					plugin.getLogger().info(replaceValues(m, false));
				} else {
					String m = plugin.getStringManager().getMessage("ChannelLeave");
					if (m.isEmpty()) return;
					for (Player pl : plugin.getServer().getOnlinePlayers()) {
						if (!plugin.getMuted(pl) && CheckPermissions(pl, "channelleave")) {
							pl.sendMessage(replaceValues(m, true));
						}
					}
					plugin.getLogger().info(replaceValues(m, false));
				}
			}
		}
	}

}
