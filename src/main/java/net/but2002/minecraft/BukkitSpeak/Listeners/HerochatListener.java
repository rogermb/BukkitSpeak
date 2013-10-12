package net.but2002.minecraft.BukkitSpeak.Listeners;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TsTarget;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QuerySender;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

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
	public void onHeroChatMessage(ChannelChatEvent e) {
		if (!BukkitSpeak.useHerochat()) return; // We're not using Herochat, so don't do this
		if (Configuration.TS_MESSAGES_TARGET.getTeamspeakTarget() == TsTarget.NONE) return;
		if (!hasPermission(e.getSender().getPlayer(), "chat")) return;
		
		String channelName = e.getChannel().getName();
		
		if (e.getResult() == Result.ALLOWED && Configuration.PLUGINS_HEROCHAT_CHANNEL.getString().equalsIgnoreCase(channelName)) {
			String tsMsg = Messages.MC_EVENT_CHAT.get();
			tsMsg = new Replacer().addPlayer(e.getSender().getPlayer()).addMessage(e.getMessage()).replace(tsMsg);
			tsMsg = MessageUtil.toTeamspeak(tsMsg, true, Configuration.TS_ALLOW_LINKS.getBoolean());
			
			if (tsMsg.isEmpty()) return;
			
			if (Configuration.TS_MESSAGES_TARGET.getTeamspeakTarget() == TsTarget.CHANNEL) {
				QuerySender qs = new QuerySender(BukkitSpeak.getQuery().getCurrentQueryClientChannelID(),
						JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
				Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qs);
			} else if (Configuration.TS_MESSAGES_TARGET.getTeamspeakTarget() == TsTarget.SERVER) {
				QuerySender qs = new QuerySender(BukkitSpeak.getQuery().getCurrentQueryClientServerID(),
						JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
				Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qs);
			}
		}
	}
	
	private boolean hasPermission(Player player, String perm) {
		return player.hasPermission("bukkitspeak.sendteamspeak." + perm);
	}
}
