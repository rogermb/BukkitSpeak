package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.Commands.BukkitSpeakCommand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {
	
	BukkitSpeak plugin;
	TeamspeakHandler ts;
	StringManager stringManager;
	
	public ChatListener(BukkitSpeak plugin) {
		this.plugin = plugin;
		ts = plugin.getTs();
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
		
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			ts.SendTextMessage(2, stringManager.getChannelID(), convert(tsMsg, false, stringManager.getAllowLinks()));
		} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			ts.SendTextMessage(3, 0, convert(tsMsg, false, stringManager.getAllowLinks()));
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
		ts = plugin.getTs();
		stringManager = plugin.getStringManager();
	}
}
