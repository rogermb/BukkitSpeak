package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import org.bukkit.configuration.MemoryConfiguration;

import net.but2002.minecraft.BukkitSpeak.util.ConfigReader;

public class StringManager {
	
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
	public static final String TEAMSPEAK_CONSOLE = "LogChatInConsole";
	public static final String TEAMSPEAK_DEFAULTREASON = "DefaultReason";
	public static final String TEAMSPEAK_DEBUG = "Debug";
	public static final String[][] TEAMSPEAK_TARGETS = {
		{"none", "nobody", "null", "noting"},
		{"channel", "chat"},
		{"server", "broadcast"}};
	
	public static final String[] FACTIONS_SECTION = {"plugin-interaction", "Factions"};
	public static final String FACTIONS_PUBLIC_ONLY = "public-only";
	
	public static final String[] HEROCHAT_SECTION = {"plugin-interaction", "Herochat"};
	public static final String HEROCHAT_ENABLED = "enabled";
	public static final String HEROCHAT_CHANNEL = "channel";
	
	public static final String[] MCMMO_SECTION = {"plugin-interaction", "mcMMO"};
	public static final String MCMMO_PARTY_CHAT = "FilterPartyChat";
	public static final String MCMMO_ADMIN_CHAT = "FilterAdminChat";
	
	public static final String[] TEAMSPEAKEVENTMESSAGES_SECTION = {"messages", "TeamspeakEvents"};
	public static final String[] TEAMSPEAKMESSAGES_SECTION = {"messages", "TeamspeakMessages"};
	public static final String[] MINECRAFTEVENTMESSAGES_SECTION = {"messages", "MinecraftEvents"};
	public static final String[] COMMANDMESSAGES_SECTION = {"messages", "MinecraftCommandMessages"};
	
	// Used in BukkitSpeak.teamspeakEvent.*
	public static final String[][] TEAMSPEAKEVENTMESSAGES = {
		{"Join", "&e%client_nickname% &ahas joined TeamSpeak"},
		{"Quit", "&e%client_nickname% &ahas left TeamSpeak"},
		{"ChannelEnter", "&e%client_nickname% &aentered the channel."},
		{"ChannelLeave", "&e%client_nickname% &aleft the channel."},
		{"ServerMsg", "[&cTS&f] &e%client_nickname%&a: %msg%"},
		{"ChannelMsg", "&e%client_nickname%&f: %msg%"},
		{"PrivateMsg", "&e%client_nickname% &a-> &eMe&f: %msg%"}};
	
	// Used in BukkitSpeak.Commands.* for the tsMsg
	public static final String[][] TEAMSPEAKMESSAGES = {
		{"ServerMessage", "&4&l%msg%"},
		{"ChannelMessage", "&4&l[%player_displayname%&4] &r%msg%"},
		{"PrivateMessage", "&4&l[%player_displayname%&4] &r%msg%"},
		{"PokeMessage", "&l[%player_displayname%] &r%msg%"},
		{"KickMessage", "[%player_displayname%] kicked you from the server for %msg%."},
		{"ChannelKickMessage", "[%player_displayname%] kicked you from the server for %msg%."},
		{"BanMessage", "[%player_displayname%] banned you from the server for %msg%."}};
	
	// Used in BukkitSpeak.ChatListener
	public static final String[][] MINECRAFTEVENTMESSAGES = {
		{"ChatMessage", "&l%player_displayname%&r: %msg%"},
		{"LoginMessage", "&l%player_displayname%&r logged in."},
		{"LogoutMessage", "&l%player_displayname%&r logged out."},
		{"KickedMessage", "&l%player_displayname%&r was kicked from the server."},
		{"BannedMessage", "&l%player_displayname%&r was banned from the server."}};
	
	// Used in BukkitSpeak.Commands.* for the mcMsg
	public static final String[][] COMMANDMESSAGES = {
		{"OnlineList", "&aCurrently online: &e%list%"},
		{"ChannelList", "&aCurrently in the channel: &e%list%"},
		{"Mute", "&aYou are now muted."},
		{"Unmute", "&aYou aren't muted anymore."},
		{"ChannelChange", "&aYou are now talking in the TeamSpeak channel &6%channel%&a."},
		{"Broadcast", "&e%player_displayname% &a-> &f[&cTS&f]&f: %msg%"},
		{"Chat", "&e%player_displayname% &a-> &eTS&f: %msg%"},
		{"Pm", "&eMe &a-> &e%target%&f: %msg%"},
		{"Poke", "&e%player_displayname% &apoked &e%target%&f: %msg%"},
		{"Kick", "&e%player_displayname% &akicked &e%target% &afrom the server for &e%msg%&a."},
		{"ChannelKick", "&e%player_displayname% &akicked &e%target% &afrom the channel for &e%msg%&a."},
		{"Ban", "&e%player_displayname% &abanned &e%target% &afor &e%msg%&a."}};
	
