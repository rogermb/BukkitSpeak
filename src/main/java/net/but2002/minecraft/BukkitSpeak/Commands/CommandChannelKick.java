package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QueryKick;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CommandChannelKick extends BukkitSpeakCommand {
	
	public CommandChannelKick() {
		super("channelkick");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts channelkick client (message)");
			return;
		} else if (!BukkitSpeak.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		HashMap<String, String> client;
		try {
			client = BukkitSpeak.getClientList().getByPartialName(args[1]);
			if (client == null) {
				send(sender, Level.WARNING, "&4Can't find the user you want to kick from the channel.");
				return;
			} else if (Integer.valueOf(client.get("cid")) != BukkitSpeak.getQuery().getCurrentQueryClientChannelID()) {
				send(sender, Level.WARNING, "&4The client is not in the channel!");
				return;
			}
		} catch (IllegalArgumentException e) {
			send(sender, Level.WARNING, "&4There are more than one clients matching &e" + args[1] + "&4.");
			return;
		}
		
		String tsMsg = Messages.MC_COMMAND_CHANNEL_KICK_TS.get();
		String mcMsg = Messages.MC_COMMAND_CHANNEL_KICK_MC.get();
		String msg = Configuration.TS_DEFAULT_REASON.getString();
		if (args.length > 2) {
			msg = combineSplit(2, args, " ");
		}
		
		Replacer r = new Replacer().addSender(sender).addTargetClient(client).addMessage(msg);
		tsMsg = MessageUtil.toTeamspeak(r.replace(tsMsg), false, Configuration.TS_ALLOW_LINKS.getBoolean());
		mcMsg = r.replace(mcMsg);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		if (tsMsg.length() > TS_MAXLENGHT) {
			send(sender, Level.WARNING, "&4The message is too long! (> 100 characters)");
			return;
		}
		
		Integer i = Integer.valueOf(client.get("clid"));
		QueryKick qk = new QueryKick(i, true, tsMsg);
		Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qk);
		broadcastMessage(mcMsg, sender);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length != 2) return null;
		List<String> al = new ArrayList<String>();
		for (HashMap<String, String> client : BukkitSpeak.getClientList().getClients().values()) {
			String n = client.get("client_nickname").replaceAll(" ", "");
			if (n.toLowerCase().startsWith(args[1].toLowerCase())) {
				al.add(n);
			}
		}
		return al;
	}
}
