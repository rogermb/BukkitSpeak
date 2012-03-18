package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSpeak extends JavaPlugin {
	
	Logger logger;
	StringManager stringManager;
	TeamspeakHandler ts;
	BukkitSpeakCommandExecutor tsCommand;
	List<String> muted;
	
	public void onEnable() {
		logger = this.getLogger();
		stringManager = new StringManager(this);
		ts = new TeamspeakHandler(this);
		tsCommand = new BukkitSpeakCommandExecutor(this);
		muted = new ArrayList<String>();
		
		this.getCommand("ts").setExecutor(tsCommand);
		new Thread(ts).start();
		
		logger.info("enabled.");
	}
	
	public void onDisable() {
		ts.kill();
		
		logger.info("disabled.");
	}
	
	public String toString() {
		return "§a[§6" + this.getDescription().getName() + "§a]§f " ;
	}
		
	public StringManager getStringManager() {
		return stringManager;
	}
	
	public List<String> getMutedList() {
		return muted;
	}
	
	public TeamspeakHandler getTs() {
		return ts;
	}
	
	public boolean getMuted(Player player) {
		return muted.contains(player.getName());
	}
	
	public void setMuted(Player player, boolean mute) {
		if (mute && !muted.contains(player.getName())) {
			muted.add(player.getName());
		} else if (!mute && muted.contains(player.getName())) {
			muted.remove(player.getName());
		}
	}
	
	public void reload() {
		ts.kill();
		this.reloadConfig();
		
		stringManager = new StringManager(this);
		ts = new TeamspeakHandler(this);
		muted = new ArrayList<String>();
		new Thread(ts).start();
	}
}
