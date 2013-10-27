package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.util.DateManager;

import org.bukkit.command.CommandSender;

public class CommandStatus extends BukkitSpeakCommand {
	
	public CommandStatus() {
		super("status", "version");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&eBukkitSpeak Version: &av" + BukkitSpeak.getInstance().getDescription().getVersion());
		if (BukkitSpeak.getQuery().isConnected()) {
			send(sender, Level.INFO, "&eTeamspeak Listener: &arunning");
			send(sender, Level.INFO, "&eRunning since: &a"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getStartedTime()));
			send(sender, Level.INFO, "&eSID = &a" + BukkitSpeak.getQuery().getCurrentQueryClientServerID()
					+ "&e, CID = &a" + BukkitSpeak.getQuery().getCurrentQueryClientChannelID()
					+ "&e, CLID = &a" + BukkitSpeak.getQuery().getCurrentQueryClientID());
		} else if (BukkitSpeak.getInstance().getStoppedTime() == null || BukkitSpeak.getInstance().getStartedTime() == null) {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &6connecting");
			if (BukkitSpeak.getInstance().getStartedTime() != null) {
				send(sender, Level.WARNING, "&eConnecting since: &6"
						+ DateManager.dateToString(BukkitSpeak.getInstance().getStartedTime()));
			}
		} else if (BukkitSpeak.getInstance().getLastStartedTime() == null) {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &4dead");
			send(sender, Level.WARNING, "&eListener started: &4"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getStartedTime()));
			send(sender, Level.WARNING, "&eStopped since: &4"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getStoppedTime()));
			send(sender, Level.WARNING, "&eUse &a/tsa reload &eto restart the listener!");
		} else if (BukkitSpeak.getInstance().getLastStoppedTime() == null) {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &6reconnecting");
			send(sender, Level.WARNING, "&eListener started: &4"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getStartedTime()));
			send(sender, Level.WARNING, "&eListener stopped: &4"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getStoppedTime()));
			send(sender, Level.WARNING, "&eReconnecting since: &6"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getLastStartedTime()));
			send(sender, Level.WARNING, "&eUse &a/tsa reload &eto restart the listener!");
		} else {
			send(sender, Level.WARNING, "&eTeamspeak Listener: &4dead");
			send(sender, Level.WARNING, "&eRunning since: &4"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getStartedTime()));
			send(sender, Level.WARNING, "&eStopped since: &4"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getStoppedTime()));
			send(sender, Level.WARNING, "&eLast reconnecting attempt: &4"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getLastStartedTime()));
			send(sender, Level.WARNING, "&eReconnecting failed: &4"
					+ DateManager.dateToString(BukkitSpeak.getInstance().getLastStoppedTime()));
			send(sender, Level.WARNING, "&eUse &a/tsa reload &eto restart the listener!");
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