	private HashMap<String, String> strings = new HashMap<String, String>();
	private boolean tsServer, tsTextServer, tsChannel, tsTextChannel, tsPrivateMessages, tsAllowLinks, tsConsole, tsDebug;
	private int queryPort, serverPort, tsChannelID, tsTarget;
	private String ip, serverAdmin, serverPass, tsName, tsConsoleName, tsChannelPass, tsDefaultReason;
	
	private boolean factionsPublicOnly, herochatEnabled, mcMMOParty, mcMMOAdmin;
	private String herochatChannel;
	
	public StringManager() {
		
		ConfigReader reader = new ConfigReader(BukkitSpeak.getInstance());
		
		if (BukkitSpeak.getInstance().getConfig().getKeys(true).size() == 0) {
			BukkitSpeak.getInstance().saveResource("config.yml", false);
			BukkitSpeak.log().info("Default config created!");
			BukkitSpeak.getInstance().reloadConfig();
		}
		BukkitSpeak.getInstance().getConfig().setDefaults(new MemoryConfiguration());
		
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
		tsConsole = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_CONSOLE, true);
		tsDefaultReason = reader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_DEFAULTREASON, "-");
		tsDebug = reader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_DEBUG, false);
		
		factionsPublicOnly = reader.getBoolean(FACTIONS_SECTION, FACTIONS_PUBLIC_ONLY, true);
		herochatEnabled = reader.getBoolean(HEROCHAT_SECTION, HEROCHAT_ENABLED, false);
		herochatChannel = reader.getString(HEROCHAT_SECTION, HEROCHAT_CHANNEL, "Global");
		mcMMOParty = reader.getBoolean(MCMMO_SECTION, MCMMO_PARTY_CHAT, true);
		mcMMOAdmin = reader.getBoolean(MCMMO_SECTION, MCMMO_ADMIN_CHAT, true);
		
		for (String[] keyPair : TEAMSPEAKEVENTMESSAGES) {
			String currentValue = reader.getString(TEAMSPEAKEVENTMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		for (String[] keyPair : TEAMSPEAKMESSAGES) {
			String currentValue = reader.getString(TEAMSPEAKMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		for (String[] keyPair : MINECRAFTEVENTMESSAGES) {
			String currentValue = reader.getString(MINECRAFTEVENTMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		for (String[] keyPair : COMMANDMESSAGES) {
			String currentValue = reader.getString(COMMANDMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		
		if (reader.gotErrors()) BukkitSpeak.getInstance().saveConfig();
	}
	
	public String getMessage(String key) {
		return strings.get(key);
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
	
	public boolean getUseServer() {
		return tsServer;
	}
	
	public boolean getUseTextServer() {
		return tsTextServer;
	}
	
	public boolean getUseChannel() {
		return tsChannel;
	}
	
	public boolean getUseTextChannel() {
		return tsTextChannel;
	}
	
	public boolean getUsePrivateMessages() {
		return tsPrivateMessages;
	}
	
	public boolean getAllowLinks() {
		return tsAllowLinks;
	}
	
	public TsTargetEnum getTeamspeakTarget() {
		return TsTargetEnum.values()[tsTarget];
	}
	
	public boolean getLogInConsole() {
		return tsConsole;
	}
	
	public String getDefaultReason() {
		return tsDefaultReason;
	}
	
	public boolean getDebugMode() {
		return tsDebug;
	}
	
	public boolean getFactionsPublicOnly() {
		return factionsPublicOnly;
	}
	
	public boolean getHerochatEnabled() {
		return herochatEnabled;
	}
	
	public String getHerochatChannel() {
		return herochatChannel;
	}
	
	public boolean getMcMMOFilterPartyChat() {
		return mcMMOParty;
	}
	
	public boolean getMcMMOFilterAdminChat() {
		return mcMMOAdmin;
	}
}
