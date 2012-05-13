package net.but2002.minecraft.BukkitSpeak.Commands;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;

public class CommandInfo extends BukkitSpeakCommand {
	
	public CommandInfo(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (Integer.valueOf(stringManager.getServerPort()) > 0) {
			sender.sendMessage("Teamspeak IP: " + stringManager.getIp() + ":" + stringManager.getServerPort());
		} else {
			sender.sendMessage("Teamspeak IP: " + stringManager.getIp() + ", Virtual Server ID: " + -(Integer.valueOf(stringManager.getServerPort())));
		}
		sender.sendMessage("Clients online: " + plugin.getClients().size());
	}
}
