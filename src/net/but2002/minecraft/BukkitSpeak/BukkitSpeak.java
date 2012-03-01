package net.but2002.minecraft.BukkitSpeak;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;



public class BukkitSpeak extends JavaPlugin {
	
	private Logger logger;
	private StringManager stringManager;
	private TeamspeakHandler ts;
	
	public Logger getLogger() {
		return logger;
	}
	
	@Override
	public void onEnable() {
		logger = getServer().getLogger();
		stringManager = new StringManager(this);
		ts = new TeamspeakHandler(this);
		new Thread(ts).start();
		
		logger.log(java.util.logging.Level.INFO,getDescription().getName()+" Version: "+getDescription().getVersion()+" enabled.");
	}

	@Override
	public void onDisable() {
		ts.kill();
		logger.log(java.util.logging.Level.INFO,getDescription().getName()+" Version: "+getDescription().getVersion()+" disabled.");
	}
	
	public String toString() {
		return "[" + this.getDescription().getName() + " v" + this.getDescription().getVersion() + "] " ;
	}
	
	public void disable(){
		getPluginLoader().disablePlugin(this);
		logger.severe(this + "was disabled due to an error.");
	}

	public StringManager getStringManager() {
		return stringManager;
	}
	
	public TeamspeakHandler getTs() {
		return ts;
	}

}
