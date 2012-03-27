package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class ClientMovedEvent extends TeamspeakEvent {
	
	public ClientMovedEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("ctid");
		localKeys.add("reasonid");
		localKeys.add("clid");
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
				String message = replaceValues(plugin.getStringManager().getMessage("ChannelEnter"), true);
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "channelenter")) pl.sendMessage(message);
				}
				plugin.getLogger().info(message);
			} else {
				String message = replaceValues(plugin.getStringManager().getMessage("ChannelLeave"), true);
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "channelleave")) pl.sendMessage(message);
				}
				plugin.getLogger().info(message);
			}
		}
	}

}
