package net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils;

import java.util.HashMap;

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
		banClient(id, reason);
	}
	
	private void banClient(int clid, String reason) {
		if (!BukkitSpeak.getQuery().isConnected()) {
			BukkitSpeak.log().warning("banClient(): Not connected to TS3 server!");
			return;
		}
		
		if (clid <= 0) {
			BukkitSpeak.log().warning("banClient(): Client ID must be greater than 0!");
			return;
		}
		
		HashMap<String, String> hmIn;
		StringBuilder command = new StringBuilder().append("banclient");
		command.append(" clid=").append(String.valueOf(clid));
		if (reason != null && !reason.isEmpty()) {
			command.append(" banreason=").append(BukkitSpeak.getQuery().encodeTS3String(reason));
		}
		
		try {
			hmIn = BukkitSpeak.getQuery().doCommand(command.toString());
			
			if (!hmIn.get("id").equals("0")) {
				BukkitSpeak.log().info("banClient()" + hmIn.get("id") + hmIn.get("msg") + hmIn.get("extra_msg")
						+ hmIn.get("failed_permid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
