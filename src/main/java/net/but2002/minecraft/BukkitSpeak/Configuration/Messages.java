package net.but2002.minecraft.BukkitSpeak.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Messages {

	// Teamspeak events
	TS_EVENT_SERVER_JOIN("TeamspeakEvent.ServerJoin.ToMinecraftServer",
			"&e%client_nickname% &ahas joined TeamSpeak",
			new String[] {"messages.TeamspeakEvents.Join"}),

	TS_EVENT_SERVER_QUIT("TeamspeakEvent.ServerQuit.ToMinecraftServer",
			"&e%client_nickname% &ahas left TeamSpeak",
			new String[] {"messages.TeamspeakEvents.Quit"}),

	TS_EVENT_CHANNEL_ENTER("TeamspeakEvent.ChannelEnter.ToMinecraftServer",
			"&e%client_nickname% &aentered the channel.",
			new String[] {"messages.TeamspeakEvents.ChannelEnter"}),

	TS_EVENT_CHANNEL_LEAVE("TeamspeakEvent.ChannelLeave.ToMinecraftServer",
			"&e%client_nickname% &aleft the channel.",
			new String[] {"messages.TeamspeakEvents.ChannelLeave"}),

	TS_EVENT_SERVER_MESSAGE("TeamspeakEvent.ServerMessage.ToMinecraftServer",
			"[&cTS&f] &e%client_nickname%&a: %msg%",
			new String[] {"messages.TeamspeakEvents.ServerMsg"}),

	TS_EVENT_CHANNEL_MESSAGE("TeamspeakEvent.ChannelMessage.ToMinecraftServer",
			"&e%client_nickname%&f: %msg%",
			new String[] {"messages.TeamspeakEvents.ChannelMsg"}),

	TS_EVENT_PRIVATE_MESSAGE("TeamspeakEvent.PrivateMessage.ToMinecraftUser",
			"&e%client_nickname% &a-> &eMe&f: %msg%",
			new String[] {"messages.TeamspeakEvents.PrivateMsg"}),

	TS_EVENT_PRIVATE_MESSAGE_NO_CONVERSATION("TeamspeakEvent.PrivateMessage.Errors.NotInConversation.ToTeamspeakUser",
			"&4You're currently not in a private message conversation."),

	TS_EVENT_PRIVATE_MESSAGE_RECIPIENT_OFFLINE("TeamspeakEvent.PrivateMessage.Errors.PmRecipientNotOnline.ToTeamspeakUser",
			"&4The user you're trying to send a message to is offline."),

	TS_EVENT_PRIVATE_MESSAGE_RECIPIENT_MUTED("TeamspeakEvent.PrivateMessage.Errors.PmRecipientMutedOrNoPermission.ToTeamspeakUser",
			"&4The user &l%player_displayname%&r&4 can't receive your message."),

	// Minecraft events
	MC_EVENT_CHAT("MinecraftEvent.PlayerChat.ToTeamspeakTarget",
			"&l%player_displayname%&r: %msg%",
			new String[] {"messages.MinecraftEvents.ChatMessage"}),

	MC_EVENT_LOGIN("MinecraftEvent.PlayerLogin.ToTeamspeakTarget",
			"&l%player_displayname%&r logged in.",
			new String[] {"messages.MinecraftEvents.LoginMessage"}),

	MC_EVENT_LOGOUT("MinecraftEvent.PlayerLogout.ToTeamspeakTarget",
			"&l%player_displayname%&r logged out.",
			new String[] {"messages.MinecraftEvents.LogoutMessage"}),

	MC_EVENT_KICK("MinecraftEvent.PlayerKicked.ToTeamspeakTarget",
			"&l%player_displayname%&r was kicked from the server.",
			new String[] {"messages.MinecraftEvents.KickedMessage"}),

	MC_EVENT_BAN("MinecraftEvent.PlayerBanned.ToTeamspeakTarget",
			"&l%player_displayname%&r was banned from the server.",
			new String[] {"messages.MinecraftEvents.BannedMessage"}),

	// Minecraft commands
	MC_COMMAND_LIST_SERVER("MinecraftCommand.List.Server.ToMinecraftUser",
			"&aCurrently online (&e%count%&a): &e%list%",
			new String[] {"messages.MinecraftCommandMessages.OnlineList"}),

	MC_COMMAND_LIST_CHANNEL("MinecraftCommand.List.Channel.ToMinecraftUser",
			"&aCurrently in the channel (&e%count%&a): &e%list%",
			new String[] {"messages.MinecraftCommandMessages.ChannelList"}),

	MC_COMMAND_MUTE("MinecraftCommand.Mute.Enable.ToMinecraftUser",
			"&aYou are now muted.",
			new String[] {"messages.MinecraftCommandMessages.Mute"}),

	MC_COMMAND_UNMUTE("MinecraftCommand.Mute.Disable.ToMinecraftUser",
			"&aYou aren't muted anymore.",
			new String[] {"messages.MinecraftCommandMessages.Unmute"}),

	MC_COMMAND_CHANNEL_CHANGE("MinecraftCommand.Set.ChannelChange.ToMinecraftServer",
			"&aYou are now talking in the TeamSpeak channel &6%channel%&a.",
			new String[] {"messages.MinecraftCommandMessages.ChannelChange"}),

	MC_COMMAND_BROADCAST_MC("MinecraftCommand.Broadcast.ToMinecraftServer",
			"&e%player_displayname% &a-> &f[&cTS&f]&f: %msg%",
			new String[] {"messages.MinecraftCommandMessages.Broadcast"}),
	MC_COMMAND_BROADCAST_TS("MinecraftCommand.Broadcast.ToTeamspeakServer",
			"&4&l%msg%",
			new String[] {"messages.TeamspeakMessages.ServerMessage"}),

	MC_COMMAND_CHAT_MC("MinecraftCommand.Chat.ToMinecraftServer",
			"&e%player_displayname% &a-> &eTS&f: %msg%",
			new String[] {"messages.MinecraftCommandMessages.Chat"}),

	MC_COMMAND_CHAT_TS("MinecraftCommand.Chat.ToTeamspeakChannel",
			"&l[%player_displayname%&r&l] &r%msg%",
			new String[] {"messages.TeamspeakMessages.ChannelMessage"}),

	MC_COMMAND_PM_MC("MinecraftCommand.Pm.ToMinecraftUser",
			"&eMe &a-> &e%target%&f: %msg%",
			new String[] {"messages.MinecraftCommandMessages.Pm"}),

	MC_COMMAND_PM_TS("MinecraftCommand.Pm.ToTeamspeakUser",
			"&l[%player_displayname%&r&l] &r%msg%",
			new String[] {"messages.TeamspeakMessages.PrivateMessage"}),

	MC_COMMAND_REPLY_NO_RECIPIENT("MinecraftCommand.Reply.Errors.NoRecipient.ToMinecraftUser",
			"&4Nobody has sent you a PM yet."),

	MC_COMMAND_POKE_MC("MinecraftCommand.Poke.ToMinecraftUser",
			"&eYou &apoked &e%target%&f: %msg%",
			new String[] {"messages.MinecraftCommandMessages.Poke"}),

	MC_COMMAND_POKE_TS("MinecraftCommand.Poke.ToTeamspeakUser",
			"&l[%player_displayname%&r&l] &r%msg%",
			new String[] {"messages.TeamspeakMessages.PokeMessage"}),

	MC_COMMAND_KICK_MC("MinecraftCommand.Kick.ToMinecraftServer",
			"&e%player_displayname% &akicked &e%target% &afrom the server for &e%msg%&a.",
			new String[] {"messages.MinecraftCommandMessages.Kick"}),

	MC_COMMAND_KICK_TS("MinecraftCommand.Kick.ToTeamspeakUser",
			"[%player_displayname%] kicked you from the server for %msg%.",
			new String[] {"messages.TeamspeakMessages.KickMessage"}),

	MC_COMMAND_CHANNEL_KICK_MC("MinecraftCommand.ChannelKick.ToMinecraftServer",
			"&e%player_displayname% &akicked &e%target% &afrom the channel for &e%msg%&a.",
			new String[] {"messages.MinecraftCommandMessages.ChannelKick"}),

	MC_COMMAND_CHANNEL_KICK_TS("MinecraftCommand.ChannelKick.ToTeamspeakUser",
			"[%player_displayname%] kicked you from the server for %msg%.",
			new String[] {"messages.TeamspeakMessages.ChannelKickMessage"}),

	MC_COMMAND_CHANNEL_KICK_NOT_IN_CHANNEL("MinecraftCommand.ChannelKick.Errors.ClientNotInChannel.ToMinecraftUser",
			"&4The client &r&e%target%&r&4 is not in the channel!"),

	MC_COMMAND_BAN_MC("MinecraftCommand.Ban.ToMinecraftServer",
			"&e%player_displayname% &abanned &e%target% &afor &e%msg%&a.",
			new String[] {"messages.MinecraftCommandMessages.Ban"}),

	MC_COMMAND_BAN_TS("MinecraftCommand.Ban.ToTeamspeakUser",
			"[%player_displayname%] banned you from the server for %msg%.",
			new String[] {"messages.TeamspeakMessages.BanMessage"}),

	MC_COMMAND_DEFAULT_REASON("MinecraftCommand.KickBanDefaultReason",
			"-",
			new String[] {"c:teamspeak.DefaultReason"}),

	MC_COMMAND_ERROR_DISCONNECTED("MinecraftCommand.Errors.NotConnected.ToMinecraftUser",
			"&4Can't communicate with the TeamSpeak server."),

	MC_COMMAND_ERROR_NO_PLAYER_FOUND("MinecraftCommand.Errors.NoPlayerFound.ToMinecraftUser",
			"&4There is no player by the name of &e%input%&4."),

	MC_COMMAND_ERROR_MULTIPLE_PLAYERS_FOUND("MinecraftCommand.Errors.MultiplePlayersFound.ToMinecraftUser",
			"&4There is more than one player matching &e%input%&4."),

	MC_COMMAND_ERROR_MESSAGE_TOO_LONG("MinecraftCommand.Errors.MessageTooLong.ToMinecraftUser",
			"&4The message is too long! (> 100 characters)"),

	// Teamspeak commands
	TS_COMMAND_LIST("TeamspeakCommand.List.ToTeamspeakUser",
			"Currently online (%count%): %list%"),

	TS_COMMAND_PM("TeamspeakCommand.Pm.ToTeamspeakUser",
			"&4Me &r-> &4%player_displayname%&r: %msg%"),

	TS_COMMAND_PM_CONVERSATION_STARTED("TeamspeakCommand.Pm.ConversationStarted.ToTeamspeakUser",
			"Started conversation with player %player_displayname%. You can now chat directly without typing !pm"),

	TS_COMMAND_PM_NO_PLAYER_BY_THIS_NAME("TeamspeakCommand.Pm.Errors.NoPlayerByThisName.ToTeamspeakUser",
			"&4No Minecraft player by the name of &e%input%&4."),

	TS_COMMAND_PM_RECIPIENT_MUTED("TeamspeakCommand.Pm.Errors.RecipientMutedOrNoPermission.ToTeamspeakUser",
			"&4The user &l%player_displayname%&r&4 can't receive your message."),

	TS_COMMAND_SENDER_NAME("TeamspeakCommand.CommandSenderName", "&a[&6TS&a] &e%client_nickname%&r",
			new String[] {"messages.TeamspeakCommandMessages.TeamspeakCommandSenderName"}),

	TS_COMMAND_NOT_WHITELISTED("TeamspeakCommand.Errors.PluginNotWhitelisted.ToTeamspeakUser",
			"You are not allowed to run commands of that plugin.", new String[] {
					"messages.TeamspeakCommandMessages.PluginNotWhitelisted",
					"TeamspeakCommand.Errors.PluginNotWhitelisted"}),

	TS_COMMAND_BLACKLISTED("TeamspeakCommand.Errors.CommandBlacklisted.ToTeamspeakUser",
			"The command you are trying to run is blacklisted.", new String[] {
					"messages.TeamspeakCommandMessages.CommandBlacklisted",
					"TeamspeakCommand.Errors.CommandBlacklisted"});

	private static File configFile;
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
		boolean movedLocale = false;
		boolean movedConfig = false;
		if (configFile == null) {
			configFile = new File(BukkitSpeak.getInstance().getDataFolder(), "locale.yml");
		}
		config = YamlConfiguration.loadConfiguration(configFile);

		if (config.getKeys(false).isEmpty()) {
			if (Configuration.getConfig().isConfigurationSection("messages")) {
				config.set("messages", Configuration.getConfig().getConfigurationSection("messages"));
				Configuration.getConfig().set("messages", null);
				BukkitSpeak.log().info("Moved the messages section from the config into the locale file.");
				changed = true;
			} else {
				BukkitSpeak.getInstance().saveResource("locale.yml", false);
				BukkitSpeak.log().info("Default locale file created!");
				config = YamlConfiguration.loadConfiguration(configFile);
			}
		}

		ValueIteration: for (Messages value : Messages.values()) {
			if (value.defValue == null) continue;
			Object val = config.get(value.path);

			if (val == null) {
				if (value.oldPaths != null) {
					for (String oldPath : value.oldPaths) {
						if (oldPath.startsWith("c:")) {
							oldPath = oldPath.substring("c:".length());
							Object oldVal = Configuration.getConfig().get(oldPath);
							if (oldVal != null && oldVal.getClass().isInstance(value.defValue)) {
								config.set(value.path, oldVal);
								Configuration.getConfig().set(oldPath, null);

								if (Configuration.TS_DEBUGGING.getBoolean()) {
									BukkitSpeak.log().info("Moved \"" + oldPath
											+ "\" to \"" + value.path + "\".");
								}

								changed = true;
								movedConfig = true;
								continue ValueIteration;
							}
						} else {
							Object oldVal = config.get(oldPath);
							if (oldVal != null && oldVal.getClass().isInstance(value.defValue)) {
								config.set(oldPath, null);
								config.set(value.path, oldVal);
								BukkitSpeak.log().info("Moved \"" + oldPath + "\" to \"" + value.path + "\".");
								changed = true;
								movedLocale = true;
								continue ValueIteration;
							}
						}
					}
				}

				value.setToDefault();
				BukkitSpeak.log().warning("Config value \"" + value.path + "\" was not set, changed it to \""
						+ String.valueOf(value.defValue) + "\".");
				changed = true;
			} else if (!val.getClass().isInstance(value.defValue)) {
				value.setToDefault();
				BukkitSpeak.log().warning("Config value \"" + value.path + "\" was not of type "
						+ value.defValue.getClass().getSimpleName() + ", changed it to \""
						+ String.valueOf(value.defValue) + "\".");
				changed = true;
			}
		}

		if (movedLocale) removeEmptySections(config);
		if (movedConfig) {
			removeEmptySections(Configuration.getConfig());
			Configuration.save();
		}
		if (changed) save();
	}

	private static void removeEmptySections(FileConfiguration fileConfig) {
		boolean removed = false;
		for (String key : fileConfig.getKeys(true)) {
			if (!fileConfig.isConfigurationSection(key)) continue;
			if (fileConfig.getConfigurationSection(key).getKeys(false).isEmpty()) {
				fileConfig.set(key, null);
				removed = true;
			}
		}
		if (removed) removeEmptySections(fileConfig);
	}

	public static void save() {
		if (config == null) return;
		if (configFile == null) {
			configFile = new File(BukkitSpeak.getInstance().getDataFolder(), "locale.yml");
		}
		try {
			config.save(configFile);
		} catch (IOException e) {
			BukkitSpeak.log().log(Level.SEVERE, "Could not save the locale file to " + configFile, e);
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
		if (config == null) {
			throw new IllegalAccessError("You need to load the configuration first!");
		}
		return config.getString(path, defValue);
	}

	public void set(String value) {
		config.set(path, value);
	}

	public void setToDefault() {
		config.set(path, defValue);
	}
}
