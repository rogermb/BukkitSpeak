package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;

public class ClientMovedEvent extends TeamspeakEvent {

	private HashMap<String, String> info;

	public ClientMovedEvent(HashMap<String, String> infoMap) {
		setUser(Integer.parseInt(infoMap.get("clid")));
		info = infoMap;
		BukkitSpeak.getClientList().asyncUpdateClient(Integer.parseInt(infoMap.get("clid")));

		if (getUser() == null) return;
		getUser().put("cid", infoMap.get("ctid"));
		performAction();
	}

	@Override
	protected void performAction() {
		if (getClientName().startsWith("Unknown from") || getClientType() != 0) return;

		if (info.get("reasonid").equals("4")) return;

		if (Integer.parseInt(info.get("ctid")) == BukkitSpeak.getQuery().getCurrentQueryClientChannelID()) {
			// Client entered channel
			sendMessage(Messages.TS_EVENT_CHANNEL_ENTER, "channelenter");
		} else {
			// Client left channel
			sendMessage(Messages.TS_EVENT_CHANNEL_LEAVE, "channelleave");
		}
	}
}
