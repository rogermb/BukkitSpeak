package net.but2002.minecraft.BukkitSpeak.Listeners;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TsTargetEnum;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QuerySender;
import net.but2002.minecraft.BukkitSpeak.Commands.BukkitSpeakCommand;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gmail.nossr50.mcMMO;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.struct.ChatMode;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class PlayerListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if (e.isCancelled()) return;
		
		if (BukkitSpeak.useHerochat()) return; //Use Herochat's ChannelChatEvent instead, if using herochat.
		if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getMessage().isEmpty()) return;
		
		/* Factions check */
		if (BukkitSpeak.hasFactions() && BukkitSpeak.getStringManager().getFactionsPublicOnly()) {
			if (FPlayers.i.get(e.getPlayer()).getChatMode() != ChatMode.PUBLIC) {
				return;
			}
		}
		
		/* mcMMO check */
		if (BukkitSpeak.hasMcMMO()) {
			if (mcMMO.p.getPlayerProfile((OfflinePlayer) e.getPlayer()).getPartyChatMode()
					&& BukkitSpeak.getStringManager().getMcMMOFilterPartyChat()) {
				return;
			}
			if (mcMMO.p.getPlayerProfile((OfflinePlayer) e.getPlayer()).getAdminChatMode()
					&& BukkitSpeak.getStringManager().getMcMMOFilterAdminChat()) {
				return;
			}
		}
		
		if (!hasPermission(e.getPlayer(), "chat")) return;
		
		String tsMsg = BukkitSpeak.getStringManager().getMessage("ChatMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
		
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
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (BukkitSpeak.getStringManager().getTeamspeakTarget() == TsTargetEnum.NONE) return;
		if (e.getPlayer() == null || e.getJoinMessage() == null) return;
		
		if (!hasPermission(e.getPlayer(), "join")) return;
		
		String tsMsg = BukkitSpeak.getStringManager().getMessage("LoginMessage");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getJoinMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
		
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
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getQuitMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
		
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
		
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", e.getPlayer().getName());
		repl.put("%player_displayname%", e.getPlayer().getDisplayName());
		repl.put("%msg%", e.getLeaveMessage());
		
		tsMsg = replaceKeys(tsMsg, repl);
		tsMsg = convert(tsMsg, true, BukkitSpeak.getStringManager().getAllowLinks());
		
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
