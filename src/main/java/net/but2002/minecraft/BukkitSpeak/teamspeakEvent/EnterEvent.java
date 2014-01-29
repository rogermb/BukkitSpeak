package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;

public class EnterEvent extends TeamspeakEvent {

	public EnterEvent(HashMap<String, String> infoMap) {
		int clid = Integer.valueOf(infoMap.get("clid"));

		if (BukkitSpeak.getClientList().containsID(clid)) return;
		BukkitSpeak.getClientList().updateClient(clid);
		if (!BukkitSpeak.getClientList().containsID(clid)) return;

		setUser(clid);
		performAction();
	}

	protected void performAction() {
		sendMessage(Messages.TS_EVENT_SERVER_JOIN, "join");
	}
}
