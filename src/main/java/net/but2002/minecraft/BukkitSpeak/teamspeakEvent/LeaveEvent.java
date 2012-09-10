package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class LeaveEvent extends TeamspeakEvent {
	
	private HashMap<String, String> info;
	
	public LeaveEvent(HashMap<String, String> infoMap) {
		super(Integer.parseInt(infoMap.get("clid")));
		BukkitSpeak.getClients().removeClient(Integer.parseInt(infoMap.get("clid")));
		info = infoMap;
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if (getUser() != null && !getClientName().startsWith("Unknown from") && getClientType() == 0) {
			if (!info.get("reasonid").equals("5")) {
				String m = BukkitSpeak.getStringManager().getMessage("Quit");
				if (m.isEmpty()) return;
				for (Player pl : getOnlinePlayers()) {
					if (!isMuted(pl) && checkPermissions(pl, "leave")) {
						pl.sendMessage(replaceValues(m, true));
					}
				}
				if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
			}
		}
	}
}
