package net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class QuerySender implements Runnable {
	
	private int id, mode;
	private String msg;
	
	public QuerySender(int targetID, int targetMode, String message) {
		id = targetID;
		mode = targetMode;
		msg = message;
	}
	
	@Override
	public void run() {
		BukkitSpeak.getQuery().sendTextMessage(id, mode, msg);
	}
}
