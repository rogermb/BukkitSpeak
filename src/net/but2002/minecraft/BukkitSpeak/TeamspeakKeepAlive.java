package net.but2002.minecraft.BukkitSpeak;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class TeamspeakKeepAlive extends Thread {
	
	private JTS3ServerQuery query;
	private boolean kill = false;
	private boolean killed = false;
	
	public TeamspeakKeepAlive(JTS3ServerQuery query) {
		this.query = query;
	}
	
	@Override
	public void run(){
		try {
			double time = System.currentTimeMillis();
			while(!kill){
				if((System.currentTimeMillis() - time) >= 300000){ //keep-alive every 60s
					query.doCommand("clientupdate");
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
