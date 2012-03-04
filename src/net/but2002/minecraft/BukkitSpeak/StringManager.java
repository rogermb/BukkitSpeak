package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.util.ConfigReader;

public class StringManager {
	
	private HashMap<String,String> strings = new HashMap<String,String>();
	private String ip, serverAdmin, serverPass;
	private int queryPort,serverPort;
	
	public static final String CONFIG_SECTION = "General";
	public static final String CONFIG_IP = "TeamSpeakIp";
	public static final String CONFIG_SERVERPORT = "TeamSpeakPort";
	public static final String CONFIG_QUERYPORT = "QueryPort";
	public static final String CONFIG_SERVERADMIN = "QueryUsername";
	public static final String CONFIG_SERVERPASS = "QueryPassword";
	
	public static final String MESSAGES_SECTION = "Messages";
	public static final String[][] MESSAGES = {
		{"msg_join", "%name% has joined TeamSpeak"}, 
		{"msg_quit", "%name% has left TeamSpeak"}, 
		{"msg_servermsg", "[TS][%client_nickname%] %msg%"}, 
		{"msg_list", "Currently online: "}};
	
	public static final String MUTED_SECTION = "Muted";
	
	public StringManager(BukkitSpeak plugin) {
		
		ConfigReader reader = new ConfigReader(plugin);
		
		ip = reader.getString(CONFIG_SECTION, CONFIG_IP, "1.2.3.4");
		serverPort = reader.getInteger(CONFIG_SECTION, CONFIG_SERVERPORT, 9987);
		queryPort = reader.getInteger(CONFIG_SECTION, CONFIG_QUERYPORT, 10011);
		serverAdmin = reader.getString(CONFIG_SECTION, CONFIG_SERVERADMIN, "admin");
		serverPass = reader.getString(CONFIG_SECTION, CONFIG_SERVERPASS, "123456");
		
		for (String[] keyPair : MESSAGES) {
			try {
				String currentValue = reader.getString(MESSAGES_SECTION, keyPair[0], keyPair[1]);
				strings.put(keyPair[0], currentValue);
			} catch (Exception e) {
				plugin.getLogger().severe(plugin + "was unable to load all the messages. This is probably a programming error.");
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
	
	public int getQueryPort() {
		return queryPort;
	}
	
	public int getServerPort() {
		return serverPort;
	}
	
	public String getServerAdmin() {
		return serverAdmin;
	}
	
	public String getServerPass() {
		return serverPass;
	}
}
