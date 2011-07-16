/**
 * 
 */
package net.but2002.minecraft.BukkitSpeak;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;



/**
 * @author greycap
 *
 */
public class BukkitSpeak extends JavaPlugin {
	

	private Logger logger;
	
	private StringManager stringManager;
	
	private TeamspeakHandler ts;
	

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}
		
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.Plugin#onEnable()
	 */
	@Override
	public void onEnable() {
		logger = getServer().getLogger();
		
		stringManager = new StringManager(this);
				
		ts = new TeamspeakHandler(this);
		new Thread(ts).start();
		
		logger.log(java.util.logging.Level.INFO,getDescription().getName()+" Version: "+getDescription().getVersion()+" enabled.");
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	@Override
	public void onDisable() {
		ts.kill();
		logger.log(java.util.logging.Level.INFO,getDescription().getName()+" Version: "+getDescription().getVersion()+" disabled.");
	}

	public void disable(){
		getPluginLoader().disablePlugin(this);
		logger.log(java.util.logging.Level.SEVERE,getDescription().getName()+" was disabled due to an error.");
	}

	/**
	 * @return the stringManager
	 */
	public StringManager getStringManager() {
		return stringManager;
	}
	
	/**
	 * @return the ts
	 */
	public TeamspeakHandler getTs() {
		return ts;
	}

}
