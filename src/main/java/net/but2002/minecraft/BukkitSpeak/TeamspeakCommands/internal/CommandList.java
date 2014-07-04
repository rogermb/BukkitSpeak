package net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.internal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;
import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;
import net.but2002.minecraft.BukkitSpeak.TeamspeakCommands.TeamspeakCommandSender;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

public class CommandList extends TeamspeakCommand {

	public CommandList() {
		super("list");
	}

	@Override
	public void execute(TeamspeakCommandSender sender, String[] args) {
		StringBuilder online = new StringBuilder();
		Player[] players = Bukkit.getOnlinePlayers();

		if (players.length > 0) {
			for (Player p : players) {
				online.append(p.getName());
				online.append(", ");
			}
			online.delete(online.length() - 2, online.length());
		} else {
			online.append(" -");
		}

		String tsMsg = Messages.TS_COMMAND_LIST.get();
		String list = online.toString();

		tsMsg = new Replacer().addSender(sender).addList(list).addCount(players.length).replace(tsMsg);
		tsMsg = MessageUtil.toTeamspeak(tsMsg, true, Configuration.TS_ALLOW_LINKS.getBoolean());

		if (tsMsg.isEmpty()) return;
		sender.sendMessage(tsMsg);
	}
}
