package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QuerySender;
import net.but2002.minecraft.BukkitSpeak.Commands.BukkitSpeakCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Chatter.Result;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class HerochatListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onHeroChatMessage(ChannelChatEvent event) {
		if (!BukkitSpeak.useHerochat()) return; //We're not using Herochat, so don't do this
		if (BukkitSpeak.getStringManager().getHerochatChannel() == null) return; //This shouldn't happen, but just in case.
		if (!hasPermission(event.getSender().getPlayer(), "chat")) return;
		
		String channelName = event.getChannel().getName();
		
		if (event.getResult() == Result.ALLOWED && BukkitSpeak.getStringManager().getHerochatChannel().equalsIgnoreCase(channelName)) {
			String tsMsg = BukkitSpeak.getStringManager().getMessage("ChatMessage");
			HashMap<String, String> repl = new HashMap<String, String>();
			repl.put("%player_name%", event.getSender().getName());
			repl.put("%player_displayname%", event.getSender().getPlayer().getDisplayName());
			repl.put("%msg%", event.getMessage());
			
			tsMsg = replaceKeys(tsMsg, repl);
			tsMsg = convert(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
			
			if (tsMsg.isEmpty()) return; //Don't spam with empty messages.
			
			if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
				QuerySender qs = new QuerySender(BukkitSpeak.getQuery().getCurrentQueryClientChannelID(),
						JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
				Bukkit.getScheduler().scheduleAsyncDelayedTask(BukkitSpeak.getInstance(), qs);
			} else if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.SERVER) {
				QuerySender qs = new QuerySender(BukkitSpeak.getQuery().getCurrentQueryClientServerID(),
						JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
				Bukkit.getScheduler().scheduleAsyncDelayedTask(BukkitSpeak.getInstance(), qs);
			}
		}
	}
	
	private String convert(String input, Boolean color, Boolean links) {
		return BukkitSpeakCommand.convertToTeamspeak(input, color, links);
	}
	
	private String replaceKeys(String input, HashMap<String, String> repl) {
		return BukkitSpeakCommand.replaceKeys(input, repl);
	}
	
	private boolean hasPermission(Player player, String perm) {
		return player.hasPermission("bukkitspeak.sendteamspeak." + perm);
	}
}
