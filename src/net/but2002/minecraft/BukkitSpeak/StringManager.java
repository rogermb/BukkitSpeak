package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

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
	public static final String[] MESSAGES = {"msg_join", "msg_quit", "msg_servermsg", "msg_list"};

	public static final String MUTED_SECTION = "Muted";
	
	private BukkitSpeak plugin;
	
	public StringManager(BukkitSpeak plugin) {
		
		this.plugin = plugin;
		FileConfiguration config = plugin.getConfig();
		boolean error = false;
		
		if (config.getValues(true).isEmpty()) {
			setDefaults(config);
			plugin.saveConfig();
		}
		
		try {
			config.getConfigurationSection(CONFIG_SECTION);
		} catch(Exception e) {
			config.createSection(CONFIG_SECTION);
		}
		
		try {
			config.getConfigurationSection(MESSAGES_SECTION);
		} catch(Exception e) {
			config.createSection(MESSAGES_SECTION);
		}

		try {
			ip = config.getConfigurationSection(CONFIG_SECTION).getString(CONFIG_IP);
		} catch(Exception e) {
			config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_IP, getDefault(CONFIG_IP));
			logConfigError(CONFIG_IP);
			error = true;
		}
		
		try {
			serverPort = Integer.parseInt(config.getConfigurationSection(CONFIG_SECTION).getString(CONFIG_SERVERPORT));
		} catch (Exception e) {
			config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_SERVERPORT, getDefault(CONFIG_SERVERPORT));
			logConfigError(CONFIG_SERVERPORT);
			error = true;
		}

		try {
			queryPort = Integer.parseInt(config.getConfigurationSection(CONFIG_SECTION).getString(CONFIG_QUERYPORT));
		} catch (Exception e) {
			config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_QUERYPORT, getDefault(CONFIG_QUERYPORT));
			logConfigError(CONFIG_QUERYPORT);
			error = true;
		}
		
		try {
			serverAdmin = config.getConfigurationSection(CONFIG_SECTION).getString(CONFIG_SERVERADMIN);
		} catch (Exception e) {
			config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_SERVERADMIN, getDefault(CONFIG_SERVERADMIN));
			logConfigError(CONFIG_SERVERADMIN);
			error = true;
		}
                
		try {
			serverAdmin = config.getConfigurationSection(CONFIG_SECTION).getString(CONFIG_SERVERPASS);
		} catch (Exception e) {
			config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_SERVERPASS, getDefault(CONFIG_SERVERPASS));
			logConfigError(CONFIG_SERVERPASS);
			error = true;
		}
		
		
		for (String currentNode : MESSAGES) { 
			String currentValue = config.getConfigurationSection(MESSAGES_SECTION).getString(currentNode);
			if (currentValue == null) {
				config.getConfigurationSection(MESSAGES_SECTION).set(currentNode, getDefault(currentNode));
				logConfigError(currentNode);
				error = true;
			} else {
				strings.put(currentNode, currentValue);
			}
		}
		
		if(error){
			plugin.saveConfig();
			plugin.disable();
		}
		
	}
	
	private void logConfigError(String message){
		plugin.getLogger().severe(plugin + "Error while parsing " + message);
	}
	
	private void setDefaults(FileConfiguration config) {
		config.createSection(CONFIG_SECTION);
		config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_IP, "1.2.3.4");
		config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_SERVERPORT, "9987");
		config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_QUERYPORT, "10011");
		config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_SERVERADMIN, "ServerAdmin");
		config.getConfigurationSection(CONFIG_SECTION).set(CONFIG_SERVERPASS, "ServerPass");
		config.createSection(MESSAGES_SECTION);
		config.getConfigurationSection(MESSAGES_SECTION).set(MESSAGES[0], "%name% has joined TeamSpeak");
		config.getConfigurationSection(MESSAGES_SECTION).set(MESSAGES[1], "%name% has left TeamSpeak");
		config.getConfigurationSection(MESSAGES_SECTION).set(MESSAGES[2], "[TS][%client_nickname%] %msg%");
	}
	
	private String getDefault(String node) {
		if(node.equals(CONFIG_IP)) return "1.2.3.4";
		else if (node.equals(CONFIG_SERVERPORT)) return "9987";
		else if (node.equals(CONFIG_QUERYPORT)) return "10011";
		else if (node.equals(CONFIG_SERVERADMIN)) return "ServerAdmin";
		else if (node.equals(CONFIG_SERVERPASS)) return "ServerPass";
		else if (node.equals(MESSAGES[0])) return "%name% has joined TeamSpeak";
		else if (node.equals(MESSAGES[1])) return "%name% has quit TeamSpeak";
		else if (node.equals(MESSAGES[2])) return "[TS][%client_nickname%] %msg%";
		else return "NO_DEFAULT_SET";
	}
	
	public String getMessage(String key){
		return strings.get(key);
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
