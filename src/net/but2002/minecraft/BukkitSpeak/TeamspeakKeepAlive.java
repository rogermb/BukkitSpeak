package net.but2002.minecraft.BukkitSpeak;

import java.util.Date;

public class TeamspeakKeepAlive extends Thread {
	
	BukkitSpeak plugin;
	
	public TeamspeakKeepAlive(BukkitSpeak plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run(){
		if (plugin.getQuery().isConnected()) {
			plugin.getQuery().doCommand("clientupdate");
			plugin.getClients().asyncUpdateAll();
		} else if (plugin.getStoppedTime() == null) {
			plugin.setStoppedTime(new Date());
		}
	}
}
