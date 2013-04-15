package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.dthielke.herochat.Herochat;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class ServerMessageEvent extends TeamspeakEvent {
	
	private HashMap<String, String> info;
	
	public ServerMessageEvent(HashMap<String, String> infoMap) {
		setUser(Integer.parseInt(infoMap.get("invokerid")));
		info = infoMap;
		
		if (getUser() == null) return;
		getUser().put("targetmode", infoMap.get("targetmode"));
		performAction();
	}
	
	@Override
	protected void performAction() {
		
		if (info == null || getClientType() != 0) return;
		
		String msg = info.get("msg");
		msg = filterLinks(msg, BukkitSpeak.getStringManager().getAllowLinks());
		getUser().put("msg", msg);
		
		if (info.get("targetmode").equals("3")) {
			String m = BukkitSpeak.getStringManager().getMessage("ServerMsg");
			if (m.isEmpty()) return;
			for (Player pl : getOnlinePlayers()) {
				if (!isMuted(pl) && checkPermissions(pl, "broadcast")) {
					pl.sendMessage(replaceValues(m, true));
				}
			}
			if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
		} else if (info.get("targetmode").equals("2")) {
			String m = BukkitSpeak.getStringManager().getMessage("ChannelMsg");
			if (m.isEmpty()) return;
			if (BukkitSpeak.useHerochat()) { //We are using Herochat
				/* Sadly using Herochat doesn't provide a way to
				send a message to a part of the group */
				String c = BukkitSpeak.getStringManager().getHerochatChannel();
				Herochat.getChannelManager().getChannel(c).announce(replaceValues(m, true));
			} else { //We're not using Herochat
				for (Player pl : getOnlinePlayers()) {
					if (!isMuted(pl) && checkPermissions(pl, "chat")) {
						pl.sendMessage(replaceValues(m, true));
					}
				}
			}
			
			if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
		} else if (info.get("targetmode").equals("1")) {
			String m = BukkitSpeak.getStringManager().getMessage("PrivateMsg");
			String p = BukkitSpeak.getInstance().getRecipient(getClientId());
			if (!m.isEmpty() && p != null && !p.isEmpty()) {
				if (replaceValues(BukkitSpeak.getStringManager().getConsoleName(), false).equals(p)) {
					BukkitSpeak.log().info(replaceValues(m, false));
				} else {
					Player pl = BukkitSpeak.getInstance().getServer().getPlayerExact(p);
					if (pl == null) return;
					if (!isMuted(pl) && checkPermissions(pl, "pm")) {
						pl.sendMessage(replaceValues(m, true));
					}
				}
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
