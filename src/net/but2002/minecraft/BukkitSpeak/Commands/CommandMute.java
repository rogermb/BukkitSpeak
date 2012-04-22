package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.HashMap;
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
				
				String message = stringManager.getMessage("Unmute");
				String Name, DisplayName;
				Name = ((Player) sender).getName();
				DisplayName = ((Player) sender).getDisplayName();
				
				HashMap<String, String> repl = new HashMap<String, String>();
				repl.put("%player_name%", Name);
				repl.put("%player_displayname%", DisplayName);
				
				message = replaceKeys(message, repl);
				
				send(sender, Level.INFO, message);
			} else {
				plugin.setMuted((Player) sender, true);
				
				String message = stringManager.getMessage("Mute");
				String Name, DisplayName;
				Name = ((Player) sender).getName();
				DisplayName = ((Player) sender).getDisplayName();
				
				HashMap<String, String> repl = new HashMap<String, String>();
				repl.put("%player_name%", Name);
				repl.put("%player_displayname%", DisplayName);
				
				message = replaceKeys(message, repl);
				
				send(sender, Level.INFO, message);
			}
		} else {
			send(sender, Level.INFO, "Can only mute BukkitSpeak for players!");
		}
	}
}
