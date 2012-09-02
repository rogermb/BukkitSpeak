package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.Commands.BukkitSpeakCommand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ChatListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getMessage().isEmpty()) return;
		
		String tsMsg = BukkitSpeak.stringManager.getMessage("ChatMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, BukkitSpeak.stringManager.getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
		if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			BukkitSpeak.query.sendTextMessage(BukkitSpeak.query.getCurrentQueryClientChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		} else if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			BukkitSpeak.query.sendTextMessage(BukkitSpeak.query.getCurrentQueryClientServerID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e){
		if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null) return;
		
		String tsMsg = BukkitSpeak.stringManager.getMessage("LoginMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getJoinMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, BukkitSpeak.stringManager.getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
		if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			BukkitSpeak.query.sendTextMessage(BukkitSpeak.query.getCurrentQueryClientChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		} else if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			BukkitSpeak.query.sendTextMessage(BukkitSpeak.query.getCurrentQueryClientServerID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent e){
		if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null) return;
		
		String tsMsg = BukkitSpeak.stringManager.getMessage("LogoutMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getQuitMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, BukkitSpeak.stringManager.getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
		if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			BukkitSpeak.query.sendTextMessage(BukkitSpeak.query.getCurrentQueryClientChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		} else if (BukkitSpeak.stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			BukkitSpeak.query.sendTextMessage(BukkitSpeak.query.getCurrentQueryClientServerID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
		}
	}
	
	private String convert(String input, Boolean color, Boolean links) {
		return BukkitSpeakCommand.convertToTeamspeak(input, color, links);
	}
	
	private String replaceKeys(String input, HashMap<String, String> repl) {
		return BukkitSpeakCommand.replaceKeys(input, repl);
	}
}
