package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.TeamspeakListener;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.modcrafting.bukkitspeak.DTS3ServerQuery;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;

public class BukkitSpeak extends JavaPlugin {
	
	public DTS3ServerQuery dquery;
	public Logger logger;
	public StringManager stringManager;
	
	public JTS3ServerQuery query;
	public TeamspeakActionListener ts;
	public QueryConnector qc;
	TeamspeakKeepAlive tsKeepAlive;
	BukkitSpeakCommandExecutor tsCommand;
	ClientList clients;
	
	ChatListener chatListener;
	List<String> muted;
	HashMap<Integer, String> pmRecipients;
	
	Date started, stopped, laststarted, laststopped;
	
	public void onEnable() {
		logger = this.getLogger();
		stringManager = new StringManager(this);
		dquery = new DTS3ServerQuery(this);
		query = new JTS3ServerQuery();
		ts = new TeamspeakListener(this);
		qc = new QueryConnector(this);
		this.getServer().getScheduler().scheduleAsyncDelayedTask(this, qc);
		tsKeepAlive = new TeamspeakKeepAlive(this);
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, tsKeepAlive, 600, 1200);
		
		tsCommand = new BukkitSpeakCommandExecutor(this);
		chatListener = new ChatListener(this);
		muted = new ArrayList<String>();
		pmRecipients = new HashMap<Integer, String>();
		
		this.getServer().getPluginManager().registerEvents(chatListener, this);
		this.getCommand("ts").setExecutor(tsCommand);
		this.getCommand("tsa").setExecutor(tsCommand);
		
		logger.info("enabled.");
	}
	
	public void onDisable() {
		query.removeTeamspeakActionListener();
		query.closeTS3Connection();
		
		this.getServer().getScheduler().cancelTasks(this);
		
		logger.info("disabled.");
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
	
	public ClientList getClients() {
		return clients;
	}
	
	public void registerRecipient(String player, Integer clid) {
		if (pmRecipients.containsKey(clid)) {
			pmRecipients.remove(clid);
		}
		pmRecipients.put(clid, player);
	}
	
	public String getRecipient(Integer clid) {
		if (pmRecipients.containsKey(clid)) {
			return pmRecipients.get(clid);
		}
		return null;
	}
	
	public Date getStartedTime() {
		return started;
	}
	
	public Date getStoppedTime() {
		return stopped;
	}
	
	public Date getLastStartedTime() {
		return laststarted;
	}
	
	public Date getLastStoppedTime() {
		return laststopped;
	}
	
	public void setStartedTime(Date d) {
		if (d != null && started == null) {
			started = d;
			laststarted = null;
		} else if (d != null) {
			laststarted = d;
		} else {
			started = null;
			laststarted = null;
		}
	}
	
	public void setStoppedTime(Date d) {
		if (d != null && stopped == null) {
			stopped = d;
		} else if (d != null) {
			laststopped = d;
		} else {
			stopped = null;
			laststopped = null;
		}
	}
	
	public void reload(BukkitSpeakCommandExecutor exec, CommandSender sender) {
		try {
			query.removeTeamspeakActionListener();
			query.closeTS3Connection();
			
			this.reloadConfig();
			
			setStoppedTime(null);
			setStartedTime(null);
			
			stringManager = new StringManager(this);
			ts = new TeamspeakListener(this);
			query.setTeamspeakActionListener(ts);
			qc = new QueryConnector(this);
			new Thread(qc).start();
			
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
