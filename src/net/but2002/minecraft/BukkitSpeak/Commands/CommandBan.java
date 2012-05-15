package net.but2002.minecraft.BukkitSpeak.Commands;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class CommandBan extends BukkitSpeakCommand {
	
	public CommandBan(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		if(args[0].equalsIgnoreCase("ban")){
			if (!CheckCommandPermission(sender, "ban")){
				//Need to check for default permission denial message.
				//sender.sendMessage(ChatColor.RED + "");
				return;
			}
			if (args.length < 1) {
				sender.sendMessage(ChatColor.GRAY + "/tsa ban {clientname} (reason)");
				return;
			}
			
			String name = args[1].toLowerCase();
			String banReason = config.getString("teamspeak.DefaultBan", "Not Sure");
			if (args.length > 3) {
				banReason = combineSplit(2, args, " ");
			}
			
			String newname = plugin.dquery.clientFind(name);
			if (plugin.dquery.banClient(name, banReason)) {
				sender.sendMessage(ChatColor.GRAY + newname + " was banned from TeamSpeak for " + banReason);
				return;
			} else {
				plugin.dquery.echoError();
				return;
			}
			
		}
	}
}
