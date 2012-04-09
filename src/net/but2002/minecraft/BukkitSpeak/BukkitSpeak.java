package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSpeak extends JavaPlugin {
	
	Logger logger;
	StringManager stringManager;
	TeamspeakHandler ts;
	BukkitSpeakCommandExecutor tsCommand;
	ChatListener chatListener;
	List<String> muted;
	
	public void onEnable() {
		logger = this.getLogger();
		stringManager = new StringManager(this);
		ts = new TeamspeakHandler(this);
		tsCommand = new BukkitSpeakCommandExecutor(this);
		chatListener = new ChatListener(this);
		muted = new ArrayList<String>();
		
		this.getServer().getPluginManager().registerEvents(chatListener, this);
		this.getCommand("ts").setExecutor(tsCommand);
		new Thread(ts).start();
		
		logger.info("enabled.");
	}
	
	public void onDisable() {
		if (ts.getAlive()) ts.kill();
		
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
	
	public void reload(BukkitSpeakCommandExecutor exec, CommandSender sender) {
		try {
			if (ts.getAlive()) ts.kill();
			this.reloadConfig();
			
			stringManager = new StringManager(this);
			ts = new TeamspeakHandler(this);
			tsCommand = new BukkitSpeakCommandExecutor(this);
			chatListener.reload(this);
			muted = new ArrayList<String>();
			
			this.getCommand("ts").setExecutor(tsCommand);
			new Thread(ts).start();
			exec.send(sender, Level.INFO, "&areloaded.");
		} catch (Exception e) {
			exec.send(sender, Level.INFO, "&4Was unable to reload, an error happened.");
			e.printStackTrace();
		}
	}
}
