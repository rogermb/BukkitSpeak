package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.Arrays;
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
		}
		ts.pushMessage("sendtextmessage targetmode=3" 
				+ " target=0 msg=" + convert(sb.toString()), stringManager.getTeamspeakNickname());
		
		String message = stringManager.getMessage("Broadcast");
		message.replaceAll("%player_name%", stringManager.getTeamspeakNickname());
		message.replaceAll("%msg%", sb.toString());
		
		for (Player pl : plugin.getServer().getOnlinePlayers()) {
			if (!plugin.getMuted(pl)) pl.sendMessage(message);
		}
		plugin.getLogger().info(message);
	}
}
