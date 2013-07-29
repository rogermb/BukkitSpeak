package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.List;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReload extends BukkitSpeakCommand {
	
	public CommandReload() {
		super("reload");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		String t = BukkitSpeak.getInstance().toString();
		if (sender instanceof Player) {
			if (BukkitSpeak.getInstance().reload()) {
				sender.sendMessage(t + ChatColor.GREEN + "reloaded.");
			} else {
				sender.sendMessage(t + ChatColor.RED + "was unable to reload, an error happened.");
			}
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
