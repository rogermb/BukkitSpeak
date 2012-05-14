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
			send(sender, Level.INFO, "&eRunning since: &a" + DateManager.DateToString(plugin.getStartedTime()));
		} else if (plugin.getStoppedTime() == null || plugin.getStartedTime() == null) {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &6connecting");
			if (plugin.getStartedTime() != null) send(sender, Level.WARNING, "&eConnecting since: &6" + DateManager.DateToString(plugin.getStartedTime()));
		} else if (plugin.getLastStartedTime() == null) {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &4dead");
			send(sender, Level.WARNING, "&eListener started: &4" + DateManager.DateToString(plugin.getStartedTime()));
			send(sender, Level.WARNING, "&eStopped since: &4" + DateManager.DateToString(plugin.getStoppedTime()));
			send(sender, Level.WARNING, "&eUse &a/tsa reload &eto restart the listener!");
		} else if (plugin.getLastStoppedTime() == null) {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &4reconnecting");
			send(sender, Level.WARNING, "&eListener started: &4" + DateManager.DateToString(plugin.getStartedTime()));
			send(sender, Level.WARNING, "&eListener stopped: &4" + DateManager.DateToString(plugin.getStoppedTime()));
			send(sender, Level.WARNING, "&eReconnecting since: &4" + DateManager.DateToString(plugin.getLastStartedTime()));
			send(sender, Level.WARNING, "&eUse &a/tsa reload &eto restart the listener!");
		} else {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &4dead");
			send(sender, Level.WARNING, "&eRunning since: &4" + DateManager.DateToString(plugin.getStartedTime()));
			send(sender, Level.WARNING, "&eStopped since: &4" + DateManager.DateToString(plugin.getStoppedTime()));
			send(sender, Level.WARNING, "&eLast reconnecting attempt: &4" + DateManager.DateToString(plugin.getLastStartedTime()));
			send(sender, Level.WARNING, "&eReconnecting failed: &4" + DateManager.DateToString(plugin.getLastStoppedTime()));
			send(sender, Level.WARNING, "&eUse &a/tsa reload &eto restart the listener!");
		}
	}
}
