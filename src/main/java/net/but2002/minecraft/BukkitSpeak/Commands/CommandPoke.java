package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.AsyncQueryUtils.QueryPoke;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPoke extends BukkitSpeakCommand {
	
	public CommandPoke() {
		super("poke");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 3) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts poke target message");
			return;
		} else if (!BukkitSpeak.getQuery().isConnected()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		HashMap<String, String> client;
		try {
			client = BukkitSpeak.getClientList().getByPartialName(args[1]);
			if (client == null) {
				send(sender, Level.WARNING, "&4Can't find the user you want to PM.");
				return;
			}
		} catch (IllegalArgumentException e) {
			send(sender, Level.WARNING, "&4There are more than one clients matching &e" + args[1] + "&4.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 2, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String tsMsg = Messages.MC_COMMAND_POKE_TS.get();
		String mcMsg = Messages.MC_COMMAND_POKE_MC.get();
		
		Replacer r = new Replacer().addSender(sender).addTargetClient(client).addMessage(sb.toString());
		tsMsg = MessageUtil.toTeamspeak(r.replace(tsMsg), true, Configuration.TS_ALLOW_LINKS.getBoolean());
		mcMsg = r.replace(mcMsg);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		if (tsMsg.length() > TS_MAXLENGHT) {
			send(sender, Level.WARNING, "&4The message is too long! (> 100 characters)");
			return;
		}
		
		Integer i = Integer.valueOf(client.get("clid"));
		QueryPoke qp = new QueryPoke(i, tsMsg);
		Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qp);
		if (mcMsg == null || mcMsg.isEmpty()) return;
		if (sender instanceof Player) {
			sender.sendMessage(MessageUtil.toMinecraft(mcMsg, true, Configuration.TS_ALLOW_LINKS.getBoolean()));
		} else {
			BukkitSpeak.log().info(MessageUtil.toMinecraft(mcMsg, false, Configuration.TS_ALLOW_LINKS.getBoolean()));
		}
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
