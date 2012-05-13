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

import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;

public class BukkitSpeak extends JavaPlugin {
	
	Logger logger;
	StringManager stringManager;
	
	JTS3ServerQuery query;
	TeamspeakActionListener ts;
	QueryConnector qc;
	TeamspeakKeepAlive tsKeepAlive;
	BukkitSpeakCommandExecutor tsCommand;
	ClientList clients;
	
	ChatListener chatListener;
	List<String> muted;
	HashMap<Integer, String> pmRecipients;
	
	Date started, stopped;
	
	public void onEnable() {
		logger = this.getLogger();
		stringManager = new StringManager(this);
		
		query = new JTS3ServerQuery();
		ts = new TeamspeakListener(this);
		qc = new QueryConnector(this);
		new Thread(qc).start();
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
	
	public void setStoppedTime(Date d) {
		stopped = d;
	}
	
	public void reload(BukkitSpeakCommandExecutor exec, CommandSender sender) {
		try {
			query.removeTeamspeakActionListener();
			query.closeTS3Connection();
			
			this.reloadConfig();
			
			stringManager = new StringManager(this);
			ts = new TeamspeakListener(this);
			query.setTeamspeakActionListener(ts);
			qc = new QueryConnector(this);
			new Thread(qc).start();
			
			chatListener.reload(this);
			muted = new ArrayList<String>();
			pmRecipients = new HashMap<Integer, String>();
			
			stopped = null;
			started = new Date();
			exec.send(sender, Level.INFO, "&areloaded.");
		} catch (Exception e) {
			exec.send(sender, Level.INFO, "&4Was unable to reload, an error happened.");
			e.printStackTrace();
		}
	}
}

class QueryConnector implements Runnable {
	
	BukkitSpeak plugin;
	JTS3ServerQuery query;
	StringManager stringManager;
	Logger logger;
	
	
	public QueryConnector(BukkitSpeak plugin) {
		this.plugin = plugin;
		query = plugin.getQuery();
		stringManager = plugin.getStringManager();
		logger = plugin.getLogger();
	}
	
	public void run() {
		setStartedTime();
		
		query.closeTS3Connection();
		query.removeTeamspeakActionListener();
		
		if (!query.connectTS3Query(stringManager.getIp(), stringManager.getQueryPort())) {
			logger.severe("Could not connect to the TS3 server.");
			logger.severe("Make sure that the IP and the QueryPort are correct!");
			setStoppedTime();
			return;
		}
		if (!query.loginTS3(stringManager.getServerAdmin(), stringManager.getServerPass())) {
			logger.severe("Could not login to the Server Query.");
			logger.severe("Make sure that \"QueryUsername\" and \"QueryPassword\" are correct.");
			query.closeTS3Connection();
			setStoppedTime();
			return;
		}
		if (stringManager.getServerPort() > 0) {
			if (!query.selectVirtualServer(stringManager.getServerPort(), true)) {
				logger.severe("Could not select the virtual server.");
				logger.severe("Make sure TeamSpeakPort is PortNumber OR -VirtualServerId");
				query.closeTS3Connection();
				setStoppedTime();
				return;
			}
		} else {
			if (!query.selectVirtualServer(-(stringManager.getServerPort()), false)) {
				logger.severe("Could not select the virtual server.");
				logger.severe("Make sure TeamSpeakPort is PortNumber OR -VirtualServerId");
				query.closeTS3Connection();
				setStoppedTime();
				return;
			}
		}
		query.setDisplayName(stringManager.getTeamspeakNickname());
		
		query.setTeamspeakActionListener(plugin.ts);
		
		if (stringManager.getUseServer()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0);
		if (stringManager.getUseTextServer()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0);
		if (stringManager.getChannelID() != 0 && (stringManager.getUseChannel() || stringManager.getUseTextChannel())) {
			if (!query.moveClient(query.getCurrentQueryClientID(), stringManager.getChannelID(), stringManager.getChannelPass())) {
				logger.severe("Could not move the QueryClient into the channel.");
				logger.severe("Ensure that the ChannelID is correct and the password is set if required.");
				query.closeTS3Connection();
				setStoppedTime();
				return;
			}
		}
		if (stringManager.getUseChannel()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, stringManager.getChannelID());
		if (stringManager.getUseTextChannel()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL, stringManager.getChannelID());
		if (stringManager.getUsePrivateMessages()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTPRIVATE, 0);
		
		plugin.clients = new ClientList(plugin);
		setStartedTime();
		logger.info("Connected with SID = " + query.getCurrentQueryClientServerID() + ", CID = " + query.getCurrentQueryClientChannelID() + ", CLID = " + query.getCurrentQueryClientID());
		
	}
	
	private void setStoppedTime() {
		plugin.stopped = new Date();
	}
	
	private void setStartedTime() {
		plugin.started = new Date();
	}
}