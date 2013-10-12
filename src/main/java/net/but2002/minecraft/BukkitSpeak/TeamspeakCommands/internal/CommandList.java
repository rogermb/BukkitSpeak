package net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.internal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.TeamspeakCommandSender;

public class CommandList extends TeamspeakCommand {
	
	public CommandList() {
		super("list");
	}
	
	@Override
	public void execute(TeamspeakCommandSender sender, String[] args) {
		StringBuilder sb = new StringBuilder("Currently online:");
		Player[] players = Bukkit.getOnlinePlayers();
		
		if (players.length > 0) {
			sb.append("\n");
			for (Player p : players) {
				sb.append(p.getName());
				sb.append(", ");
			}
			sb.delete(sb.length() - 2, sb.length());
		} else {
			sb.append(" -");
		}
		
		sender.sendMessage(sb.toString());
	}
}
