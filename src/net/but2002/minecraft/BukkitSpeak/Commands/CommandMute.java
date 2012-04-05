package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMute extends BukkitSpeakCommand {
	
	public CommandMute(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (plugin.getMuted((Player) sender)) {
				plugin.setMuted((Player) sender, false);
				send(sender, Level.INFO, stringManager.getMessage("Unmute"));
			} else {
				plugin.setMuted((Player) sender, true);
				send(sender, Level.INFO, stringManager.getMessage("Mute"));
			}
		} else {
			send(sender, Level.INFO, "Can only mute BukkitSpeak for players!");
		}
	}
}
