package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class LeaveEvent extends TeamspeakEvent{
	
	HashMap<String, String> info;
	
	public LeaveEvent(HashMap<String, String> info) {
		super(Integer.parseInt(info.get("clid")));
		BukkitSpeak.getClients().removeClient(Integer.parseInt(info.get("clid")));
		this.info = info;
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if (user != null && !getClientName().startsWith("Unknown from") && getClientType() == 0) {
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
