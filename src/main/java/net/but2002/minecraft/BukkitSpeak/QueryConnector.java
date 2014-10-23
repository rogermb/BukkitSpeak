package net.but2002.minecraft.BukkitSpeak;

import java.util.Date;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class QueryConnector implements Runnable {

	private BukkitSpeak plugin;
	private JTS3ServerQuery query;
	private Logger logger;

	public QueryConnector() {
		this.plugin = BukkitSpeak.getInstance();
		query = BukkitSpeak.getQuery();
		logger = plugin.getLogger();
	}

	public void run() {
		plugin.setStartedTime(new Date());
		query.removeTeamspeakActionListener();

		if (!query.connectTS3Query(Configuration.MAIN_IP.getString(), Configuration.MAIN_QUERYPORT.getInt())) {
			if (plugin.getStoppedTime() == null) {
				logger.severe("Could not connect to the TS3 server.");
				logger.severe("Make sure that the IP and the QueryPort are correct!");
				logger.severe("You might also be (flood) banned from the server. Check the query whitelist!");
				logger.severe("(" + query.getLastError() + ")");
			}

			plugin.setStoppedTime(new Date());
			return;
		}
		if (!query.loginTS3(Configuration.MAIN_USERNAME.getString(), Configuration.MAIN_PASSWORD.getString())) {
			if (plugin.getStoppedTime() == null) {
				logger.severe("Could not login to the Server Query.");
				logger.severe("Make sure that \"QueryUsername\" and \"QueryPassword\" are correct.");
				logger.severe("(" + query.getLastError() + ")");
			}

			plugin.setStoppedTime(new Date());
			query.closeTS3Connection();
			return;
		}
		if (Configuration.MAIN_SERVERPORT.getInt() > 0) {
			if (!query.selectVirtualServer(Configuration.MAIN_SERVERPORT.getInt(), true)) {
				if (plugin.getStoppedTime() == null) {
					logger.severe("Could not select the virtual server.");
					logger.severe("Make sure TeamSpeakPort is PortNumber OR -VirtualServerId");
					logger.severe("(" + query.getLastError() + ")");
				}

				plugin.setStoppedTime(new Date());
				query.closeTS3Connection();
				return;
			}
		} else {
			if (!query.selectVirtualServer(-(Configuration.MAIN_SERVERPORT.getInt()), false)) {
				if (plugin.getStoppedTime() == null) {
					logger.severe("Could not select the virtual server.");
					logger.severe("Make sure TeamSpeakPort is PortNumber OR -VirtualServerId");
					logger.severe("(" + query.getLastError() + ")");
				}

				plugin.setStoppedTime(new Date());
				query.closeTS3Connection();
				return;
			}
		}
		if (!query.setDisplayName(Configuration.TS_NICKNAME.getString())) {
			logger.warning("Could not set the nickname on Teamspeak.");
			logger.warning("Make sure that the name isn't occupied.");
			logger.warning("(" + query.getLastError() + ")");
		}

		query.setTeamspeakActionListener(plugin.getTSActionListener());

		if (Configuration.TS_ENABLE_SERVER_EVENTS.getBoolean()) {
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0);
		}
		if (Configuration.TS_ENABLE_SERVER_MESSAGES.getBoolean()) {
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0);
		}

		final int cid = Configuration.TS_CHANNEL_ID.getInt();
		final boolean channelEvents = Configuration.TS_ENABLE_CHANNEL_EVENTS.getBoolean();
		final boolean channelMessages = Configuration.TS_ENABLE_CHANNEL_MESSAGES.getBoolean();
		if ((cid != 0 && cid != query.getCurrentQueryClientChannelID()) && (channelEvents || channelMessages)) {
			if (!query.moveClient(query.getCurrentQueryClientID(), cid, Configuration.TS_CHANNEL_PASSWORD.getString())) {
				logger.severe("Could not move the QueryClient into the channel.");
				logger.severe("Ensure that the ChannelID is correct and the password is set if required.");
				logger.severe("(" + query.getLastError() + ")");
			}
		}

		if (channelEvents) {
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, query.getCurrentQueryClientChannelID());
		}
		if (channelMessages) {
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL, query.getCurrentQueryClientChannelID());
		}
		if (Configuration.TS_ENABLE_PRIVATE_MESSAGES.getBoolean()) {
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTPRIVATE, 0);
		}

		BukkitSpeak.getInstance().resetLists();
		plugin.setStoppedTime(null);
		plugin.setStartedTime(null);
		plugin.setStartedTime(new Date());
		logger.info("Connected with SID = " + query.getCurrentQueryClientServerID() + ", CID = "
				+ query.getCurrentQueryClientChannelID() + ", CLID = " + query.getCurrentQueryClientID());
	}
}
