package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class ClientMovedEvent extends TeamspeakEvent {
	
	public ClientMovedEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("clid");
		localKeys.add("ctid");
		localKeys.add("reasonid");
		parseLocalValues(msg);
		
		try {
			setUser(plugin.getTs().getUserByID(Integer.parseInt(localValues.get("clid"))));
		} catch(Exception e) {
			plugin.getLogger().info("Could not identify user.");
			return;
		}
		
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if (user != null && !getUser().getName().startsWith("Unknown from") && getUser().getClientType() == 0) {
			if (Integer.parseInt(localValues.get("ctid")) == plugin.getStringManager().getChannelID()) {
				String m = plugin.getStringManager().getMessage("ChannelEnter");
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "channelenter")) {
						pl.sendMessage(replaceValues(m, true));
					}
				}
				plugin.getLogger().info(replaceValues(m, false));
			} else {
				String m = plugin.getStringManager().getMessage("ChannelLeave");
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
