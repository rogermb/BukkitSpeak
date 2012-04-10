package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBroadcast extends BukkitSpeakCommand {
	
	public CommandBroadcast(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 2) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts broadcast message");
			return;
		} else if (!ts.getAlive()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		String SenderName, DisplayName, msg;
		msg = filterLinks(sb.toString(), stringManager.getAllowLinks());
		if (msg.isEmpty()) return;
		if (sender instanceof Player) {
			SenderName = sender.getName();
			DisplayName = ((Player) sender).getDisplayName();
		} else {
			SenderName = stringManager.getTeamspeakNickname();
			DisplayName = "&eServer";
		}
		ts.pushMessage("sendtextmessage targetmode=3" 
				+ " target=0"
				+ " msg=" + convert(msg), SenderName);
		
		String message = stringManager.getMessage("Broadcast");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%player_name%", DisplayName);
		repl.put("%msg%", msg);
		repl.put("(\\[URL]|\\[/URL])", "");
		
		for (Player pl : plugin.getServer().getOnlinePlayers()) {
			if (!plugin.getMuted(pl)) pl.sendMessage(replaceKeys(message, true, repl));
		}
		plugin.getLogger().info(replaceKeys(message, false, repl));
	}
}
