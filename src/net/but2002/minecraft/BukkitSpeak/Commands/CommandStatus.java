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
		if (ts.getAlive()) {
			send(sender, Level.INFO, "&eTeamspeak Listener: &arunning");
			if (ts.getStarted() != null) send(sender, Level.INFO, "&eRunning since: &a" + DateManager.DateToString(ts.getStarted()));
		} else {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &4dead");
			if (ts.getStarted() != null) send(sender, Level.WARNING, "&eRunning since: &4" + DateManager.DateToString(ts.getStarted()));
			if (ts.getStopped() != null) send(sender, Level.WARNING, "&eStopped since: &4" + DateManager.DateToString(ts.getStopped()));
			send(sender, Level.WARNING, "&eUse &a/ts reload &eto restart the listener!");
		}
	}
}
