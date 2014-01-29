package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;

import org.bukkit.command.CommandSender;

public class CommandInfo extends BukkitSpeakCommand {

	public CommandInfo() {
		super("info");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!isConnected(sender)) return;

		if (Integer.valueOf(Configuration.MAIN_SERVERPORT.getInt()) > 0) {
			StringBuilder sb = new StringBuilder();

			sb.append("&aTeamspeak IP: &e").append(Configuration.MAIN_IP.getString());
			sb.append(":").append(Configuration.MAIN_SERVERPORT.getInt());

			send(sender, Level.INFO, sb.toString());
		} else {
			int port = -(Integer.valueOf(Configuration.MAIN_SERVERPORT.getInt()));
			StringBuilder sb = new StringBuilder();

			sb.append("&aTeamspeak IP: &e").append(Configuration.MAIN_IP.getString());
			sb.append(", Virtual Server ID: ").append(String.valueOf(port));

			send(sender, Level.INFO, sb.toString());
		}
		send(sender, Level.INFO, "&aClients online: &e" + BukkitSpeak.getClientList().size());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
