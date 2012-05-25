package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import org.bukkit.configuration.MemoryConfiguration;

import net.but2002.minecraft.BukkitSpeak.util.ConfigReader;

public class StringManager {
	
	private HashMap<String,String> strings = new HashMap<String,String>();
	private String ip, serverAdmin, serverPass, tsName, tsConsoleName, tsChannelPass, tsDefaultReason;
	private int queryPort, serverPort, tsChannelID, tsTarget;
	private Boolean tsServer, tsTextServer, tsChannel, tsTextChannel, tsPrivateMessages, tsAllowLinks;
	
	public static final String CONFIG_SECTION = "main";
	public static final String CONFIG_IP = "TeamSpeakIp";
	public static final String CONFIG_SERVERPORT = "TeamSpeakPort";
	public static final String CONFIG_QUERYPORT = "QueryPort";
	public static final String CONFIG_SERVERADMIN = "QueryUsername";
	public static final String CONFIG_SERVERPASS = "QueryPassword";
	
	public static final String TEAMSPEAK_SECTION = "teamspeak";
	public static final String TEAMSPEAK_NAME = "TeamspeakNickname";
	public static final String TEAMSPEAK_CONSOLENAME = "ConsoleName";
	public static final String TEAMSPEAK_CHANNELID = "ChannelID";
	public static final String TEAMSPEAK_CHANNELPW = "ChannelPassword";
	public static final String TEAMSPEAK_SERVER = "ListenToServerEvents";
	public static final String TEAMSPEAK_TEXTSERVER = "ListenToServerBroadcasts";
	public static final String TEAMSPEAK_CHANNEL = "ListenToChannel";
	public static final String TEAMSPEAK_TEXTCHANNEL = "ListenToChannelChat";
	public static final String TEAMSPEAK_PRIVATEMESSAGES = "ListenToPrivateMessages";
	public static final String TEAMSPEAK_ALLOWLINKS = "AllowLinksInMessages";
	public static final String TEAMSPEAK_TARGET = "SendChatToTeamspeak";
	public static final String TEAMSPEAK_DEFAULTREASON = "DefaultReason";
	public static final String[][] TEAMSPEAK_TARGETS = {
		{"none", "nobody", "null", "noting"},
		{"channel", "chat"},
		{"server", "broadcast"}};
	
	public static final String[] TEAMSPEAKEVENTMESSAGES_SECTION = {"messages", "TeamspeakEvents"};
	public static final String[] TEAMSPEAKMESSAGES_SECTION = {"messages", "TeamspeakMessages"};
	public static final String[] MINECRAFTEVENTMESSAGES_SECTION = {"messages", "MinecraftEvents"};
	public static final String[] COMMANDMESSAGES_SECTION = {"messages", "MinecraftCommandMessages"};
	
	// Used in BukkitSpeak.teamspeakEvent.*
	public static final String[][] TEAMSPEAKEVENTMESSAGES = {
		{"Join", "&e%client_nickname% &ahas joined TeamSpeak"}, 
		{"Quit", "&e%client_nickname% &ahas left TeamSpeak"}, 
		{"ChannelEnter","&e%client_nickname% &aentered the channel."},
		{"ChannelLeave","&e%client_nickname% &aleft the channel."},
		{"ServerMsg", "[&cTS&f] &e%client_nickname%&a: %msg%"}, 
		{"ChannelMsg", "&e%client_nickname%&f: %msg%"},
		{"PrivateMsg", "&e%client_nickname% &a-> &eMe&f: %msg%"}};
	
	// Used in BukkitSpeak.Commands.* for the tsMsg
	public static final String[][] TEAMSPEAKMESSAGES = {
		{"ServerMessage", "&4&l%msg%"},
		{"ChannelMessage", "&4&l[%player_displayname%&4] &r%msg%"},
		{"PrivateMessage", "&4&l[%player_displayname%&4] &r%msg%"},
		{"PokeMessage", "&4&l[%player_displayname%&4] &r&a%msg%"},
		{"KickMessage", "[%player_displayname%] kicked you from the server for %msg%."},
		{"ChannelKickMessage", "[%player_displayname%] kicked you from the server for %msg%."}};
	
	// Used in BukkitSpeak.ChatListener
	public static final String[][] MINECRAFTEVENTMESSAGES = {
		{"ChatMessage", "&l%player_displayname%&r: %msg%"},
		{"LoginMessage", ""},
		{"LogoutMessage", ""}};
	
