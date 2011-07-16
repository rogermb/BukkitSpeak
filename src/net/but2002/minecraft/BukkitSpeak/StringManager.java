/**
 * 
 */
package net.but2002.minecraft.BukkitSpeak;


import java.util.HashMap;

import org.bukkit.util.config.Configuration;

/**
 * @author greycap
 *
 */
public class StringManager {
	
	private HashMap<String,String> strings = new HashMap<String,String>();
	private String ip;
        private String serverAdmin;
        private String serverPass;
	private int queryPort,serverPort;
	
	public static final String CONFIG_IP = "ip";
	public static final String CONFIG_QUERYPORT = "queryPort";
	public static final String CONFIG_SERVERPORT = "serverPort";
        public static final String CONFIG_SERVERADMIN = "serverAdmin";
        public static final String CONFIG_SERVERPASS = "serverPass";
	
	public static final String[] MESSAGES = {"msg_join","msg_quit","msg_servermsg"};
	
	private BukkitSpeak plugin;
	
	public StringManager(BukkitSpeak plugin){
		this.plugin = plugin;
		Configuration config = plugin.getConfiguration();
		boolean error = false;
		ip = config.getString(CONFIG_IP);
		if(ip == null){ //check if config file contains IP
			config.setProperty(CONFIG_IP, "1.2.3.4");
			logConfigError("IP");
			error = true;
		}
		
		String queryPort = config.getString(CONFIG_QUERYPORT);
		try {
			this.setQueryPort(Integer.parseInt(queryPort));
		} catch (Exception e) {
			config.setProperty(CONFIG_QUERYPORT, "10011");
			logConfigError(e.toString());
			error = true;
		}
		
		String serverPort = config.getString(CONFIG_SERVERPORT);
		try {
			this.setServerPort(Integer.parseInt(serverPort));
		} catch (Exception e) {
			config.setProperty(CONFIG_SERVERPORT, "9987");
			logConfigError(e.toString());
			error = true;
		}
                
                String serverAdmin = config.getString(CONFIG_SERVERADMIN);
                try {
                        this.setServerAdmin(serverAdmin);
                } catch (Exception e) {
			config.setProperty(CONFIG_SERVERADMIN, "serveradmin");
			logConfigError(e.toString());
			error = true;
		}
                
                String serverPass = config.getString(CONFIG_SERVERPASS);
                try {
                        this.setServerPass(serverPass);
                } catch (Exception e) {
			config.setProperty(CONFIG_SERVERPASS, "serverpass");
			logConfigError(e.toString());
			error = true;
		}
               
		
		for(String currentNode : MESSAGES){ //read all the messages
			String currentValue = config.getString(currentNode);
			if(currentValue == null){
				config.setProperty(currentNode, getDefaultMessage(currentNode));
				logConfigError(currentNode);
				error = true;
			}
			else{
				strings.put(currentNode, currentValue);
			}
		}
		if(error){
			config.save();
			plugin.disable();
		}
		
	}
	
	private void logConfigError(String message){
		plugin.getLogger().log(java.util.logging.Level.SEVERE,"["+plugin.getDescription().getName()+"] Error while parsing "+message);
	}
	
	private String getDefaultMessage(String node){
		if(node.equals("msg_join"))
			return "%name% has joined TS";
		else if (node.equals("msg_quit"))
			return "%name% has quit TS";
		else if (node.equals("msg_servermsg"))
			return "[TS][Server][%client_nickname%] %msg%";
		return "FIXME";
	}
	
	public String getMessage(String key){
		return strings.get(key);
	}
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param queryPort the queryPort to set
	 */
	public void setQueryPort(int queryPort) {
		this.queryPort = queryPort;
	}

	/**
	 * @return the queryPort
	 */
	public int getQueryPort() {
		return queryPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}
                
        public void setServerAdmin(String serverAdmin)
        {
            this.serverAdmin = serverAdmin;
        }
        
        public void setServerPass(String serverPass)
        {
            this.serverPass = serverPass;         
        }
        
        public String getServerAdmin()
        {
            return serverAdmin;
        }
        
        public String getServerPass()
        {
            return serverPass;
        }

}
