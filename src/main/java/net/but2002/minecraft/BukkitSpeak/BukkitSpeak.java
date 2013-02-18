package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.Listeners.PlayerListener;
import net.but2002.minecraft.BukkitSpeak.Listeners.HerochatListener;
import net.but2002.minecraft.BukkitSpeak.Metrics.MetricsUtil;
import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.TeamspeakListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.dthielke.herochat.Herochat;

import com.modcrafting.bukkitspeak.DTS3ServerQuery;

import de.stefan1200.jts3serverquery.*;

public class BukkitSpeak extends JavaPlugin {
	
	private static final int KEEP_ALIVE_DELAY = 1200;
	
	private static BukkitSpeak instance;
	private static StringManager stringManager;
	private static ClientList clients;
	private static ChannelList channels;
	private static JTS3ServerQuery query;
	private static DTS3ServerQuery dquery;
	
	private static List<String> muted;
	private static HashMap<Integer, String> pmRecipients;
	private static HashMap<String, Integer> pmSenders;
	
	private static boolean factions, herochat, mcMMO;
	
	private QueryConnector qc;
	private TeamspeakActionListener ts;
	private TeamspeakKeepAlive tsKeepAlive;
	private BukkitSpeakCommandExecutor tsCommand;
	private PlayerListener chatListener;
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
		Bukkit.getScheduler().runTaskAsynchronously(this, qc);
		tsKeepAlive = new TeamspeakKeepAlive(this);
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, tsKeepAlive, KEEP_ALIVE_DELAY / 2, KEEP_ALIVE_DELAY);
		
		tsCommand = new BukkitSpeakCommandExecutor();
		chatListener = new PlayerListener();
		muted = new ArrayList<String>();
		pmRecipients = new HashMap<Integer, String>();
		pmSenders = new HashMap<String, Integer>();
		
		this.getServer().getPluginManager().registerEvents(chatListener, this);
		this.getCommand("ts").setExecutor(tsCommand);
		this.getCommand("tsa").setExecutor(tsCommand);
		
		/* PlugIn hooks after the initialization */
		factions = Bukkit.getPluginManager().isPluginEnabled("Factions");
		if (factions) logger.info("Hooked into Factions!");
		
		mcMMO = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
		if (mcMMO) logger.info("Hooked into mcMMO!");
		
		herochat = false;
		if (stringManager.getHerochatEnabled()) {
			if (Bukkit.getPluginManager().isPluginEnabled("Herochat")) {
				String channel = stringManager.getHerochatChannel();
				if (Herochat.getChannelManager().getChannel(channel) == null) {
					logger.warning("Could not get the channel (" + channel +  ") specified in the config for Herochat, "
							+ "please make sure it is correct.");
				} else {
					herochat = true;
					this.getServer().getPluginManager().registerEvents(new HerochatListener(), this);
					logger.info("Using Herochat for the chat and using the channel " + channel + ".");
				}
			} else {
				logger.warning("Your config has Herochat set to true but it's not enabled on the server.");
			}
		}
		
		/* Metrics stuff after everything else */
		MetricsUtil.setupMetrics();
		
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
	
	public static ClientList getClientList() {
		return clients;
	}
	
	public static ChannelList getChannelList() {
		return channels;
	}
	
	public void resetLists() {
		clients = new ClientList();
		channels = new ChannelList();
	}
	
	public static void registerRecipient(String player, int clid) {
		pmRecipients.put(clid, player);
		pmSenders.put(player, clid);
	}
	
	public String getRecipient(int clid) {
		return pmRecipients.get(clid);
	}
	
	public Integer getSender(String player) {
		return pmSenders.get(player);
	}
	
	public QueryConnector getQueryConnector() {
		return qc;
	}
	
	public TeamspeakActionListener getTSActionListener() {
		return ts;
	}
	
	public PlayerListener getChatListener() {
		return chatListener;
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
	
	/* Returns true if factions is enabled on the server */
	public static boolean hasFactions() {
		return factions;
	}
	
	/* Returns true if herochat is enabled on the server and set to true in the config */
	public static boolean useHerochat() {
		return herochat;
	}
	
	/* Returns true if mcMMO is enabled on the server */
	public static boolean hasMcMMO() {
		return mcMMO;
	}
	
	public boolean reload() {
		try {
			query.closeTS3Connection();
			
			setStoppedTime(null);
			setStartedTime(null);
			
			qc = new QueryConnector();
			Bukkit.getScheduler().runTaskAsynchronously(this, qc);
			
			muted = new ArrayList<String>();
			pmRecipients = new HashMap<Integer, String>();
			pmSenders = new HashMap<String, Integer>();
			
			this.getLogger().info("reloaded.");
			return true;
		} catch (Exception e) {
			this.getLogger().info("was unable to reload, an error happened.");
			e.printStackTrace();
			return false;
		}
	}
	
	public void reloadStringManager() {
		stringManager = new StringManager();
		query.DEBUG = stringManager.getDebugMode();
	}
}
