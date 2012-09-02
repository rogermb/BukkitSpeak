package net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class QueryKick implements Runnable {
	
	int id;
	boolean local;
	String reason;
	
	public QueryKick(int clientID, boolean onlyChannelKick, String kickReason) {
		id = clientID;
		local = onlyChannelKick;
		reason = kickReason;
	}
	
	@Override
	public void run() {
		BukkitSpeak.getQuery().kickClient(id, local, reason);
	}
}
