package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandChat extends BukkitSpeakCommand {
	
	public CommandChat(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 1) {
			send(sender, Level.WARNING, "&aToo few arguments!");
			send(sender, Level.WARNING, "&aUsage: /ts chat message");
			return;
		} else if (!ts.getAlive()) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (String s : Arrays.copyOfRange(args, 1, args.length)) {
			sb.append(s);
			sb.append("\\s");
		}
		String SenderName;
		if (sender instanceof Player) {
			SenderName = sender.getName();
		} else {
			SenderName = stringManager.getTeamspeakNickname();
		}
		ts.pushMessage("sendtextmessage targetmode=2 target=" + stringManager.getChannelID() + " msg=" + sb.toString(), SenderName);
	}
}
