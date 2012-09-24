package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.TeamspeakListener;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.modcrafting.bukkitspeak.DTS3ServerQuery;

import de.stefan1200.jts3serverquery.*;

public class BukkitSpeak extends JavaPlugin {
	
	private static final int KEEP_ALIVE_DELAY = 1200;
	
	private static BukkitSpeak instance;
	private static StringManager stringManager;
	private static ClientList clients;
	private static JTS3ServerQuery query;
	private static DTS3ServerQuery dquery;
	
	private static List<String> muted;
	private static HashMap<Integer, String> pmRecipients;
	private static HashMap<String, Integer> pmSenders;
	
	private static boolean factions;
	
	private QueryConnector qc;
	private TeamspeakActionListener ts;
	private TeamspeakKeepAlive tsKeepAlive;
	private BukkitSpeakCommandExecutor tsCommand;
	private ChatListener chatListener;
	private Logger logger;
	
	private Date started, stopped, laststarted, laststopped;
	
	public static BukkitSpeak getInstance() {
		return instance;
	}
	
	public void onEnable() {
		
		instance = this;
		logger = this.getLogger();
		stringManager = new StringManager();
		query = new JTS3ServerQuery();
		query.DEBUG = stringManager.getDebugMode();
		
		dquery = new DTS3ServerQuery();
		ts = new TeamspeakListener();
		qc = new QueryConnector();
		this.getServer().getScheduler().scheduleAsyncDelayedTask(this, qc);
		tsKeepAlive = new TeamspeakKeepAlive(this);
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, tsKeepAlive, KEEP_ALIVE_DELAY / 2, KEEP_ALIVE_DELAY);
		
		tsCommand = new BukkitSpeakCommandExecutor();
		chatListener = new ChatListener();
		muted = new ArrayList<String>();
		pmRecipients = new HashMap<Integer, String>();
		pmSenders = new HashMap<String, Integer>();
		
		this.getServer().getPluginManager().registerEvents(chatListener, this);
		this.getCommand("ts").setExecutor(tsCommand);
		this.getCommand("tsa").setExecutor(tsCommand);
		
		/* PlugIn hooks after everything else */
		factions = Bukkit.getPluginManager().isPluginEnabled("Factions");
		
		logger.info("enabled.");
	}
	
	public void onDisable() {
		query.removeTeamspeakActionListener();
		query.closeTS3Connection();
		
		this.getServer().getScheduler().cancelTasks(this);
		
		logger.info("disabled.");
	}
	
	public String toString() {
		return "\u00A7a[\u00A76" + this.getDescription().getName() + "\u00A7a]\u00A7f ";
	}
	
	public static String getFullName() {
		return instance.toString();
	}
	
	public static Logger log() {
		return instance.getLogger();
	}
	
	public static JTS3ServerQuery getQuery() {
		return query;
	}
	
	public static DTS3ServerQuery getDQuery() {
		return dquery;
	}
	
	public static StringManager getStringManager() {
		return stringManager;
	}
	
	public static List<String> getMutedList() {
		return muted;
	}
	
	public static boolean getMuted(Player player) {
		return muted.contains(player.getName());
	}
	
	public static void setMuted(Player player, boolean mute) {
		if (mute && !muted.contains(player.getName())) {
			muted.add(player.getName());
		} else if (!mute && muted.contains(player.getName())) {
			muted.remove(player.getName());
		}
	}
	
	public static ClientList getClients() {
		return clients;
	}
	
	public void resetClientList() {
		clients = new ClientList(this);
	}
	
	public static void registerRecipient(String player, Integer clid) {
		if (pmRecipients.containsKey(clid)) pmRecipients.remove(clid);
		if (pmSenders.containsKey(player)) pmSenders.remove(player);
		
		pmRecipients.put(clid, player);
		pmSenders.put(player, clid);
	}
	
	public String getRecipient(Integer clid) {
		if (pmRecipients.containsKey(clid)) {
			return pmRecipients.get(clid);
		}
		return null;
	}
	
	public Integer getSender(String player) {
		if (pmSenders.containsKey(player)) {
			return pmSenders.get(player);
		}
		return null;
	}
	
	public QueryConnector getQueryConnector() {
		return qc;
	}
	
	public TeamspeakActionListener getTSActionListener() {
		return ts;
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
	
	public static boolean hasFactions() {
		return factions;
	}
	
	public void reload(CommandSender sender) {
		try {
			query.closeTS3Connection();
			
			setStoppedTime(null);
			setStartedTime(null);
			
			reloadStringManager();
			
			qc = new QueryConnector();
			this.getServer().getScheduler().scheduleAsyncDelayedTask(this, qc);
			
			muted = new ArrayList<String>();
			pmRecipients = new HashMap<Integer, String>();
			pmSenders = new HashMap<String, Integer>();
			
			if (sender instanceof Player) sender.sendMessage(this + "\u00A7areloaded.");
			this.getLogger().info("reloaded.");
		} catch (Exception e) {
			if (sender instanceof Player) {
				sender.sendMessage(this + "\u00A74was unable to reload, an error happened.");
			}
			this.getLogger().info("was unable to reload, an error happened.");
			e.printStackTrace();
		}
	}
	
	public void reloadStringManager() {
		this.reloadConfig();
		stringManager = new StringManager();
		query.DEBUG = stringManager.getDebugMode();
	}
}
