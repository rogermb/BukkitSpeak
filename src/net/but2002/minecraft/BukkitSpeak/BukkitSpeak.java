package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.HashMap;
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
	HashMap<TeamspeakUser, String> pmRecipients;
	
	public void onEnable() {
		logger = this.getLogger();
		stringManager = new StringManager(this);
		ts = new TeamspeakHandler(this);
		tsCommand = new BukkitSpeakCommandExecutor(this);
		chatListener = new ChatListener(this);
		muted = new ArrayList<String>();
		pmRecipients = new HashMap<TeamspeakUser, String>();
		
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
	
	public void registerRecipient(String player, TeamspeakUser tsUser) {
		if (pmRecipients.containsKey(tsUser)) {
			pmRecipients.remove(tsUser);
		}
		pmRecipients.put(tsUser, player);
	}
	
	public String getRecipient(TeamspeakUser tsUser) {
		if (pmRecipients.containsKey(tsUser)) {
			return pmRecipients.get(tsUser);
		}
		return null;
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
