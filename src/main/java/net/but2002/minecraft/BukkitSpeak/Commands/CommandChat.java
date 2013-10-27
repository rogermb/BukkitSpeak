package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QuerySender;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
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
		if (!isConnected(sender)) return;
		
		if (!Configuration.TS_ENABLE_CHANNEL_MESSAGES.getBoolean()) {
			send(sender, Level.WARNING, "&4You need to enable ListenToChannelChat in the config to use this command.");
			return;
		}
		
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts chat message");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String tsMsg = Messages.MC_COMMAND_CHAT_TS.get();
		String mcMsg = Messages.MC_COMMAND_CHAT_MC.get();
		
		Replacer r = new Replacer().addSender(sender).addMessage(sb.toString());
		tsMsg = MessageUtil.toTeamspeak(r.replace(tsMsg), true, Configuration.TS_ALLOW_LINKS.getBoolean());
		mcMsg = r.replace(mcMsg);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		QuerySender qs = new QuerySender(BukkitSpeak.getQuery().getCurrentQueryClientChannelID(),
				JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, tsMsg);
		Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitSpeak.getInstance(), qs);
		broadcastMessage(mcMsg, sender);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
