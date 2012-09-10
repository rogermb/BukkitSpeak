package net.but2002.minecraft.BukkitSpeak;

import java.util.Date;

public class TeamspeakKeepAlive extends Thread {
	
	//XXX
	
	BukkitSpeak plugin;
	int c;
	
	public TeamspeakKeepAlive(BukkitSpeak plugin) {
		this.plugin = plugin;
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
			if (c >= 5) {
				c = 0;
				new Thread(plugin.getQueryConnector()).start();
			}
		}
	}
}
