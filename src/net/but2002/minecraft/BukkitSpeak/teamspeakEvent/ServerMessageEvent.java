package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class ServerMessageEvent extends TeamspeakEvent{
	
	HashMap<String, String> info;
	
	public ServerMessageEvent(BukkitSpeak plugin, HashMap<String, String> info) {
		super(plugin, Integer.parseInt(info.get("invokerid")));
		this.info = info;
		
		user.put("targetmode", info.get("targetmode"));
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		String msg = info.get("msg");
		msg = filterLinks(msg, plugin.getStringManager().getAllowLinks());
		user.put("msg", msg);
		
		if (info.get("targetmode").equals("3")) {
			String m = plugin.getStringManager().getMessage("ServerMsg");
			for (Player pl : plugin.getServer().getOnlinePlayers()) {
				if (!plugin.getMuted(pl) && CheckPermissions(pl, "broadcast")) {
					pl.sendMessage(replaceValues(m, true));
				}
			}
			plugin.getLogger().info(replaceValues(m, false));
		} else if (info.get("targetmode").equals("2")) {
			String m = plugin.getStringManager().getMessage("ChannelMsg");
			for (Player pl : plugin.getServer().getOnlinePlayers()) {
				if (!plugin.getMuted(pl) && CheckPermissions(pl, "chat")) {
					pl.sendMessage(replaceValues(m, true));
				}
			}
			plugin.getLogger().info(replaceValues(m, false));
		} else if (info.get("targetmode").equals("1")) {
			String m = plugin.getStringManager().getMessage("PrivateMsg");
			String p = plugin.getRecipient(getClientId());
			if (p != null && !p.isEmpty()) {
				if (!replaceValues(plugin.getStringManager().getConsoleName(), false).equals(p)) {
					Player pl = plugin.getServer().getPlayerExact(p);
					if (pl == null) return;
					if (!plugin.getMuted(pl) && CheckPermissions(pl, "pm")) {
						pl.sendMessage(replaceValues(m, true));
					}
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