	// Used in BukkitSpeak.Commands.* for the mcMsg
	public static final String[][] COMMANDMESSAGES = {
		{"OnlineList", "&aCurrently online: &e%list%"},
		{"ChannelList", "&aCurrently in the channel: &e%list%"},
		{"Mute", "&aYou are now muted."},
		{"Unmute", "&aYou aren't muted anymore."},
		{"Broadcast", "&e%player_displayname% &a-> &f[&cTS&f]&f: %msg%"},
		{"Chat", "&e%player_displayname% &a-> &eTS&f: %msg%"},
		{"Pm", "&eMe &a-> &e%target%&f: %msg%"},
		{"Poke", "&e%player_displayname% &apoked &e%target%&f: %msg%"},
		{"Kick", "&e%player_displayname% &akicked &e%target% &afrom the server for &e%msg%&a."},
		{"ChannelKick", "&e%player_displayname% &akicked &e%target% &afrom the channel for &e%msg%&a."}};
	
	public StringManager(BukkitSpeak plugin) {
		
		ConfigReader reader = new ConfigReader(plugin);
		
		if (plugin.getConfig().getKeys(true).size() == 0) {
			plugin.saveResource("config.yml", false);
			plugin.getLogger().info("Default config created!");
			plugin.reloadConfig();
		}
		plugin.getConfig().setDefaults(new MemoryConfiguration());
		
		ip = reader.getString(CONFIG_SECTION, CONFIG_IP, "1.2.3.4");
		serverPort = reader.getInteger(CONFIG_SECTION, CONFIG_SERVERPORT, 9987);
		queryPort = reader.getInteger(CONFIG_SECTION, CONFIG_QUERYPORT, 10011);
		serverAdmin = reader.getString(CONFIG_SECTION, CONFIG_SERVERADMIN, "admin");
		serverPass = reader.getString(CONFIG_SECTION, CONFIG_SERVERPASS, "123456");
		
		tsName = reader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_NAME, "Minecraft");
		tsConsoleName = reader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_CONSOLENAME, "&eServer");
		tsChannelID = reader.getInteger(TEAMSPEAK_SECTION, TEAMSPEAK_CHANNELID, 0);
		tsChannelPass = reader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_CHANNELPW, "");
		tsServer = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_SERVER, true);
		tsTextServer = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_TEXTSERVER, true);
		tsChannel = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_CHANNEL, true);
		tsTextChannel = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_TEXTCHANNEL, true);
		tsPrivateMessages = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_PRIVATEMESSAGES, true);
		tsAllowLinks = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_ALLOWLINKS, true);
		tsTarget = reader.getChoice(TEAMSPEAK_SECTION, TEAMSPEAK_TARGET, 0, TEAMSPEAK_TARGETS);
		tsDefaultReason = reader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_DEFAULTREASON, "-");
		
		for (String[] keyPair : TEAMSPEAKEVENTMESSAGES) {
			try {
				String currentValue = reader.getString(TEAMSPEAKEVENTMESSAGES_SECTION, keyPair[0], keyPair[1]);
				strings.put(keyPair[0], currentValue);
			} catch (Exception e) {
				plugin.getLogger().severe("Was unable to load all the messages. This is probably a programming error.");
			}
		}
		for (String[] keyPair : TEAMSPEAKMESSAGES) {
			try {
				String currentValue = reader.getString(TEAMSPEAKMESSAGES_SECTION, keyPair[0], keyPair[1]);
				strings.put(keyPair[0], currentValue);
			} catch (Exception e) {
				plugin.getLogger().severe("Was unable to load all the messages. This is probably a programming error.");
			}
		}
		for (String[] keyPair : MINECRAFTEVENTMESSAGES) {
			try {
				String currentValue = reader.getString(MINECRAFTEVENTMESSAGES_SECTION, keyPair[0], keyPair[1]);
				strings.put(keyPair[0], currentValue);
			} catch (Exception e) {
				plugin.getLogger().severe("Was unable to load all the messages. This is probably a programming error.");
			}
		}
		for (String[] keyPair : COMMANDMESSAGES) {
			try {
				String currentValue = reader.getString(COMMANDMESSAGES_SECTION, keyPair[0], keyPair[1]);
				strings.put(keyPair[0], currentValue);
			} catch (Exception e) {
				plugin.getLogger().severe("Was unable to load all the messages. This is probably a programming error.");
			}
		}
		
		if (reader.gotErrors()) plugin.saveConfig();
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
	
	public String getConsoleName() {
		return tsConsoleName;
	}
	
	public int getChannelID() {
		return tsChannelID;
	}
	
	public String getChannelPass() {
		return tsChannelPass;
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
	
	public Boolean getUsePrivateMessages() {
		return tsPrivateMessages;
	}
	
	public Boolean getAllowLinks() {
		return tsAllowLinks;
	}
	
	public TsTargetEnum getTeamspeakTarget() {
		return TsTargetEnum.values()[tsTarget];
	}
	
	public String getDefaultReason() {
		return tsDefaultReason;
	}
}
