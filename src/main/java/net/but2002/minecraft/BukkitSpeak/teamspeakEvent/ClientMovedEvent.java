package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class ClientMovedEvent extends TeamspeakEvent {
	
	private HashMap<String, String> info;
	
	public ClientMovedEvent(HashMap<String, String> infoMap) {
		super(Integer.parseInt(infoMap.get("clid")));
		info = infoMap;
		BukkitSpeak.getClients().asyncUpdateClient(Integer.parseInt(infoMap.get("clid")));
		getUser().put("cid", infoMap.get("ctid"));
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if (getUser() != null && !getClientName().startsWith("Unknown from") && getClientType() == 0) {
			if (!info.get("reasonid").equals("4")) {
				if (Integer.parseInt(info.get("ctid")) == BukkitSpeak.getStringManager().getChannelID()) {
					String m = BukkitSpeak.getStringManager().getMessage("ChannelEnter");
					if (m.isEmpty()) return;
					for (Player pl : getOnlinePlayers()) {
						if (!isMuted(pl) && checkPermissions(pl, "channelenter")) {
							pl.sendMessage(replaceValues(m, true));
						}
					}
					if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
				} else {
					String m = BukkitSpeak.getStringManager().getMessage("ChannelLeave");
					if (m.isEmpty()) return;
					for (Player pl : getOnlinePlayers()) {
						if (!isMuted(pl) && checkPermissions(pl, "channelleave")) {
							pl.sendMessage(replaceValues(m, true));
						}
					}
					if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
				}
			}
		}
	}

}
