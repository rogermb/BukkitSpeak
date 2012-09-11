package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import de.stefan1200.jts3serverquery.TeamspeakActionListener;

public class TeamspeakListener implements TeamspeakActionListener {
	
	@Override
	public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo) {
		
		if (eventType.equals("notifycliententerview")) {
			if (eventInfo == null || !eventInfo.get("client_type").equals("0")
					|| eventInfo.get("client_nickname").startsWith("Unknown from")) return;
			new EnterEvent(eventInfo);
		} else if (eventType.equals("notifyclientleftview")) {
			if (!BukkitSpeak.getClients().containsKey(Integer.parseInt(eventInfo.get("clid")))) return;
			new LeaveEvent(eventInfo);
		} else if (eventType.equals("notifytextmessage")) {
			new ServerMessageEvent(eventInfo);
		} else if (eventType.equals("notifyclientmoved")) {
			new ClientMovedEvent(eventInfo);
		}
		
		/*
		String msg = eventInfo.remove("msg");
		
		//Add Team Speak Commands ingame commands.
		if(msg.contains("!playerlist")){
			Player[] pl = plugin.getServer().getOnlinePlayers();
			if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
				plugin.query.sendTextMessage(stringManager.getChannelID(),
						JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, "==Minecraft PlayerList==");
			} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
				plugin.query.sendTextMessage(0, JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER,
						"==Minecraft PlayerList==");
			}
			for (int i=0; i<pl.length; i++){
				String name = pl[i].getName();
				String format = "[MC]" + name;
				if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
					plugin.query.sendTextMessage(stringManager.getChannelID(),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, format);
				} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
					plugin.query.sendTextMessage(0, JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, format);
				}
			}
			return;
		}
		
		//Example of usage
		if(msg.contains("!reload")){
			if(plugin.query.removeAllEvents()){
			}
			plugin.query.removeTeamspeakActionListener();
			plugin.query.closeTS3Connection();
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			plugin.getServer().getPluginManager().enablePlugin(plugin);
			String format = "Example";
			if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
					plugin.query.sendTextMessage(stringManager.getChannelID(),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, format);
				} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
					plugin.query.sendTextMessage(0, JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, format);
				}
			return;
		}
		*/
	}
}
