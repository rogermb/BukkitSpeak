package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.HashMap;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;
import net.but2002.minecraft.BukkitSpeak.TeamspeakHandler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BukkitSpeakCommand {
	
	BukkitSpeak plugin;
	StringManager stringManager;
	TeamspeakHandler ts;
	
	public BukkitSpeakCommand(BukkitSpeak plugin) {
		this.plugin = plugin;
		stringManager = plugin.getStringManager();
		ts = plugin.getTs();
	}
	
	protected void send(CommandSender sender, Level level, String msg) {
		if (sender instanceof Player) {
			msg = msg.replaceAll("(&([a-fk-orA-FK-OR0-9]))", "§$2").replaceAll("($([a-fk-orA-FK-OR0-9]))", "§$2");
			sender.sendMessage(plugin + msg);
		} else {
			msg = msg.replaceAll("(&([a-fk-orA-FK-OR0-9]))", "").replaceAll("($([a-fk-orA-FK-OR0-9]))", "");
			plugin.getLogger().log(level, msg);
		}
	}
	
	public static String convertToTeamspeak(String input, Boolean color, Boolean links) {
		if (input != null) {
			String s = input;
			s = s.replaceAll("\\s", "\\\\s");
			s = s.replaceAll("/", "\\\\/");
			s = s.replaceAll("\\|", "\\\\p");
			if (color) {
				//TODO: ADD COLORS!
			} else {
				s = s.replaceAll("((&|$|§)([a-fk-orA-FK-OR0-9]))", "");
			}
			if (links) {
				s = s.replaceAll("(?i)((http://|ftp://).*\\.?.+\\..+(/.*)?)", "\\[URL]$1\\[/URL]");
			} else {
				s = s.replaceAll("(?i)((http://|ftp://).*\\.?.+\\..+(/.*)?)", "");
			}
			return s;
		}
		return null;
	}
	
	public static String convertToMinecraft(String input, Boolean color, Boolean links) {
		if (input != null) {
			String s = input;
			if (color) {
				s = s.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "§$3");
			} else {
				s = s.replaceAll("((&|$|§)([a-fk-orA-FK-OR0-9]))", "");
			}
			if (!links) {
				s = s.replaceAll("(?i)((http://|ftp://).*\\.?.+\\..+(/.*)?)", "");
			}
			return s;
		}
		return null;
	}
	
	public static String replaceKeys(String input, HashMap<String, String> repl) {
		if (input != null) {
			String s = input;
			for (String key : repl.keySet()) {
				s = s.replaceAll(key, repl.get(key));
			}
			return s;
		}
		return null;
	}
	
	public abstract void execute(CommandSender sender, String[] args);
	
}
