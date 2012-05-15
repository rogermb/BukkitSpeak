package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.Commands.BukkitSpeakCommand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
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
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getMessage().isEmpty()) return;
		
		String tsMsg = stringManager.getMessage("MinecraftMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, stringManager.getAllowLinks());
		
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			plugin.query.sendTextMessage(stringManager.getChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			plugin.query.sendTextMessage(0, JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event){
	        String name = event.getPlayer().getName();
	        String format = "[MC]<" + name + "> : Joined the Game.";
	        if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
				plugin.query.sendTextMessage(stringManager.getChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, format);
			} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
				plugin.query.sendTextMessage(0, JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, format);
			}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerQuit(PlayerQuitEvent event){
	        String name = event.getPlayer().getName();
	        String format = "[MC]<" + name + "> : Left the Game.";
	        if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
				plugin.query.sendTextMessage(stringManager.getChannelID(), JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, format);
			} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
				plugin.query.sendTextMessage(0, JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, format);
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
