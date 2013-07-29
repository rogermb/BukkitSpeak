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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class PlayerListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getJoinMessage() == null) return;
		
		if (!hasPermission(e.getPlayer(), "join")) return;
		
		String tsMsg = BukkitSpeak.getStringManager().getMessage("LoginMessage");
		tsMsg = new Replacer().addPlayer(e.getPlayer()).addMessage(e.getJoinMessage()).replace(tsMsg);
		tsMsg = MessageUtil.toTeamspeak(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getQuitMessage() == null) return;
		
		if (!hasPermission(e.getPlayer(), "quit")) return;
		
		String tsMsg = BukkitSpeak.getStringManager().getMessage("LogoutMessage");
		tsMsg = new Replacer().addPlayer(e.getPlayer()).addMessage(e.getQuitMessage()).replace(tsMsg);
		tsMsg = MessageUtil.toTeamspeak(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerKick(PlayerKickEvent e) {
		if (e.isCancelled()) return;
		
		if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getLeaveMessage() == null) return;
		
		String tsMsg;
		if (e.getPlayer().isBanned()) {
			// Was banned
			if (!hasPermission(e.getPlayer(), "ban")) return;
			tsMsg = BukkitSpeak.getStringManager().getMessage("BannedMessage");
		} else {
			// Or just kicked
			if (!hasPermission(e.getPlayer(), "kick")) return;
			tsMsg = BukkitSpeak.getStringManager().getMessage("KickedMessage");
		}
		
		tsMsg = new Replacer().addPlayer(e.getPlayer()).addMessage(e.getLeaveMessage()).replace(tsMsg);
		tsMsg = MessageUtil.toTeamspeak(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
		
		if (tsMsg.isEmpty()) return;
		
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
	
	private boolean hasPermission(Player player, String perm) {
		return player.hasPermission("bukkitspeak.sendteamspeak." + perm);
	}
}
