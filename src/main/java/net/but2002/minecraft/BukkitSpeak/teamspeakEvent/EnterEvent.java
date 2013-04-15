package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.dthielke.herochat.Herochat;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class EnterEvent extends TeamspeakEvent {
	
	public EnterEvent(HashMap<String, String> infoMap) {
		int clid = Integer.valueOf(infoMap.get("clid"));
		
		if (!BukkitSpeak.getClientList().containsID(clid)) {
			if (!BukkitSpeak.getClientList().addClient(clid)) return;
		} else {
			return;
		}
		setUser(clid);
		performAction();
	}
	
	protected void performAction() {
		String m = BukkitSpeak.getStringManager().getMessage("Join");
		if (m.isEmpty()) return;
		if (BukkitSpeak.useHerochat() && BukkitSpeak.getStringManager().getHerochatUsesEvents()) {
			// Send to Herochat channel
			String c = BukkitSpeak.getStringManager().getHerochatChannel();
			Herochat.getChannelManager().getChannel(c).announce(replaceValues(m, true));
		} else {
			for (Player pl : getOnlinePlayers()) {
				if (!isMuted(pl) && checkPermissions(pl, "join")) {
					pl.sendMessage(replaceValues(m, true));
				}
			}
		}
		if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
	}
}
