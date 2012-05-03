package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;

public class BukkitSpeak extends JavaPlugin {
	
	Logger logger;
	StringManager stringManager;
	
	JTS3ServerQuery query;
	TeamspeakActionListener ts;
	TeamspeakKeepAlive tsKeepAlive;
	BukkitSpeakCommandExecutor tsCommand;
	
	ChatListener chatListener;
	List<String> muted;
	HashMap<Integer, String> pmRecipients;
	
	Date started, stopped;
	
	public void onEnable() {
		logger = this.getLogger();
		stringManager = new StringManager(this);
		
		query = new JTS3ServerQuery();
		ts = new TeamspeakListener();
		query.setTeamspeakActionListener(ts);
		setupQuery();
		tsKeepAlive = new TeamspeakKeepAlive(this);
		new Thread(tsKeepAlive).start();
		
		tsCommand = new BukkitSpeakCommandExecutor(this);
		chatListener = new ChatListener(this);
		muted = new ArrayList<String>();
		pmRecipients = new HashMap<Integer, String>();
		
		this.getServer().getPluginManager().registerEvents(chatListener, this);
		this.getCommand("ts").setExecutor(tsCommand);
		
		started = new Date();
		logger.info("enabled.");
	}
	
	public void onDisable() {
		query.removeTeamspeakActionListener();
		query.closeTS3Connection();
		tsKeepAlive.kill();
		
		logger.info("disabled.");
	}
	
	public void setupQuery() {
		query.connectTS3Query(stringManager.getIp(), stringManager.getQueryPort());
		query.loginTS3(stringManager.getServerAdmin(), stringManager.getServerPass());
		
		query.selectVirtualServer(stringManager.getServerPort(), true);
		query.setDisplayName(stringManager.getTeamspeakNickname());
		
		if (stringManager.getUseServer()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0);
		if (stringManager.getUseTextServer()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0);
		if (stringManager.getChannelID() != 0 && (stringManager.getUseChannel() || stringManager.getUseTextChannel())) {
			query.moveClient(0, stringManager.getChannelID(), stringManager.getChannelPass());
		}
		if (stringManager.getUseChannel()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, stringManager.getChannelID());
		if (stringManager.getUseTextChannel()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL, 0);
		if (stringManager.getUseTextServer()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0);
	}
	
	public String toString() {
		return "§a[§6" + this.getDescription().getName() + "§a]§f " ;
	}
	
	public JTS3ServerQuery getQuery() {
		return query;
	}
	
	public StringManager getStringManager() {
		return stringManager;
	}
	
	public List<String> getMutedList() {
		return muted;
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
		pmRecipients.put(tsUser.getID(), player);
	}
	
	public String getRecipient(TeamspeakUser tsUser) {
		if (pmRecipients.containsKey(tsUser.getID())) {
			return pmRecipients.get(tsUser.getID());
		}
		return null;
	}
	
	public Date getStartedTime() {
		return started;
	}
	
	public Date getStoppedTime() {
		return stopped;
	}
	
	public void setStoppedTime(Date d) {
		stopped = d;
	}
	
	public void reload(BukkitSpeakCommandExecutor exec, CommandSender sender) {
		try {
			query.removeTeamspeakActionListener();
			query.closeTS3Connection();
			tsKeepAlive.kill();
			
			this.reloadConfig();
			
			stringManager = new StringManager(this);
			ts = new TeamspeakListener();
			query.setTeamspeakActionListener(ts);
			setupQuery();
			tsKeepAlive = new TeamspeakKeepAlive(this);
			new Thread(tsKeepAlive).start();
			
			chatListener.reload(this);
			muted = new ArrayList<String>();
			pmRecipients = new HashMap<Integer, String>();
			
			exec.send(sender, Level.INFO, "&areloaded.");
		} catch (Exception e) {
			exec.send(sender, Level.INFO, "&4Was unable to reload, an error happened.");
			e.printStackTrace();
		}
	}
}
