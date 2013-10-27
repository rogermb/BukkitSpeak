package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import org.bukkit.entity.Player;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class CommandPm extends BukkitSpeakCommand {
	
	public CommandPm() {
		super("pm");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!Configuration.TS_ENABLE_PRIVATE_MESSAGES.getBoolean()) {
			send(sender, Level.WARNING, "&4You need to enable ListenToPrivateMessages in the config to use this command.");
			return;
		}
		
		if (args.length < 3) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts pm target message");
			return;
		}
		
		if (!isConnected(sender)) return;
		
		HashMap<String, String> client = getClient(args[1], sender);
		if (client == null) return;
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 2, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		String tsMsg = Messages.MC_COMMAND_PM_TS.get();
		String mcMsg = Messages.MC_COMMAND_PM_MC.get();
		String name;
		if (sender instanceof Player) {
			name = ((Player) sender).getName();
		} else {
			name = MessageUtil.toMinecraft(Configuration.TS_CONSOLE_NAME.getString(), false, false);
		}
		
		Replacer r = new Replacer().addSender(sender).addTargetClient(client).addMessage(sb.toString());
		tsMsg = MessageUtil.toTeamspeak(r.replace(tsMsg), true, Configuration.TS_ALLOW_LINKS.getBoolean());
		mcMsg = r.replace(mcMsg);
		
		if (tsMsg == null || tsMsg.isEmpty()) return;
		Integer i = Integer.valueOf(client.get("clid"));
		QuerySender qs = new QuerySender(i, JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, tsMsg);
		Bukkit.getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), qs);
		BukkitSpeak.registerRecipient(name, i);
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
