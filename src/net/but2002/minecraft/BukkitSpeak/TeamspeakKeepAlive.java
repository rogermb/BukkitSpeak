/**
 * 
 */
package net.but2002.minecraft.BukkitSpeak;

import java.io.PrintWriter;

/**
 * @author greycap
 *
 */
public class TeamspeakKeepAlive extends Thread{
	
	private PrintWriter out;
	
	private boolean kill = false;
	
	public TeamspeakKeepAlive(PrintWriter out){
		this.out = out;
	}
	
	@Override
	public void run(){
		try {
			double time = System.currentTimeMillis();
			while(!kill){
				if((System.currentTimeMillis()-time) >=300000){//keep-alive every 60s
					out.println("clientupdate");
					time = System.currentTimeMillis();
				}
				Thread.sleep(1000);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void kill(){
		kill = true;
	}

}
