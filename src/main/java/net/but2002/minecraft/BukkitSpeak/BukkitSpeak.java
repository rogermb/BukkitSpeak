package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.Listeners.ChatListener;
import net.but2002.minecraft.BukkitSpeak.Listeners.PlayerListener;
import net.but2002.minecraft.BukkitSpeak.Listeners.HerochatListener;
import net.but2002.minecraft.BukkitSpeak.Metrics.MetricsUtil;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.PermissionsHelper;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.TeamspeakCommandExecutor;
import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.TeamspeakListener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.dthielke.herochat.Herochat;

import de.stefan1200.jts3serverquery.*;

public class BukkitSpeak extends JavaPlugin {

	private static final int KEEP_ALIVE_DELAY = 1200;

	private static BukkitSpeak instance;
	private static TeamspeakCommandExecutor tsCommand;
	private static PermissionsHelper permissionsHelper;
	private static ClientList clients;
	private static ChannelList channels;
	private static JTS3ServerQuery query;

	private static List<String> muted;
	private static HashMap<Integer, String> pmRecipients;
	private static HashMap<String, Integer> pmSenders;

	private static boolean factions, herochat, mcMMO;

	private QueryConnector qc;
	private TeamspeakActionListener ts;
	private TeamspeakKeepAlive tsKeepAlive;
	private BukkitSpeakCommandExecutor mcCommand;
	private PlayerListener playerListener;
	private ChatListener chatListener;
	private Logger logger;

	private Date started, stopped, laststarted, laststopped;

	public static BukkitSpeak getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		logger = this.getLogger();
		muted = new ArrayList<String>();
		pmRecipients = new HashMap<Integer, String>();
		pmSenders = new HashMap<String, Integer>();

		Configuration.reload();
		Messages.reload();

		query = new JTS3ServerQuery();
		query.DEBUG = Configuration.TS_DEBUGGING.getBoolean();
		clients = new ClientList();
		channels = new ChannelList();
		permissionsHelper = new PermissionsHelper();

		ts = new TeamspeakListener();
		qc = new QueryConnector();
		Bukkit.getScheduler().runTaskAsynchronously(this, qc);
		tsKeepAlive = new TeamspeakKeepAlive(this);
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, tsKeepAlive, KEEP_ALIVE_DELAY / 2, KEEP_ALIVE_DELAY);

		mcCommand = new BukkitSpeakCommandExecutor();
		tsCommand = new TeamspeakCommandExecutor();
		playerListener = new PlayerListener();
		chatListener = new ChatListener();

		EventPriority p = Configuration.TS_CHAT_LISTENER_PRIORITY.getEventPriority();
		boolean i = (p != EventPriority.LOWEST);
		getServer().getPluginManager().registerEvent(AsyncPlayerChatEvent.class, chatListener, p, chatListener, this, i);
		getServer().getPluginManager().registerEvents(playerListener, this);
		getCommand("ts").setExecutor(mcCommand);
		getCommand("tsa").setExecutor(mcCommand);

		/* PlugIn hooks after the initialization */
		factions = Bukkit.getPluginManager().isPluginEnabled("Factions");
		if (factions) logger.info("Hooked into Factions!");

		mcMMO = Bukkit.getPluginManager().isPluginEnabled("mcMMO");
		if (mcMMO) logger.info("Hooked into mcMMO!");

		herochat = false;
		if (Configuration.PLUGINS_HEROCHAT_ENABLED.getBoolean()) {
			if (Bukkit.getPluginManager().isPluginEnabled("Herochat")) {
				String channel = Configuration.PLUGINS_HEROCHAT_CHANNEL.getString();
				if (Herochat.getChannelManager().getChannel(channel) == null) {
					logger.warning("Could not get the channel (" + channel + ") specified in the config for Herochat, "
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

	public static TeamspeakCommandExecutor getTeamspeakCommandExecutor() {
		return tsCommand;
	}

	public static PermissionsHelper getPermissionsHelper() {
		return permissionsHelper;
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

	public ChatListener getChatListener() {
		return chatListener;
	}

	public PlayerListener getPlayerListener() {
		return playerListener;
	}

	public static ClientList getClientList() {
		return clients;
	}

	public static ChannelList getChannelList() {
		return channels;
	}

	public void resetLists() {
		if (isFloodBanned()) return;
		clients.updateAll();
		if (isFloodBanned()) return;
		channels.updateAll();
		if (isFloodBanned()) return;
		if (Configuration.TS_COMMANDS_ENABLED.getBoolean()) {
			permissionsHelper.run();
		}
		if (isFloodBanned()) return;
	}

	private boolean isFloodBanned() {
		if (query.getLastErrorID() == 3331) {
			logger.severe("You were flood banned. You need to add the Minecraft server IP to the TeamSpeak query whitelist!");
			getServer().getPluginManager().disablePlugin(this);
			return true;
		}
		return false;
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

			Configuration.reload();
			Messages.reload();
			query.DEBUG = Configuration.TS_DEBUGGING.getBoolean();

			AsyncPlayerChatEvent.getHandlerList().unregister(chatListener);
			EventPriority p = Configuration.TS_CHAT_LISTENER_PRIORITY.getEventPriority();
			boolean i = (p != EventPriority.LOWEST);
			getServer().getPluginManager().registerEvent(AsyncPlayerChatEvent.class, chatListener, p, chatListener, this, i);
			tsCommand = new TeamspeakCommandExecutor();

			qc = new QueryConnector();
			Bukkit.getScheduler().runTaskAsynchronously(this, qc);

			this.getLogger().info("reloaded.");
			return true;
		} catch (Exception e) {
			this.getLogger().info("was unable to reload, an error happened.");
			e.printStackTrace();
			return false;
		}
	}
}
