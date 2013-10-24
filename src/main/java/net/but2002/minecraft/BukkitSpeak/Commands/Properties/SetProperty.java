package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public abstract class SetProperty {
	
	protected void send(CommandSender sender, Level level, String msg) {
		String m = msg;
		if (sender instanceof Player) {
			m = m.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "\u00A7$3");
			sender.sendMessage(BukkitSpeak.getFullName() + m);
		} else {
			m = m.replaceAll("((&|$|\u00A7)([a-fk-orA-FK-OR0-9]))", "");
			BukkitSpeak.log().log(level, m);
		}
	}
	
	protected void reloadListener() {
		BukkitSpeak.getQuery().removeAllEvents();
		
		if (Configuration.TS_ENABLE_SERVER_EVENTS.getBoolean()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0);
		}
		if (Configuration.TS_ENABLE_SERVER_MESSAGES.getBoolean()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0);
		}
		if (Configuration.TS_ENABLE_CHANNEL_EVENTS.getBoolean()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL, BukkitSpeak.getQuery().getCurrentQueryClientChannelID());
		}
		if (Configuration.TS_ENABLE_CHANNEL_MESSAGES.getBoolean()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL, BukkitSpeak.getQuery().getCurrentQueryClientChannelID());
		}
		if (Configuration.TS_ENABLE_PRIVATE_MESSAGES.getBoolean()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTPRIVATE, 0);
		}
	}
	
	protected void broadcastMessage(String mcMsg, CommandSender sender) {
		if (mcMsg == null || mcMsg.isEmpty()) return;
		for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
			if (!BukkitSpeak.getMuted(pl)) {
				pl.sendMessage(MessageUtil.toMinecraft(mcMsg, true, Configuration.TS_ALLOW_LINKS.getBoolean()));
			}
		}
		if (!(sender instanceof Player) || (Configuration.TS_LOGGING.getBoolean())) {
			BukkitSpeak.log().info(MessageUtil.toMinecraft(mcMsg, false, Configuration.TS_ALLOW_LINKS.getBoolean()));
		}
	}
	
	protected void sendChannelChangeMessage(CommandSender sender) {
		String mcMsg = Messages.MC_COMMAND_CHANNEL_CHANGE.get();
		HashMap<String, String> info = BukkitSpeak.getQuery().getInfo(JTS3ServerQuery.INFOMODE_CHANNELINFO,
				BukkitSpeak.getQuery().getCurrentQueryClientChannelID());
		
		mcMsg = new Replacer().addSender(sender).addChannel(info).replace(mcMsg);
		broadcastMessage(mcMsg, sender);
	}
	
	protected void connectChannel(CommandSender sender) {
		int cid = BukkitSpeak.getQuery().getCurrentQueryClientChannelID();
		int clid = BukkitSpeak.getQuery().getCurrentQueryClientID();
		String pw = Configuration.TS_CHANNEL_PASSWORD.getString();
		if (!BukkitSpeak.getQuery().moveClient(clid, cid, pw)) {
			send(sender, Level.WARNING, "&4The channel ID could not be set.");
			send(sender, Level.WARNING, "&4Ensure that the ChannelID is really assigned to a valid channel.");
			send(sender, Level.WARNING, "&4" + BukkitSpeak.getQuery().getLastError());
			return;
		}
		sendChannelChangeMessage(sender);
	}
	
	public String getName() {
		String property = getProperty().getConfigPath();
		return property.substring(property.lastIndexOf(".") + 1);
	}
	
	public abstract Configuration getProperty();
	
	public abstract String getAllowedInput();
	
	public abstract String getDescription();
	
	public abstract boolean execute(CommandSender sender, String arg);
	
	public abstract List<String> onTabComplete(CommandSender sender, String[] args);
}
