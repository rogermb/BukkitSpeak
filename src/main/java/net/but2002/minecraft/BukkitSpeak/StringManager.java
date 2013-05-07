package net.but2002.minecraft.BukkitSpeak;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventPriority;

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
	public static final String TEAMSPEAK_CHATPRIORITY = "ChatListenerPriority";
	public static final String TEAMSPEAK_DEBUG = "Debug";
	
	public static final String TS_COMMANDS_SECTION = "teamspeak-commands";
	public static final String TS_COMMANDS_ENABLED = "Enabled";
	public static final String TS_COMMANDS_COMMANDPREFIX = "CommandPrefix";
	public static final String TS_COMMANDS_NAMEPREFIX = "NamePrefix";
	public static final String TS_COMMANDS_LOGGING = "LogTeamspeakCommands";
	public static final String TS_COMMANDS_BUFFER = "MessageBufferDelay";
	
	public static final String[] FACTIONS_SECTION = {"plugin-interaction", "Factions"};
	public static final String FACTIONS_PUBLIC_ONLY = "public-only";
	
	public static final String[] HEROCHAT_SECTION = {"plugin-interaction", "Herochat"};
	public static final String HEROCHAT_ENABLED = "enabled";
	public static final String HEROCHAT_CHANNEL = "channel";
	public static final String HEROCHAT_EVENTS = "SendTeamspeakEventsToChannel";
	
	public static final String[] MCMMO_SECTION = {"plugin-interaction", "mcMMO"};
	public static final String MCMMO_PARTY_CHAT = "FilterPartyChat";
	public static final String MCMMO_ADMIN_CHAT = "FilterAdminChat";
	
	public static final String[] TEAMSPEAKEVENTMESSAGES_SECTION = {"messages", "TeamspeakEvents"};
	public static final String[] TEAMSPEAKMESSAGES_SECTION = {"messages", "TeamspeakMessages"};
	public static final String[] TEAMSPEAKCOMMANDMESSAGES_SECTION = {"messages", "TeamspeakCommandMessages"};
	public static final String[] MINECRAFTEVENTMESSAGES_SECTION = {"messages", "MinecraftEvents"};
	public static final String[] COMMANDMESSAGES_SECTION = {"messages", "MinecraftCommandMessages"};
	
	public static final String[][] TEAMSPEAK_TARGETS = {
		{"none", "nobody", "null", "noting"},
		{"channel", "chat"},
		{"server", "broadcast"}};
	
	public static final String[][] TS_COMMAND_TARGETS = {
		{"server", "broadcast"},
		{"channel", "chat"},
		{"pm", "private", "privatemessage", "client"}};
	
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
	
	// Used in BukkitSpeak.TeamspeakCommands
	public static final String[][] TEAMSPEAKCOMMANDMESSAGES = {
		{"PluginNotWhitelisted", "Unknown command. Type \"help\" for help."},
		{"CommandBlacklisted", "Unknown command. Type \"help\" for help."}};
	
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
		{"Poke", "&eYou &apoked &e%target%&f: %msg%"},
		{"Kick", "&e%player_displayname% &akicked &e%target% &afrom the server for &e%msg%&a."},
		{"ChannelKick", "&e%player_displayname% &akicked &e%target% &afrom the channel for &e%msg%&a."},
		{"Ban", "&e%player_displayname% &abanned &e%target% &afor &e%msg%&a."}};
	
	private HashMap<String, String> strings = new HashMap<String, String>();
	private boolean tsServer, tsTextServer, tsChannel, tsTextChannel, tsPrivateMessages, tsAllowLinks, tsConsole, tsDebug;
	private int queryPort, serverPort, tsChannelID, tsTarget;
	private String ip, serverAdmin, serverPass, tsName, tsConsoleName, tsChannelPass, tsDefaultReason;
	private EventPriority tsChatListenerPriority;
	
	private boolean factionsPublicOnly, herochatEnabled, herochatEvents, mcMMOParty, mcMMOAdmin;
	private String herochatChannel;
	
	private boolean tsCommands, tsCommandLogging;
	private String tsCommandPrefix, tsCommandNamePrefix;
	private int tsCommandSenderBuffer;
	
	private File localeFile;
	private FileConfiguration localeConfig;
	
	public StringManager() {
		
		BukkitSpeak.getInstance().reloadConfig();
		reloadLocale();
		
		ConfigReader configReader = new ConfigReader(BukkitSpeak.getInstance());
		ConfigReader localeReader = new ConfigReader(BukkitSpeak.getInstance(), localeConfig);
		
		if (configReader.isEmpty()) {
			BukkitSpeak.getInstance().saveResource("config.yml", false);
			BukkitSpeak.log().info("Default config file created!");
			BukkitSpeak.getInstance().reloadConfig();
		}
		if (localeReader.isEmpty()) {
			if (configReader.contains("messages")) {
				localeConfig.set("messages", configReader.getConfigSection("messages", null));
				configReader.getConfig().set("messages", null);
				BukkitSpeak.log().info("Moved the messages section from the config into the locale file.");
				saveLocale();
				BukkitSpeak.getInstance().saveConfig();
			} else {
				BukkitSpeak.getInstance().saveResource("locale.yml", false);
				BukkitSpeak.log().info("Default locale file created!");
			}
			reloadLocale();
			localeReader = new ConfigReader(BukkitSpeak.getInstance(), localeConfig);
		}
		BukkitSpeak.getInstance().getConfig().setDefaults(new MemoryConfiguration());
		
		ip = configReader.getString(CONFIG_SECTION, CONFIG_IP, "1.2.3.4");
		serverPort = configReader.getInteger(CONFIG_SECTION, CONFIG_SERVERPORT, 9987);
		queryPort = configReader.getInteger(CONFIG_SECTION, CONFIG_QUERYPORT, 10011);
		serverAdmin = configReader.getString(CONFIG_SECTION, CONFIG_SERVERADMIN, "admin");
		serverPass = configReader.getString(CONFIG_SECTION, CONFIG_SERVERPASS, "123456");
		
		tsName = configReader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_NAME, "Minecraft");
		tsConsoleName = configReader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_CONSOLENAME, "&eServer");
		tsChannelID = configReader.getInteger(TEAMSPEAK_SECTION, TEAMSPEAK_CHANNELID, 0);
		tsChannelPass = configReader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_CHANNELPW, "");
		tsServer = configReader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_SERVER, true);
		tsTextServer = configReader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_TEXTSERVER, true);
		tsChannel = configReader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_CHANNEL, true);
		tsTextChannel = configReader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_TEXTCHANNEL, true);
		tsPrivateMessages = configReader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_PRIVATEMESSAGES, true);
		tsAllowLinks = configReader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_ALLOWLINKS, true);
		tsTarget = configReader.getChoice(TEAMSPEAK_SECTION, TEAMSPEAK_TARGET, 0, TEAMSPEAK_TARGETS);
		tsConsole = configReader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_CONSOLE, true);
		tsDefaultReason = configReader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_DEFAULTREASON, "-");
		tsChatListenerPriority = eventPriority(configReader.getString(TEAMSPEAK_SECTION, TEAMSPEAK_CHATPRIORITY, "MONITOR"));
		tsDebug = configReader.getBoolean(TEAMSPEAK_SECTION, TEAMSPEAK_DEBUG, false);
		
		tsCommands = configReader.getBoolean(TS_COMMANDS_SECTION, TS_COMMANDS_ENABLED, false);
		tsCommandPrefix = configReader.getString(TS_COMMANDS_SECTION, TS_COMMANDS_COMMANDPREFIX, "!");
		tsCommandNamePrefix = configReader.getString(TS_COMMANDS_SECTION, TS_COMMANDS_NAMEPREFIX, "TS: ");
		tsCommandLogging = configReader.getBoolean(TS_COMMANDS_SECTION, TS_COMMANDS_LOGGING, true);
		tsCommandSenderBuffer = configReader.getInteger(TS_COMMANDS_SECTION, TS_COMMANDS_BUFFER, 50);
		if (tsCommandSenderBuffer < 1) {
			tsCommandSenderBuffer = 50;
		}
		
		factionsPublicOnly = configReader.getBoolean(FACTIONS_SECTION, FACTIONS_PUBLIC_ONLY, true);
		herochatEnabled = configReader.getBoolean(HEROCHAT_SECTION, HEROCHAT_ENABLED, false);
		herochatChannel = configReader.getString(HEROCHAT_SECTION, HEROCHAT_CHANNEL, "Global");
		herochatEvents = configReader.getBoolean(HEROCHAT_SECTION, HEROCHAT_EVENTS, false);
		mcMMOParty = configReader.getBoolean(MCMMO_SECTION, MCMMO_PARTY_CHAT, true);
		mcMMOAdmin = configReader.getBoolean(MCMMO_SECTION, MCMMO_ADMIN_CHAT, true);
		
		for (String[] keyPair : TEAMSPEAKEVENTMESSAGES) {
			String currentValue = localeReader.getString(TEAMSPEAKEVENTMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		for (String[] keyPair : TEAMSPEAKMESSAGES) {
			String currentValue = localeReader.getString(TEAMSPEAKMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		for (String[] keyPair : TEAMSPEAKCOMMANDMESSAGES) {
			String currentValue = localeReader.getString(TEAMSPEAKCOMMANDMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		for (String[] keyPair : MINECRAFTEVENTMESSAGES) {
			String currentValue = localeReader.getString(MINECRAFTEVENTMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		for (String[] keyPair : COMMANDMESSAGES) {
			String currentValue = localeReader.getString(COMMANDMESSAGES_SECTION, keyPair[0], keyPair[1]);
			strings.put(keyPair[0], currentValue);
		}
		
		if (configReader.gotErrors()) BukkitSpeak.getInstance().saveConfig();
		if (localeReader.gotErrors()) saveLocale();
	}
	
	private EventPriority eventPriority(String name) {
		for (EventPriority val : EventPriority.values()) {
			if (val.name().equalsIgnoreCase(name)) return val;
		}
		BukkitSpeak.log().severe("Error while parsing " + TEAMSPEAK_SECTION + "." + TEAMSPEAK_CHATPRIORITY);
		BukkitSpeak.getInstance().getConfig().set(TEAMSPEAK_SECTION + "." + TEAMSPEAK_CHATPRIORITY, "MONITOR");
		return EventPriority.MONITOR;
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
	
	public EventPriority getChatListenerPriority() {
		return tsChatListenerPriority;
	}
	
	public boolean getDebugMode() {
		return tsDebug;
	}
	
	public boolean getTeamspeakCommandsEnabled() {
		return tsCommands;
	}
	
	public String getTeamspeakCommandPrefix() {
		return tsCommandPrefix;
	}
	
	public String getTeamspeakNamePrefix() {
		return tsCommandNamePrefix;
	}
	
	public boolean getTeamspeakCommandLoggingEnabled() {
		return tsCommandLogging;
	}
	
	public int getTeamspeakCommandSenderBuffer() {
		return tsCommandSenderBuffer;
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
	
	public boolean getHerochatUsesEvents() {
		return herochatEvents;
	}
	
	public boolean getMcMMOFilterPartyChat() {
		return mcMMOParty;
	}
	
	public boolean getMcMMOFilterAdminChat() {
		return mcMMOAdmin;
	}
	
	public void reloadLocale() {
		if (localeFile == null) {
			localeFile = new File(BukkitSpeak.getInstance().getDataFolder(), "locale.yml");
		}
		localeConfig = YamlConfiguration.loadConfiguration(localeFile);
	}
	
	public void saveLocale() {
		if ((localeFile == null) || (localeConfig == null)) return;
		try {
			localeConfig.save(localeFile);
		} catch (IOException e) {
			BukkitSpeak.log().log(Level.SEVERE, "Could not save the locale file to " + localeFile, e);
		}
	}
}
