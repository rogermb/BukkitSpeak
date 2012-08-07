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
	
	BukkitSpeak plugin;
	StringManager stringManager;
	
	public ChatListener(BukkitSpeak plugin) {
		this.plugin = plugin;
		stringManager = plugin.getStringManager();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getMessage().isEmpty()) return;
		
		String tsMsg = stringManager.getMessage("ChatMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, stringManager.getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			plugin.query.sendTextMessage(plugin.query.getCurrentQueryClientChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			plugin.query.sendTextMessage(plugin.query.getCurrentQueryClientServerID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e){
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null) return;
		
		String tsMsg = stringManager.getMessage("LoginMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getJoinMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, stringManager.getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			plugin.query.sendTextMessage(plugin.query.getCurrentQueryClientChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			plugin.query.sendTextMessage(plugin.query.getCurrentQueryClientServerID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent e){
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null) return;
		
		String tsMsg = stringManager.getMessage("LogoutMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getQuitMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, stringManager.getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			plugin.query.sendTextMessage(plugin.query.getCurrentQueryClientChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			plugin.query.sendTextMessage(plugin.query.getCurrentQueryClientServerID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
		}
	}
	
	private String convert(String input, Boolean color, Boolean links) {
		return BukkitSpeakCommand.convertToTeamspeak(input, color, links);
	}
	
	private String replaceKeys(String input, HashMap<String, String> repl) {
		return BukkitSpeakCommand.replaceKeys(input, repl);
	}
	
	public void reload(BukkitSpeak plugin) {
		this.plugin = plugin;
		stringManager = plugin.getStringManager();
	}
}
