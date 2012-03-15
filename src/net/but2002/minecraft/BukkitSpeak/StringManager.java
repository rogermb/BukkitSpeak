package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import org.bukkit.configuration.MemoryConfiguration;

import net.but2002.minecraft.BukkitSpeak.util.ConfigReader;

public class StringManager {
	
	private HashMap<String,String> strings = new HashMap<String,String>();
	private String ip, serverAdmin, serverPass, tsName;
	private int queryPort, serverPort, tsChannelID;
	private Boolean tsServer, tsTextServer, tsChannel, tsTextChannel;
	
	public static final String CONFIG_SECTION = "main";
	public static final String CONFIG_IP = "TeamSpeakIp";
	public static final String CONFIG_SERVERPORT = "TeamSpeakPort";
	public static final String CONFIG_QUERYPORT = "QueryPort";
	public static final String CONFIG_SERVERADMIN = "QueryUsername";
	public static final String CONFIG_SERVERPASS = "QueryPassword";
	
	public static final String TEAMSPEAK_SECTION = "teamspeak";
	public static final String TEAMSPEAK_NAME = "BroadcastNickname";
	public static final String TEAMSPEAK_SERVER = "ListenToServerEvents";
	public static final String TEAMSPEAK_TEXTSERVER = "ListenToServerBroadcasts";
	public static final String TEAMSPEAK_CHANNEL = "ListenToChannel";
	public static final String TEAMSPEAK_TEXTCHANNEL = "ListenToChannelChat";
	public static final String TEAMSPEAK_CHANNELID = "ChannelID";
	
	public static final String MESSAGES_SECTION = "messages";
	public static final String[][] MESSAGES = {
		{"Join", "&e%client_nickname% &ahas joined TeamSpeak"}, 
		{"Quit", "&e%client_nickname% &ahas left TeamSpeak"}, 
		{"ServerMsg", "[&cTS&f] &e%client_nickname%: &a%msg%"}, 
		{"ChannelMsg", "&e%client_nickname%: &f%msg%"},
		{"OnlineList", "&aCurrently online: &e%list%"},
		{"Mute", "&aYou are now muted."},
		{"Unmute", "&aYou aren't muted anymore."}};
	
	public static final String MUTED_SECTION = "muted";
	
	public StringManager(BukkitSpeak plugin) {
		
		ConfigReader reader = new ConfigReader(plugin);
		
		if (plugin.getConfig().getKeys(true).size()==0) {
			plugin.saveResource("config.yml", false);
			plugin.reloadConfig();
		}
		plugin.getConfig().setDefaults(new MemoryConfiguration());
		
		ip = reader.getString(CONFIG_SECTION, CONFIG_IP, "1.2.3.4");
		serverPort = reader.getInteger(CONFIG_SECTION, CONFIG_SERVERPORT, 9987);
		queryPort = reader.getInteger(CONFIG_SECTION, CONFIG_QUERYPORT, 10011);
		serverAdmin = reader.getString(CONFIG_SECTION, CONFIG_SERVERADMIN, "admin");
		serverPass = reader.getString(CONFIG_SECTION, CONFIG_SERVERPASS, "123456");
		
		tsName = reader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_NAME, "Minecraft");
		tsServer = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_SERVER, true);
		tsTextServer = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_TEXTSERVER, true);
		tsChannel = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_SERVER, false);
		tsTextChannel = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_SERVER, false);
		tsChannelID = reader.getInteger(TEAMSPEAK_SECTION, TEAMSPEAK_SERVER, 0);
		
		for (String[] keyPair : MESSAGES) {
			try {
				String currentValue = reader.getString(MESSAGES_SECTION, keyPair[0], keyPair[1]);
				strings.put(keyPair[0], currentValue);
			} catch (Exception e) {
				plugin.getLogger().severe("Was unable to load all the messages. This is probably a programming error.");
			}
		}
		
		plugin.saveConfig();
		
	}
	
	public String getMessage(String key) {
		try {
			return strings.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getIp() {
		return ip;
	}
	
	public int getServerPort() {
		return serverPort;
	}

	public int getQueryPort() {
		return queryPort;
	}	
	
	public String getServerAdmin() {
		return serverAdmin;
	}
	
	public String getServerPass() {
		return serverPass;
	}
	
	public String getTeamspeakNickname() {
		return tsName;
	}
	
	public Boolean getUseServer() {
		return tsServer;
	}
	
	public Boolean getUseTextServer() {
		return tsTextServer;
	}
	
	public Boolean getUseChannel() {
		return tsChannel;
	}
	
	public Boolean getUseTextChannel() {
		return tsTextChannel;
	}
	
	public int getChannelID() {
		return tsChannelID;
	}
}
