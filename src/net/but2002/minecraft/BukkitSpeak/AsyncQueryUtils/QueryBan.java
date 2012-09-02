package net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class QueryBan implements Runnable {
	
	int id;
	String reason;
	
	public QueryBan(int clientID, String banReason) {
		id = clientID;
		reason = banReason;
	}
	
	@Override
	public void run() {
		BukkitSpeak.getDQuery().banClient(id, reason);
	}
}
