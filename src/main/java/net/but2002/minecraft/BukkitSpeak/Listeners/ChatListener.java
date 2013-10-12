package net.but2002.minecraft.BukkitSpeak.Listeners;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TsTargetEnum;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QuerySender;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

import com.gmail.nossr50.api.ChatAPI;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.ChatMode;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ChatListener implements EventExecutor, Listener {
	
	@Override
	public void execute(Listener listener, Event event) {
		if (!(event instanceof AsyncPlayerChatEvent)) return;
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;
		
		if (BukkitSpeak.useHerochat()) return; // Use Herochat's ChannelChatEvent instead, if using herochat.
		if (Configuration.TS_MESSAGES_TARGET.getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getMessage().isEmpty()) return;
		
		/* Factions check */
		if (BukkitSpeak.hasFactions() && Configuration.PLUGINS_FACTIONS_PUBLIC_ONLY.getBoolean()) {
			if (FPlayers.i.get(e.getPlayer()).getChatMode() != ChatMode.PUBLIC) {
				return;
			}
		}
		
		/* mcMMO check */
		if (BukkitSpeak.hasMcMMO()) {
			if (ChatAPI.isUsingPartyChat(e.getPlayer()) && Configuration.PLUGINS_MCMMO_FILTER_PARTY_CHAT.getBoolean()) {
				return;
			}
			if (ChatAPI.isUsingAdminChat(e.getPlayer()) && Configuration.PLUGINS_MCMMO_FILTER_ADMIN_CHAT.getBoolean()) {
				return;
			}
		}
		
		if (!hasPermission(e.getPlayer(), "chat")) return;
		
		String tsMsg = Messages.MC_EVENT_CHAT.get();
		tsMsg = new Replacer().addPlayer(e.getPlayer()).addMessage(e.getMessage()).replace(tsMsg);
		tsMsg = MessageUtil.toTeamspeak(tsMsg, true, Configuration.TS_ALLOW_LINKS.getBoolean());
		
		if (tsMsg.isEmpty()) return;
		
		if (Configuration.TS_MESSAGES_TARGET.getTeamspeakTarget() == TsTargetEnum.CHANNEL) {
			QuerySender qs = new QuerySender(BukkitSpeak.getQuery().getCurrentQueryClientChannelID(),
					JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
			Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qs);
		} else if (Configuration.TS_MESSAGES_TARGET.getTeamspeakTarget() == TsTargetEnum.SERVER) {
			QuerySender qs = new QuerySender(BukkitSpeak.getQuery().getCurrentQueryClientServerID(),
					JTS3ServerQuery.TEXTMESSAGE_TARGET_VIRTUALSERVER, tsMsg);
			Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qs);
		}
	}
	
	private boolean hasPermission(Player player, String perm) {
		return player.hasPermission("bukkitspeak.sendteamspeak." + perm);
	}
}
