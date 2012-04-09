package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;
import net.but2002.minecraft.BukkitSpeak.Commands.BukkitSpeakCommand;

public class ServerMessageEvent extends TeamspeakEvent{
	
	public ServerMessageEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("targetmode");
		localKeys.add("msg");
		localKeys.add("invokerid");
		localKeys.add("invokername");
		parseLocalValues(msg);
		
		try {
			setUser(plugin.getTs().getUserByID(Integer.parseInt(localValues.get("invokerid"))));
		} catch(Exception e) {
			plugin.getLogger().info("Could not identify user.");
			return;
		}
		
		String msgValue = localValues.get("msg");
		msgValue = filterLinks(msgValue);
		if (msgValue != null && user != null) localValues.put("msg", TeamspeakUser.convert(msgValue));
		String invokerNameValue = localValues.get("invokername");
		if (invokerNameValue != null && user != null) localValues.put("invokername", TeamspeakUser.convert(invokerNameValue));
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if (user != null) {
			if (localValues.get("targetmode").equals("3")) {
				String message = replaceValues(plugin.getStringManager().getMessage("ServerMsg"), true);
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "broadcast")) pl.sendMessage(message);
				}
				plugin.getLogger().info(message);
			} else if (localValues.get("targetmode").equals("2")) {
				String message = replaceValues(plugin.getStringManager().getMessage("ChannelMsg"), true);
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "chat")) pl.sendMessage(message);
				}
				plugin.getLogger().info(message);
			}
		}
	}
	
	protected String filterLinks(String msg) {
		return BukkitSpeakCommand.filterLinks(msg, plugin.getStringManager().getAllowLinks());
	}
}
