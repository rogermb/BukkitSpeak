package net.but2002.minecraft.BukkitSpeak;

import java.util.Date;

public class TeamspeakKeepAlive extends Thread {
	
	BukkitSpeak plugin;
	boolean kill = false;
	boolean killed = false;
	
	public TeamspeakKeepAlive(BukkitSpeak plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run(){
		try {
			double time = System.currentTimeMillis();
			while(!kill){
				if((System.currentTimeMillis() - time) >= 300000){ //keep-alive every 60s
					if (plugin.getQuery().isConnected()) {
						plugin.getQuery().doCommand("clientupdate");
					} else if (plugin.getStoppedTime() == null) {
						plugin.setStoppedTime(new Date());
					}
					time = System.currentTimeMillis();
				}
				Thread.sleep(1000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			killed = true;
		}
	}
	
	public void kill(){
		kill = true;
		
		try {
			while (!killed) {
				Thread.sleep(50);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
