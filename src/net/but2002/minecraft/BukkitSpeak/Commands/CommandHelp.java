package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;

public class CommandHelp extends BukkitSpeakCommand {
	
	public CommandHelp(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&aHelp");
		if (CheckCommandPermission(sender, "list"))
			send(sender, Level.INFO, "&e/ts list &a- Displays who's currently on TeamSpeak.");
		if (CheckCommandPermission(sender, "mute"))
			send(sender, Level.INFO, "&e/ts mute &a- Mutes / unmutes BukkitSpeak for you.");
		if (CheckCommandPermission(sender, "broadcast") && stringManager.getUseTextServer())
			send(sender, Level.INFO, "&e/ts broadcast &a- Broadcast a global TS message.");
		if (CheckCommandPermission(sender, "chat") && stringManager.getUseTextChannel())
			send(sender, Level.INFO, "&e/ts chat &a- Displays a message in the TS channel.");
		if (CheckCommandPermission(sender, "pm") && stringManager.getUsePrivateMessages())
			send(sender, Level.INFO, "&e/ts pm &a- Sends a message to a certain client.");
		if (CheckCommandPermission(sender, "poke"))
			send(sender, Level.INFO, "&e/ts poke &a- Pokes a client on Teamspeak.");
		if (CheckCommandPermission(sender, "info"))
			send(sender, Level.INFO, "&e/ts info &a- Information about the TS server.");
		if (CheckCommandPermission(sender, "status"))
			send(sender, Level.INFO, "&e/ts status &a- Shows some info about BukkitSpeak.");
		if (CheckCommandPermission(sender, "admin"))
			send(sender, Level.INFO, "&e/ts admin &2or &e/tsa &2- BukkitSpeak admin commands.");
	}
}
