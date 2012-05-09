package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.*;

import de.stefan1200.jts3serverquery.TeamspeakActionListener;

public class TeamspeakListener implements TeamspeakActionListener {
	
	BukkitSpeak plugin;
	
	public TeamspeakListener(BukkitSpeak plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo) {
		
		if (eventType.equals("notifycliententerview")) {
			new EnterEvent(plugin, eventInfo);
		} else if (eventType.equals("notifyclientleftview")) {
			new LeaveEvent(plugin, eventInfo);
		} else if (eventType.equals("notifytextmessage")) {
			new ServerMessageEvent(plugin, eventInfo);
		} else if (eventType.equals("notifyclientmoved")) {
			new ClientMovedEvent(plugin, eventInfo);
		}
	}
}
