package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QuerySender;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class CommandChat extends BukkitSpeakCommand {
	
	public CommandChat() {
		super("chat");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!BukkitSpeak.getStringManager().getUseTextChannel()) {
			send(sender, Level.WARNING, "&4You need to enable ListenToChannelChat in the config to use this command.");
			return;
		}
		
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts chat message");
			return;
		} else if (!BukkitSpeak.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String tsMsg = BukkitSpeak.getStringManager().getMessage("ChannelMessage");
		String mcMsg = BukkitSpeak.getStringManager().getMessage("Chat");
		
		Replacer r = new Replacer().addSender(sender).addMessage(sb.toString());
		tsMsg = MessageUtil.toTeamspeak(r.replace(tsMsg), true, BukkitSpeak.getStringManager().getAllowLinks());
		mcMsg = r.replace(mcMsg);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		QuerySender qs = new QuerySender(BukkitSpeak.getStringManager().getChannelID(),
				JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitSpeak.getInstance(), qs);
		broadcastMessage(mcMsg, sender);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
