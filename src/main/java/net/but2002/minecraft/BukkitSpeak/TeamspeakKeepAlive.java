package net.but2002.minecraft.BukkitSpeak;

import java.util.Date;

public class TeamspeakKeepAlive extends Thread {
	
	private static final int MAX_STEPS = 6;
	private static final int MAX_WAIT = 60;
	
	private BukkitSpeak plugin;
	private int wait, failed;
	
	public TeamspeakKeepAlive(BukkitSpeak bukkitSpeak) {
		plugin = bukkitSpeak;
		wait = 0;
		failed = 0;
	}
	
	@Override
	public void run() {
		if (BukkitSpeak.getQuery().isConnected()) {
			if (plugin.getStoppedTime() != null) plugin.setStoppedTime(null);
			try {
				BukkitSpeak.getQuery().doCommand("clientupdate");
				BukkitSpeak.getClients().asyncUpdateAll();
			} catch (NullPointerException e) {
				plugin.setStoppedTime(new Date());
				wait = 0;
				failed = 0;
			}
		} else if (plugin.getStoppedTime() == null) {
			plugin.setStoppedTime(new Date());
			wait = 0;
			failed = 0;
		} else {
			wait += 1;
			if (wait >= getRetryTime(failed)) {
				wait = 0;
				failed += 1;
				new Thread(plugin.getQueryConnector()).start();
			}
		}
	}
	
	private int getRetryTime(int d) {
		int p = d / 2;
		if (p < MAX_STEPS) {
			return (int) Math.pow(2, p);
		} else {
			return MAX_WAIT;
		}
	}
}
