package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

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
		if (info.get("reasonid").equals("5")) return;
		
		sendMessage("Quit", "leave");
	}
}
