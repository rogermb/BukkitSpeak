package net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class QueryBan implements Runnable {
	
	private int id;
	private String reason;
	
	public QueryBan(int clientID, String banReason) {
		id = clientID;
		reason = banReason;
	}
	
	@Override
	public void run() {
		BukkitSpeak.getDQuery().banClient(id, reason);
	}
}
