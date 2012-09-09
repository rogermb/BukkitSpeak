package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;

public class CommandInfo extends BukkitSpeakCommand {
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!BukkitSpeak.getQuery().isConnected() || BukkitSpeak.getClients() == null) {
			send(sender, Level.WARNING, "&4Can't communicate with the TeamSpeak server.");
			return;
		}
		
		if (Integer.valueOf(BukkitSpeak.getStringManager().getServerPort()) > 0) {
			StringBuilder sb = new StringBuilder();
			
			sb.append("&aTeamspeak IP: &e").append(BukkitSpeak.getStringManager().getIp());
			sb.append(":").append(BukkitSpeak.getStringManager().getServerPort());
			
			send(sender, Level.INFO, sb.toString());
		} else {
			int port = -(Integer.valueOf(BukkitSpeak.getStringManager().getServerPort()));
			StringBuilder sb = new StringBuilder();
			
			sb.append("&aTeamspeak IP: &e").append(BukkitSpeak.getStringManager().getIp());
			sb.append(", Virtual Server ID: ").append(String.valueOf(port));
			
			send(sender, Level.INFO, sb.toString());
		}
		send(sender, Level.INFO, "&aClients online: &e" + BukkitSpeak.getClients().size());
	}
}
