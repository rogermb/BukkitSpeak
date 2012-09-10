package net.but2002.minecraft.BukkitSpeak;

import java.util.Date;

public class TeamspeakKeepAlive extends Thread {
	
	//XXX
	
	private static final int RETRY_TIME = 5;
	
	private BukkitSpeak plugin;
	private int c;
	
	public TeamspeakKeepAlive(BukkitSpeak bukkitSpeak) {
		plugin = bukkitSpeak;
		c = 0;
	}
	
	@Override
	public void run() {
		if (BukkitSpeak.getQuery().isConnected()) {
			if (plugin.getStoppedTime() != null) plugin.setStoppedTime(null);
			try {
				BukkitSpeak.getQuery().doCommand("clientupdate");
				BukkitSpeak.getClients().asyncUpdateAll();
			} catch (Exception e) {
				plugin.setStoppedTime(new Date());
				c = 0;
			}
		} else if (plugin.getStoppedTime() == null) {
			plugin.setStoppedTime(new Date());
			c = 0;
		} else {
			c += 1;
			if (c >= RETRY_TIME) {
				c = 0;
				new Thread(plugin.getQueryConnector()).start();
			}
		}
	}
}
