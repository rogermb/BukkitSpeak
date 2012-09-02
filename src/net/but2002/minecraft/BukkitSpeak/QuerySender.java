package net.but2002.minecraft.BukkitSpeak;

public class QuerySender implements Runnable {
	
	int id, mode;
	String msg;
	
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
