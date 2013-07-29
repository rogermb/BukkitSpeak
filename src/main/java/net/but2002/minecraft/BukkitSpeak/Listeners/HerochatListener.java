package net.but2002.minecraft.BukkitSpeak.Listeners;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TsTargetEnum;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QuerySender;
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
		if (!BukkitSpeak.useHerochat()) return; //We're not using Herochat, so don't do this
		if (BukkitSpeak.getStringManager().getHerochatChannel() == null) return; //This shouldn't happen, but just in case.
		if (!hasPermission(e.getSender().getPlayer(), "chat")) return;
		
		String channelName = e.getChannel().getName();
		
		if (e.getResult() == Result.ALLOWED && BukkitSpeak.getStringManager().getHerochatChannel().equalsIgnoreCase(channelName)) {
			String tsMsg = BukkitSpeak.getStringManager().getMessage("ChatMessage");
			tsMsg = new Replacer().addPlayer(e.getSender().getPlayer()).addMessage(e.getMessage()).replace(tsMsg);
			tsMsg = MessageUtil.toTeamspeak(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
			
			if (tsMsg.isEmpty()) return; //Don't spam with empty messages.
			
			if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
				QuerySender qs = new QuerySender(BukkitSpeak.getQuery().getCurrentQueryClientChannelID(),
						JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
				Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qs);
			} else if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.SERVER) {
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
