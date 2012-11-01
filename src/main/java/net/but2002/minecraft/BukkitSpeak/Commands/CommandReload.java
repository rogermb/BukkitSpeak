package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.List;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReload extends BukkitSpeakCommand {
	
	private static final String[] NAMES = {"reload"};
	
	@Override
	public String getName() {
		return NAMES[0];
	}
	
	@Override
	public String[] getNames() {
		return NAMES;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		String t = BukkitSpeak.getInstance().toString();
		if (BukkitSpeak.getInstance().reload()) {
			if (sender instanceof Player) {
				sender.sendMessage(t + ChatColor.GREEN + "reloaded.");
			}
		} else {
			if (sender instanceof Player) {
				sender.sendMessage(t + ChatColor.RED + "was unable to reload, an error happened.");
			}
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
