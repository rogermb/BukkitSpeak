package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;
import java.util.regex.Pattern;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;

public class TeamspeakListener implements TeamspeakActionListener {
	
	@Override
	public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo) {
		
		if (eventType.equals("notifycliententerview")) {
			if (eventInfo == null || !eventInfo.get("client_type").equals("0")
					|| eventInfo.get("client_nickname").startsWith("Unknown from")) return;
			new EnterEvent(eventInfo);
		} else if (eventType.equals("notifyclientleftview")) {
			if (!BukkitSpeak.getClientList().containsID(Integer.parseInt(eventInfo.get("clid")))) return;
			new LeaveEvent(eventInfo);
		} else if (eventType.equals("notifytextmessage")) {
			String message = eventInfo.get("msg");
			
			String reg = Pattern.quote(Configuration.TS_COMMANDS_PREFIX.getString());
			if (Configuration.TS_COMMANDS_ENABLED.getBoolean() && message.matches(reg + "\\S.*")) {
				new TeamspeakCommandEvent(eventInfo);
			} else {
				new ServerMessageEvent(eventInfo);
			}
		} else if (eventType.equals("notifyclientmoved")) {
			new ClientMovedEvent(eventInfo);
		}
	}
}
