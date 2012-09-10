package net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class QueryPoke implements Runnable {
	
	private int id;
	private String msg;
	
	public QueryPoke(int clientID, String message) {
		id = clientID;
		msg = message;
	}
	
	@Override
	public void run() {
		BukkitSpeak.getQuery().pokeClient(id, msg);
	}
}
