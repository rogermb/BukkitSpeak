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
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

import com.gmail.nossr50.api.ChatAPI;
import nz.co.lolnet.james137137.FactionChat.API.FactionChatAPI;
//import com.massivecraft.factions.struct.ChatMode;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ChatListener implements EventExecutor, Listener {

	@Override
	public void execute(Listener listener, Event event) {
		if (!(event instanceof AsyncPlayerChatEvent)) return;
		AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;

		if (BukkitSpeak.useHerochat()) return; // Use Herochat's ChannelChatEvent instead, if using herochat.
		if (Configuration.TS_MESSAGES_TARGET.getTeamspeakTarget() == TsTarget.NONE) return;
		if (e.getPlayer() == null || e.getMessage().isEmpty()) return;

		/* Factions check */
		if (BukkitSpeak.hasFactions() && BukkitSpeak.hasFactionsChat() && Configuration.PLUGINS_FACTIONS_PUBLIC_ONLY.getBoolean()) {
			if(!FactionChatAPI.getChatMode(e.getPlayer()).toUpperCase().equals("PUBLIC")){
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

		/* If all players on the server will receive this message, it should be considered safe to relay */
		if (Configuration.PLUGINS_CHAT_RECIPIENTS_MUST_BE_EVERYONE.getBoolean()) {
			if (e.getRecipients().size() != Bukkit.getOnlinePlayers().size()) {
				return;
			}
		}

		if (!hasPermission(e.getPlayer(), "chat")) return;

		String tsMsg = Messages.MC_EVENT_CHAT.get();
		tsMsg = new Replacer().addPlayer(e.getPlayer()).addMessage(e.getMessage()).replace(tsMsg);
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

	private boolean hasPermission(Player player, String perm) {
		return player.hasPermission("bukkitspeak.sendteamspeak." + perm);
	}
}
