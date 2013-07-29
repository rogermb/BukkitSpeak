package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMute extends BukkitSpeakCommand {
	
	public CommandMute() {
		super("mute");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (BukkitSpeak.getMuted((Player) sender)) {
				BukkitSpeak.setMuted((Player) sender, false);
				
				String mcMsg = BukkitSpeak.getStringManager().getMessage("Unmute");
				mcMsg = new Replacer().addPlayer((Player) sender).replace(mcMsg);
				
				if (mcMsg == null || mcMsg.isEmpty()) return;
				send(sender, Level.INFO, mcMsg);
			} else {
				BukkitSpeak.setMuted((Player) sender, true);
				
				String mcMsg = BukkitSpeak.getStringManager().getMessage("Mute");
				mcMsg = new Replacer().addPlayer((Player) sender).replace(mcMsg);
				
				if (mcMsg == null || mcMsg.isEmpty()) return;
				send(sender, Level.INFO, mcMsg);
			}
		} else {
			send(sender, Level.INFO, "Can only mute BukkitSpeak for players!");
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
