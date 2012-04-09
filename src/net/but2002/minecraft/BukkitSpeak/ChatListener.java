package net.but2002.minecraft.BukkitSpeak;

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
		if (e.getPlayer() == null || e.getMessage().isEmpty()) return;
		
		if (stringManager.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			ts.pushMessage("sendtextmessage targetmode=2"
					+ " target=" + stringManager.getChannelID()
					+ " msg=" + convert(e.getMessage()), e.getPlayer().getName());
		} else if (stringManager.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			ts.pushMessage("sendtextmessage targetmode=3"
					+ " target=0"
					+ " msg=" + convert(e.getMessage()), e.getPlayer().getName());
		}
		
	}
	
	public String convert(String input) {
		return BukkitSpeakCommand.convert(input);
	}
	
	public void reload(BukkitSpeak plugin) {
		this.plugin = plugin;
		ts = plugin.getTs();
		stringManager = plugin.getStringManager();
	}
}
