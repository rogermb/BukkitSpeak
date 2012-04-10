package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPm extends BukkitSpeakCommand {
	
	public CommandPm(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 3) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts pm target message");
			return;
		} else if (!ts.getAlive()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		} 
		
		TeamspeakUser user = ts.getUserByPartialName(args[1]);
		if (user == null) {
			send(sender, Level.WARNING, "&4Can't find the user you want to PM.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 2, args.length)) {
			sb.append(s);
			sb.append(" ");
		}
		String SenderName, msg;
		msg = filterLinks(sb.toString(), stringManager.getAllowLinks());
		if (msg.isEmpty()) return;
		if (sender instanceof Player) {
			SenderName = sender.getName();
		} else {
			SenderName = stringManager.getTeamspeakNickname();
		}
		ts.pushMessage("sendtextmessage targetmode=1" 
				+ " target=" + user.getID()
				+ " msg=" + convert(msg), SenderName);
		
		String message = stringManager.getMessage("Pm");
		HashMap<String, String> repl = new HashMap<String, String>();
		repl.put("%target%", user.getName());
		repl.put("%msg%", msg);
		repl.put("(\\[URL]|\\[/URL])", "");
		
		message = replaceKeys(message, true, repl);
		
		send(sender, Level.INFO, message);
	}
}
