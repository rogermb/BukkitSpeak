package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

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
		msg = msg.replaceAll("\\n", "");
		msg = MessageUtil.toMinecraft(msg, true, BukkitSpeak.getStringManager().getAllowLinks());
		if (msg.isEmpty()) return;
		getUser().put("msg", msg);
		
		if (info.get("targetmode").equals("3")) {
			String m = BukkitSpeak.getStringManager().getMessage("ServerMsg");
			if (m.isEmpty()) return;
			m = new Replacer().addClient(getUser()).replace(m);
			m = MessageUtil.toMinecraft(m, true, true);
			
			for (Player pl : BukkitSpeak.getInstance().getServer().getOnlinePlayers()) {
				if (!BukkitSpeak.getMuted(pl) && checkPermissions(pl, "broadcast")) {
					pl.sendMessage(m);
				}
			}
			if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(m);
			
		} else if (info.get("targetmode").equals("2")) {
			sendMessage("ChannelMsg", "chat");
			
		} else if (info.get("targetmode").equals("1")) {
			String m = BukkitSpeak.getStringManager().getMessage("PrivateMsg");
			if (m.isEmpty()) return;
			m = new Replacer().addClient(getUser()).replace(m);
			m = MessageUtil.toMinecraft(m, true, true);
			
			String p = BukkitSpeak.getInstance().getRecipient(getClientId());
			if (p == null || p.isEmpty()) return;
			
			if (MessageUtil.toMinecraft(BukkitSpeak.getStringManager().getConsoleName(), false, false).equals(p)) {
				BukkitSpeak.log().info(m);
			} else {
				Player pl = BukkitSpeak.getInstance().getServer().getPlayerExact(p);
				if (pl == null) return;
				if (!BukkitSpeak.getMuted(pl) && checkPermissions(pl, "pm")) {
					pl.sendMessage(m);
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
