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
		if (CheckCommandPermission(sender, "reload"))
			send(sender, Level.INFO, "&e/tsa reload &2- Reloads the config and the query.");
	}
}
