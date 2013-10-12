package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
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
		msg = msg.replaceAll("\\n", " ");
		msg = MessageUtil.toMinecraft(msg, true, Configuration.TS_ALLOW_LINKS.getBoolean());
		if (msg.isEmpty()) return;
		getUser().put("msg", msg);
		
		if (info.get("targetmode").equals("3")) {
			String m = Messages.TS_EVENT_SERVER_MESSAGE.get();
			if (m.isEmpty()) return;
			m = new Replacer().addClient(getUser()).replace(m);
			m = MessageUtil.toMinecraft(m, true, true);
			
			for (Player pl : BukkitSpeak.getInstance().getServer().getOnlinePlayers()) {
				if (!BukkitSpeak.getMuted(pl) && checkPermissions(pl, "broadcast")) {
					pl.sendMessage(m);
				}
			}
			if (Configuration.TS_LOGGING.getBoolean()) BukkitSpeak.log().info(m);
			
		} else if (info.get("targetmode").equals("2")) {
			sendMessage(Messages.TS_EVENT_CHANNEL_MESSAGE, "chat");
			
		} else if (info.get("targetmode").equals("1")) {
			String m = Messages.TS_EVENT_PRIVATE_MESSAGE.get();
			if (m.isEmpty()) return;
			m = new Replacer().addClient(getUser()).replace(m);
			m = MessageUtil.toMinecraft(m, true, true);
			
			String p = BukkitSpeak.getInstance().getRecipient(getClientId());
			if (p == null || p.isEmpty()) return;
			
			if (MessageUtil.toMinecraft(Configuration.TS_CONSOLE_NAME.getString(), false, false).equals(p)) {
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
