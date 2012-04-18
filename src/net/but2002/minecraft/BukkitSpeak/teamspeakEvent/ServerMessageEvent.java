package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

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
		msgValue = filterLinks(TeamspeakUser.convert(msgValue), plugin.getStringManager().getAllowLinks());
		if (msgValue == null || msgValue.isEmpty() || user == null) return;
		localValues.put("msg", msgValue);
		String invokerNameValue = localValues.get("invokername");
		if (invokerNameValue != null && user != null) localValues.put("invokername", TeamspeakUser.convert(invokerNameValue));
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if (user != null) {
			if (localValues.get("targetmode").equals("3")) {
				String m = plugin.getStringManager().getMessage("ServerMsg");
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "broadcast")) {
						pl.sendMessage(replaceValues(m, true));
					}
				}
				plugin.getLogger().info(replaceValues(m, false));
			} else if (localValues.get("targetmode").equals("2")) {
				String m = plugin.getStringManager().getMessage("ChannelMsg");
				for (Player pl : plugin.getServer().getOnlinePlayers()) {
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "chat")) {
						pl.sendMessage(replaceValues(m, true));
					}
				}
				plugin.getLogger().info(replaceValues(m, false));
			} else if (localValues.get("targetmode").equals("1")) {
				String m = plugin.getStringManager().getMessage("PrivateMsg");
				String p = plugin.getRecipient(getUser());
				if (p != null && !p.isEmpty() && plugin.getStringManager().getConsoleName().equals(p)) {
					Player pl = plugin.getServer().getPlayerExact(p);
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "pm")) pl.sendMessage(replaceValues(m, true));
				}
				plugin.getLogger().info(replaceValues(m, false));
			}
		}
	}
	
	protected String filterLinks(String input, Boolean allowed) {
		if (input != null) {
			String s = input;
			if (allowed) {
				s = s.replaceAll("\\[URL](.*)\\[/URL]", "$1");
			} else {
				s = s.replaceAll("\\[URL](.*)\\[/URL]", "");
			}
			return s;
		}
		return null;
	}
}
