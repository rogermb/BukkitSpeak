package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.util.DateManager;

import org.bukkit.command.CommandSender;

public class CommandStatus extends BukkitSpeakCommand {
	
	public CommandStatus(BukkitSpeak plugin) {
		super(plugin);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&eBukkitSpeak Version: &av" + plugin.getDescription().getVersion());
		if (plugin.getQuery().isConnected()) {
			send(sender, Level.INFO, "&eTeamspeak Listener: &arunning");
			if (plugin.getStartedTime() != null) send(sender, Level.INFO, "&eRunning since: &a" + DateManager.DateToString(plugin.getStartedTime()));
		} else if (plugin.getStoppedTime() == null) {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &6connecting");
			if (plugin.getStartedTime() != null) send(sender, Level.WARNING, "&eRunning since: &6" + DateManager.DateToString(plugin.getStartedTime()));
		} else {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &4dead");
			if (plugin.getStartedTime() != null) send(sender, Level.WARNING, "&eRunning since: &4" + DateManager.DateToString(plugin.getStartedTime()));
			if (plugin.getStoppedTime() != null) send(sender, Level.WARNING, "&eStopped since: &4" + DateManager.DateToString(plugin.getStoppedTime()));
			send(sender, Level.WARNING, "&eUse &a/tsa reload &eto restart the listener!");
		}
	}
}
