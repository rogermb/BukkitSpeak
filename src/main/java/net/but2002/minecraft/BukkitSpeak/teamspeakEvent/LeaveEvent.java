package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.dthielke.herochat.Herochat;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class LeaveEvent extends TeamspeakEvent {
	
	private HashMap<String, String> info;
	
	public LeaveEvent(HashMap<String, String> infoMap) {
		int clid = Integer.parseInt(infoMap.get("clid"));
		
		setUser(clid);
		BukkitSpeak.getClientList().removeClient(Integer.parseInt(infoMap.get("clid")));
		info = infoMap;
		performAction();
	}
	
	@Override
	protected void performAction() {
		if (getUser() == null || getClientName().startsWith("Unknown from") || getClientType() != 0) return;
		if (!info.get("reasonid").equals("5")) {
			String m = BukkitSpeak.getStringManager().getMessage("Quit");
			if (m.isEmpty()) return;
			if (BukkitSpeak.useHerochat() && BukkitSpeak.getStringManager().getHerochatUsesEvents()) {
				// Send to Herochat channel
				String c = BukkitSpeak.getStringManager().getHerochatChannel();
				Herochat.getChannelManager().getChannel(c).announce(replaceValues(m, true));
			} else {
				for (Player pl : getOnlinePlayers()) {
					if (!isMuted(pl) && checkPermissions(pl, "leave")) {
						pl.sendMessage(replaceValues(m, true));
					}
				}
			}
			if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
		}
	}
}
