package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHelp extends BukkitSpeakCommand {
	
	public CommandHelp(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&aHelp");
		if (CheckPermissions(sender, "list"))
			send(sender, Level.INFO, "&e/ts list &a- Displays who's currently on TeamSpeak.");
		if (CheckPermissions(sender, "mute"))
			send(sender, Level.INFO, "&e/ts mute &a- Mutes / unmutes BukkitSpeak for you.");
		if (CheckPermissions(sender, "broadcast") && stringManager.getUseTextServer())
			send(sender, Level.INFO, "&e/ts broadcast &a- Broadcast a global TS message.");
		if (CheckPermissions(sender, "chat") && stringManager.getUseTextChannel())
			send(sender, Level.INFO, "&e/ts chat &a- Displays a message only in the TS channel.");
		if (CheckPermissions(sender, "pm") && stringManager.getUsePrivateMessages())
			send(sender, Level.INFO, "&e/ts pm &a- Sends a certain person on TS a message.");
		if (CheckPermissions(sender, "poke"))
			send(sender, Level.INFO, "&e/ts poke &a- Pokes a client on Teamspeak.");
		if (CheckPermissions(sender, "status"))
			send(sender, Level.INFO, "&e/ts status &a- Shows some info about BukkitSpeak.");
		if (CheckPermissions(sender, "reload"))
			send(sender, Level.INFO, "&e/ts reload &a- Reloads the config and the listener.");
	}
	
	public Boolean CheckPermissions(CommandSender sender, String perm) {
		if (sender instanceof Player) {
			return sender.hasPermission("bukkitspeak.commands." + perm); 
		} else {
			return true;
		}
	}
}
