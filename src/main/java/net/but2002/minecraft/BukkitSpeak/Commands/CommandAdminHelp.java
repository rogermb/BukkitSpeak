package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;

public class CommandAdminHelp extends BukkitSpeakCommand {
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&2Admin Commands Help");
		if (checkCommandPermission(sender, "kick"))
			send(sender, Level.INFO, "&e/tsa kick target (reason) &2- Kicks from the TS.");
		if (checkCommandPermission(sender, "ban"))
			send(sender, Level.INFO, "&e/tsa ban target (reason) &2- Bans a client.");
		if (checkCommandPermission(sender, "channelkick"))
			send(sender, Level.INFO, "&e/tsa channelkick target (reason) &2- Kicks from the channel and moves the client to the default channel.");
		if (checkCommandPermission(sender, "set"))
			send(sender, Level.INFO, "&e/tsa set (property) (value) &a- Change BukkitSpeak's config.");
		if (checkCommandPermission(sender, "status"))
			send(sender, Level.INFO, "&e/tsa status &a- Shows some info about BukkitSpeak.");
		if (checkCommandPermission(sender, "reload"))
			send(sender, Level.INFO, "&e/tsa reload &2- Reloads the config and the query.");
	}
}
