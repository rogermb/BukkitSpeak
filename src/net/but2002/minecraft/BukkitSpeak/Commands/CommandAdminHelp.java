package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;

public class CommandAdminHelp extends BukkitSpeakCommand {
	
	public CommandAdminHelp(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&2Admin Commands Help");
		if (CheckCommandPermission(sender, "kick"))
			send(sender, Level.INFO, "&e/tsa kick target (reason) &2- Kicks from the TS.");
		if (CheckCommandPermission(sender, "ban"))
			send(sender, Level.INFO, "&e/tsa ban target (reason) &2- Bans a client.");
		if (CheckCommandPermission(sender, "channelkick"))
			send(sender, Level.INFO, "&e/tsa channelkick target (reason) &2- Kicks from the channel and moves the client to the default channel.");
		if (CheckCommandPermission(sender, "status"))
			send(sender, Level.INFO, "&e/tsa status &a- Shows some info about BukkitSpeak.");
		if (CheckCommandPermission(sender, "reload"))
			send(sender, Level.INFO, "&e/tsa reload &2- Reloads the config and the query.");
	}
}
