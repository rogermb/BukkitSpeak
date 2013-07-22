package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;

public class CommandHelp extends BukkitSpeakCommand {
	
	public CommandHelp() {
		super("help");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&aHelp");
		if (checkCommandPermission(sender, "list"))
			send(sender, Level.INFO, "&e/ts list &a- Displays who's currently on TeamSpeak.");
		if (checkCommandPermission(sender, "mute"))
			send(sender, Level.INFO, "&e/ts mute &a- Mutes / unmutes BukkitSpeak for you.");
		if (checkCommandPermission(sender, "broadcast") && BukkitSpeak.getStringManager().getUseTextServer())
			send(sender, Level.INFO, "&e/ts broadcast &a- Broadcast a global TS message.");
		if (checkCommandPermission(sender, "chat") && BukkitSpeak.getStringManager().getUseTextChannel())
			send(sender, Level.INFO, "&e/ts chat &a- Displays a message in the TS channel.");
		if (checkCommandPermission(sender, "pm") && BukkitSpeak.getStringManager().getUsePrivateMessages())
			send(sender, Level.INFO, "&e/ts pm &a- Sends a message to a certain client.");
		if (checkCommandPermission(sender, "reply") && BukkitSpeak.getStringManager().getUsePrivateMessages())
			send(sender, Level.INFO, "&e/ts r(eply) &a- Replies to a PM.");
		if (checkCommandPermission(sender, "poke"))
			send(sender, Level.INFO, "&e/ts poke &a- Pokes a client on Teamspeak.");
		if (checkCommandPermission(sender, "info"))
			send(sender, Level.INFO, "&e/ts info &a- Information about the TS server.");
		if (checkCommandPermission(sender, "admin"))
			send(sender, Level.INFO, "&e/ts admin &2or &e/tsa &2- BukkitSpeak admin commands.");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
