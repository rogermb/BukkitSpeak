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
			sendTooFewArgumentsMessage(sender, Messages.MC_COMMAND_CHANNEL_KICK_USAGE.get());
			return;
		}

		if (!isConnected(sender)) return;

		HashMap<String, String> client = getClient(args[1], sender);
		if (client == null) {
			return;
		} else if (Integer.valueOf(client.get("cid")) != BukkitSpeak.getQuery().getCurrentQueryClientChannelID()) {
			String notInChannel = Messages.MC_COMMAND_CHANNEL_KICK_NOT_IN_CHANNEL.get();
			notInChannel = new Replacer().addSender(sender).addTargetClient(client).replace(notInChannel);
			send(sender, Level.WARNING, notInChannel);
			return;
		}

		String tsMsg = Messages.MC_COMMAND_CHANNEL_KICK_TS.get();
		String mcMsg = Messages.MC_COMMAND_CHANNEL_KICK_MC.get();
		String msg = Messages.MC_COMMAND_DEFAULT_REASON.get();
		if (args.length > 2) {
			msg = combineSplit(2, args, " ");
		}

		Replacer r = new Replacer().addSender(sender).addTargetClient(client).addMessage(msg);
		tsMsg = MessageUtil.toTeamspeak(r.replace(tsMsg), false, Configuration.TS_ALLOW_LINKS.getBoolean());
		mcMsg = r.replace(mcMsg);

		if (tsMsg == null || tsMsg.isEmpty()) return;
		if (tsMsg.length() > TS_MAXLENGHT) {
			String tooLong = Messages.MC_COMMAND_ERROR_MESSAGE_TOO_LONG.get();
			tooLong = new Replacer().addSender(sender).addTargetClient(client).replace(tooLong);
			send(sender, Level.WARNING, tooLong);
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
