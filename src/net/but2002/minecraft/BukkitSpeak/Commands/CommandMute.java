package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMute extends BukkitSpeakCommand {
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (BukkitSpeak.getMuted((Player) sender)) {
				BukkitSpeak.setMuted((Player) sender, false);
				
				String message = BukkitSpeak.getStringManager().getMessage("Unmute");
				String Name, DisplayName;
				Name = ((Player) sender).getName();
				DisplayName = ((Player) sender).getDisplayName();
				
				HashMap<String, String> repl = new HashMap<String, String>();
				repl.put("%player_name%", Name);
				repl.put("%player_displayname%", DisplayName);
				
				message = replaceKeys(message, repl);
				
				if (message == null || message.isEmpty()) return;
				send(sender, Level.INFO, message);
			} else {
				BukkitSpeak.setMuted((Player) sender, true);
				
				String message = BukkitSpeak.getStringManager().getMessage("Mute");
				String Name, DisplayName;
				Name = ((Player) sender).getName();
				DisplayName = ((Player) sender).getDisplayName();
				
				HashMap<String, String> repl = new HashMap<String, String>();
				repl.put("%player_name%", Name);
				repl.put("%player_displayname%", DisplayName);
				
				message = replaceKeys(message, repl);
				
				if (message == null || message.isEmpty()) return;
				send(sender, Level.INFO, message);
			}
		} else {
			send(sender, Level.INFO, "Can only mute BukkitSpeak for players!");
		}
	}
}
