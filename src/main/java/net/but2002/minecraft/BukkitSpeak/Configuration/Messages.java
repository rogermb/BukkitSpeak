package net.but2002.minecraft.BukkitSpeak.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Messages {
	
	TS_EVENT_SERVER_JOIN("messages.TeamspeakEvents.Join", "&e%client_nickname% &ahas joined TeamSpeak"),
	TS_EVENT_SERVER_QUIT("messages.TeamspeakEvents.Quit", "&e%client_nickname% &ahas left TeamSpeak"),
	TS_EVENT_CHANNEL_ENTER("messages.TeamspeakEvents.ChannelEnter", "&e%client_nickname% &aentered the channel."),
	TS_EVENT_CHANNEL_LEAVE("messages.TeamspeakEvents.ChannelLeave", "&e%client_nickname% &aleft the channel."),
	TS_EVENT_SERVER_MESSAGE("messages.TeamspeakEvents.ServerMsg", "[&cTS&f] &e%client_nickname%&a: %msg%"),
	TS_EVENT_CHANNEL_MESSAGE("messages.TeamspeakEvents.ChannelMsg", "&e%client_nickname%&f: %msg%"),
	TS_EVENT_PRIVATE_MESSAGE("messages.TeamspeakEvents.PrivateMsg", "&e%client_nickname% &a-> &eMe&f: %msg%"),
	
	MC_EVENT_CHAT("messages.MinecraftEvents.ChatMessage", "&l%player_displayname%&r: %msg%"),
	MC_EVENT_LOGIN("messages.MinecraftEvents.LoginMessage", "&l%player_displayname%&r logged in."),
	MC_EVENT_LOGOUT("messages.MinecraftEvents.LogoutMessage", "&l%player_displayname%&r logged out."),
	MC_EVENT_KICK("messages.MinecraftEvents.KickedMessage", "&l%player_displayname%&r was kicked from the server."),
	MC_EVENT_BAN("messages.MinecraftEvents.BannedMessage", "&l%player_displayname%&r was banned from the server."),
	
	MC_COMMAND_LIST_SERVER("messages.MinecraftCommandMessages.OnlineList", "&aCurrently online: &e%list%"),
	MC_COMMAND_LIST_CHANNEL("messages.MinecraftCommandMessages.ChannelList", "&aCurrently in the channel: &e%list%"),
	MC_COMMAND_MUTE("messages.MinecraftCommandMessages.Mute", "&aYou are now muted."),
	MC_COMMAND_UNMUTE("messages.MinecraftCommandMessages.Unmute", "&aYou aren't muted anymore."),
	MC_COMMAND_CHANNEL_CHANGE("messages.MinecraftCommandMessages.",
			"&aYou are now talking in the TeamSpeak channel &6%channel%&a."),
	
	MC_COMMAND_BROADCAST_MC("messages.MinecraftCommandMessages.Broadcast",
			"&e%player_displayname% &a-> &f[&cTS&f]&f: %msg%"),
	MC_COMMAND_BROADCAST_TS("messages.TeamspeakMessages.ServerMessage", "&4&l%msg%"),
	MC_COMMAND_CHAT_MC("messages.MinecraftCommandMessages.Chat", "&e%player_displayname% &a-> &eTS&f: %msg%"),
	MC_COMMAND_CHAT_TS("messages.TeamspeakMessages.ChannelMessage", "&4&l[%player_displayname%&4] &r%msg%"),
	MC_COMMAND_PM_MC("messages.MinecraftCommandMessages.Pm", "&eMe &a-> &e%target%&f: %msg%"),
	MC_COMMAND_PM_TS("messages.TeamspeakMessages.PrivateMessage", "&4&l[%player_displayname%&4] &r%msg%"),
	MC_COMMAND_POKE_MC("messages.MinecraftCommandMessages.Poke", "&eYou &apoked &e%target%&f: %msg%"),
	MC_COMMAND_POKE_TS("messages.TeamspeakMessages.PokeMessage", "&l[%player_displayname%] &r%msg%"),
	MC_COMMAND_KICK_MC("messages.MinecraftCommandMessages.Kick",
			"&e%player_displayname% &akicked &e%target% &afrom the server for &e%msg%&a."),
	MC_COMMAND_KICK_TS("messages.TeamspeakMessages.KickMessage",
			"[%player_displayname%] kicked you from the server for %msg%."),
	MC_COMMAND_CHANNEL_KICK_MC("messages.MinecraftCommandMessages.ChannelKick",
			"&e%player_displayname% &akicked &e%target% &afrom the channel for &e%msg%&a."),
	MC_COMMAND_CHANNEL_KICK_TS("messages.TeamspeakMessages.ChannelKickMessage",
			"[%player_displayname%] kicked you from the server for %msg%."),
	MC_COMMAND_BAN_MC("messages.MinecraftCommandMessages.Ban",
			"&e%player_displayname% &abanned &e%target% &afor &e%msg%&a."),
	MC_COMMAND_BAN_TS("messages.TeamspeakMessages.BanMessage",
			"[%player_displayname%] banned you from the server for %msg%."),
	
	TS_COMMAND_NOT_WHITELISTED("messages.TeamspeakCommandMessages.PluginNotWhitelisted",
			"You are not allowed to run commands of that plugin."),
	TS_COMMAND_BLACKLISTED("messages.TeamspeakCommandMessages.CommandBlacklisted",
			"The command you are trying to run is blacklisted."),
	TS_COMMAND_SENDER_NAME("messages.TeamspeakCommandMessages.TeamspeakCommandSenderName",
			"&a[&6TS&a] &e%client_nickname%&r");
	
	private static final File CONFIG_FILE = new File(BukkitSpeak.getInstance().getDataFolder(), "locale.yml");
	private static YamlConfiguration config;
	
	private final String path;
	private final String[] oldPaths;
	private final String defValue;
	
	Messages(String configPath, String defaultValue) {
		this(configPath, defaultValue, null);
	}
	
	Messages(String configPath, String defaultValue, String[] oldConfigPaths) {
		path = configPath;
		oldPaths = oldConfigPaths;
		defValue = defaultValue;
	}
	
	public static void reload() {
		boolean changed = false;
		config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
		
		if (config.getKeys(false).isEmpty()) {
			if (Configuration.getConfig().isConfigurationSection("messages")) {
				config.set("messages", Configuration.getConfig().getConfigurationSection("messages"));
				Configuration.getConfig().set("messages", null);
				BukkitSpeak.log().info("Moved the messages section from the config into the locale file.");
				changed = true;
			} else {
				BukkitSpeak.getInstance().saveResource("locale.yml", false);
				BukkitSpeak.log().info("Default locale file created!");
				config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
			}
		}
		
		ValueIteration: for (Messages value : Messages.values()) {
			if (value.defValue == null) continue;
			Object val = config.get(value.path);
			
			if (val == null) {
				if (value.oldPaths != null) {
					for (String oldPath : value.oldPaths) {
						Object oldVal;
						if (oldPath.startsWith("C:")) {
							oldVal = Configuration.getConfig().get(oldPath);
						} else {
							oldVal = config.get(oldPath);
						}
						if (oldVal != null && oldVal.getClass().isInstance(value.defValue.getClass())) {
							config.set(value.path, oldVal);
							config.set(oldPath, null);
							BukkitSpeak.log().warning("Moved \"" + oldPath + "\" to \"" + value.path + "\".");
							changed = true;
							continue ValueIteration;
						}
					}
				}
				
				value.setToDefault();
				BukkitSpeak.log().warning("Config value \"" + value.path + "\" was not set, changed it to \""
						+ String.valueOf(value.defValue) + "\".");
				changed = true;
			} else if (val.getClass().isInstance(value.defValue.getClass())) {
				value.setToDefault();
				BukkitSpeak.log().warning("Config value \"" + value.path + "\" was not of type "
						+ value.defValue.getClass().getSimpleName() + ", changed it to \""
						+ String.valueOf(value.defValue) + "\".");
				changed = true;
			}
		}
		
		if (changed) save();
	}
	
	public static void save() {
		if (config == null) return;
		try {
			config.save(CONFIG_FILE);
		} catch (IOException e) {
			BukkitSpeak.log().log(Level.SEVERE, "Could not save the locale file to " + CONFIG_FILE, e);
		}
	}
	
	public static FileConfiguration getConfig() {
		return config;
	}
	
	public String getConfigPath() {
		return path;
	}
	
	public String getDefaultValue() {
		return defValue;
	}
	
	public String get() {
		return config.getString(path, defValue);
	}
	
	public void set(String value) {
		config.set(path, value);
	}
	
	public void setToDefault() {
		config.set(path, defValue);
	}
}
