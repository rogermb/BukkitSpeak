package net.but2002.minecraft.BukkitSpeak;

import java.util.Date;
import java.util.logging.Logger;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class QueryConnector implements Runnable {
	
	BukkitSpeak plugin;
	JTS3ServerQuery query;
	StringManager stringManager;
	Logger logger;
	
	public QueryConnector() {
		this.plugin = BukkitSpeak.getInstance();
		query = BukkitSpeak.getQuery();
		stringManager = BukkitSpeak.getStringManager();
		logger = plugin.getLogger();
	}
	
	public void run() {
		plugin.setStartedTime(new Date());
		query.removeTeamspeakActionListener();
		
		if (!query.connectTS3Query(stringManager.getIp(), stringManager.getQueryPort())) {
			logger.severe("Could not connect to the TS3 server.");
			logger.severe("Make sure that the IP and the QueryPort are correct!");
			logger.severe("(" + query.getLastError() + ")");
			plugin.setStoppedTime(new Date());
			return;
		}
		if (!query.loginTS3(stringManager.getServerAdmin(), stringManager.getServerPass())) {
			logger.severe("Could not login to the Server Query.");
			logger.severe("Make sure that \"QueryUsername\" and \"QueryPassword\" are correct.");
			logger.severe("(" + query.getLastError() + ")");
			query.closeTS3Connection();
			plugin.setStoppedTime(new Date());
			return;
		}
		if (stringManager.getServerPort() > 0) {
			if (!query.selectVirtualServer(stringManager.getServerPort(), true)) {
				logger.severe("Could not select the virtual server.");
				logger.severe("Make sure TeamSpeakPort is PortNumber OR -VirtualServerId");
				logger.severe("(" + query.getLastError() + ")");
				query.closeTS3Connection();
				plugin.setStoppedTime(new Date());
				return;
			}
		} else {
			if (!query.selectVirtualServer(-(stringManager.getServerPort()), false)) {
				logger.severe("Could not select the virtual server.");
				logger.severe("Make sure TeamSpeakPort is PortNumber OR -VirtualServerId");
				logger.severe("(" + query.getLastError() + ")");
				query.closeTS3Connection();
				plugin.setStoppedTime(new Date());
				return;
			}
		}
		if (!query.setDisplayName(stringManager.getTeamspeakNickname())) {
			logger.warning("Could not set the nickname on Teamspeak.");
			logger.warning("Make sure that the name isn't occupied.");
			logger.warning("(" + query.getLastError() + ")");
		}
		
		query.setTeamspeakActionListener(plugin.ts);
		
		if (stringManager.getUseServer()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0);
		if (stringManager.getUseTextServer()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0);
		if ((stringManager.getChannelID() != 0 && stringManager.getChannelID() != query.getCurrentQueryClientChannelID()) && (stringManager.getUseChannel() || stringManager.getUseTextChannel())) {
			if (!query.moveClient(query.getCurrentQueryClientID(), stringManager.getChannelID(), stringManager.getChannelPass())) {
				logger.severe("Could not move the QueryClient into the channel.");
				logger.severe("Ensure that the ChannelID is correct and the password is set if required.");
				logger.severe("(" + query.getLastError() + ")");
				query.closeTS3Connection();
				plugin.setStoppedTime(new Date());
				return;
			}
		}
		if (stringManager.getUseChannel()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, stringManager.getChannelID());
		if (stringManager.getUseTextChannel()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL, stringManager.getChannelID());
		if (stringManager.getUsePrivateMessages()) query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTPRIVATE, 0);
		
		BukkitSpeak.clients = new ClientList(plugin);
		plugin.setStoppedTime(null);
		plugin.setStartedTime(null);
		plugin.setStartedTime(new Date());
		logger.info("Connected with SID = " + query.getCurrentQueryClientServerID() + ", CID = " + query.getCurrentQueryClientChannelID() + ", CLID = " + query.getCurrentQueryClientID());
		
	}
}
