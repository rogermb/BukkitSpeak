package net.but2002.minecraft.BukkitSpeak.Configuration;

import java.util.List;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TsTargetEnum;

import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public enum Configuration {
	
	MAIN_IP("main.TeamSpeakIp", "1.2.3.4"),
	MAIN_SERVERPORT("main.TeamSpeakPort", 9987),
	MAIN_QUERYPORT("main.QueryPort", 10011),
	MAIN_USERNAME("main.QueryUsername", "admin"),
	MAIN_PASSWORD("main.QueryPassword", "123456"),
	
	TS_NICKNAME("teamspeak.TeamspeakNickname", "Minecraft"),
	TS_CONSOLE_NAME("teamspeak.ConsoleName", "&eServer"),
	TS_CHANNEL_ID("teamspeak.ChannelID", 0),
	TS_CHANNEL_PASSWORD("teamspeak.ChannelPassword", ""),
	TS_ENABLE_SERVER_EVENTS("teamspeak.ListenToServerEvents", true),
	TS_ENABLE_SERVER_MESSAGES("teamspeak.ListenToServerBroadcasts", true),
	TS_ENABLE_CHANNEL_EVENTS("teamspeak.ListenToChannel", true),
	TS_ENABLE_CHANNEL_MESSAGES("teamspeak.ListenToChannelChat", true),
	TS_ENABLE_PRIVATE_MESSAGES("teamspeak.ListenToPrivateMessages", true),
	TS_ALLOW_LINKS("teamspeak.AllowLinksInMessages", true),
	TS_MESSAGES_TARGET("teamspeak.SendChatToTeamspeak", "none"),
	TS_LOGGING("teamspeak.LogChatInConsole", true),
	TS_DEFAULT_REASON("teamspeak.DefaultReason", "-"),
	TS_CHAT_LISTENER_PRIORITY("teamspeak.ChatListenerPriority", "MONITOR"),
	TS_DEBUGGING("teamspeak.Debug", false),
	
	TS_COMMANDS_ENABLED("teamspeak-commands.Enabled", false),
	TS_COMMANDS_PREFIX("teamspeak-commands.CommandPrefix", "!"),
	TS_COMMANDS_LOGGING("teamspeak-commands.LogTeamspeakCommands", true),
	TS_COMMANDS_MESSAGE_BUFFER("teamspeak-commands.MessageBufferDelay", 50),
	
	PLUGINS_FACTIONS_PUBLIC_ONLY("plugin-interaction.Factions.public-only", true),
	
	PLUGINS_HEROCHAT_ENABLED("plugin-interaction.Herochat.enabled", false),
	PLUGINS_HEROCHAT_CHANNEL("plugin-interaction.Herochat.channel", "Global"),
	PLUGINS_HEROCHAT_RELAY_EVENTS("plugin-interaction.Herochat.SendTeamspeakEventsToChannel", false),
	
	PLUGINS_MCMMO_FILTER_PARTY_CHAT("plugin-interaction.mcMMO.FilterPartyChat", true),
	PLUGINS_MCMMO_FILTER_ADMIN_CHAT("plugin-interaction.mcMMO.FilterAdminChat", true);
	
	private final String path;
	private final Object defValue;
	
	Configuration(String configPath, Object defaultValue) {
		path = configPath;
		defValue = defaultValue;
	}
	
	public static void reload() {
		boolean changed = false;
		BukkitSpeak.getInstance().reloadConfig();
		FileConfiguration config = BukkitSpeak.getInstance().getConfig();
		
		for (Configuration value : Configuration.values()) {
			if (value.defValue == null) continue;
			Object val = config.get(value.path);
			
			if (val == null) {
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
		BukkitSpeak.getInstance().saveConfig();
	}
	
	public String getConfigPath() {
		return path;
	}
	
	public Object getDefaultValue() {
		return defValue;
	}
	
	public Object get() {
		return BukkitSpeak.getInstance().getConfig().get(path, defValue);
	}
	
	public String getString() {
		return BukkitSpeak.getInstance().getConfig().getString(path, (String) defValue);
	}
	
	public int getInt() {
		return BukkitSpeak.getInstance().getConfig().getInt(path, (Integer) defValue);
	}
	
	public boolean getBoolean() {
		return BukkitSpeak.getInstance().getConfig().getBoolean(path, (Boolean) defValue);
	}
	
	public double getDouble() {
		return BukkitSpeak.getInstance().getConfig().getDouble(path, (Double) defValue);
	}
	
	public long getLong() {
		return BukkitSpeak.getInstance().getConfig().getLong(path, (Long) defValue);
	}
	
	public List<?> getList() {
		return BukkitSpeak.getInstance().getConfig().getList(path, (List<?>) defValue);
	}
	
	public Vector getVector() {
		return BukkitSpeak.getInstance().getConfig().getVector(path, (Vector) defValue);
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return BukkitSpeak.getInstance().getConfig().getOfflinePlayer(path, (OfflinePlayer) defValue);
	}
	
	public ItemStack getItemStack() {
		return BukkitSpeak.getInstance().getConfig().getItemStack(path, (ItemStack) defValue);
	}
	
	public Color getColor() {
		return BukkitSpeak.getInstance().getConfig().getColor(path, (Color) defValue);
	}
	
	public EventPriority getEventPriority() {
		String name = getString();
		for (EventPriority val : EventPriority.values()) {
			if (val.name().equalsIgnoreCase(name)) return val;
		}
		BukkitSpeak.log().warning("Config value \"" + path
				+ "\" did not match a valid EventPriority. Using MONITOR instead.");
		return EventPriority.MONITOR;
	}
	
	public TsTargetEnum getTeamspeakTarget() {
		TsTargetEnum tsTarget = TsTargetEnum.getFromString(getString());
		if (tsTarget == null) {
			BukkitSpeak.log().warning("Config value \"" + path
					+ "\" did not match a valid Teamspeak target. Not sending messages to TeamSpeak.");
			return TsTargetEnum.NONE;
		} else {
			return tsTarget;
		}
	}
	
	public void set(Object value) {
		BukkitSpeak.getInstance().getConfig().set(path, value);
	}
	
	public void setToDefault() {
		BukkitSpeak.getInstance().getConfig().set(path, defValue);
	}
}
