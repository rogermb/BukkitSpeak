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
                
                if(Bukkit.getOnlinePlayers().size() > 0){
                    for (Player players : Bukkit.getOnlinePlayers()) {
                            online.append(players.getName());
                            online.append(", ");
                    }

                    online.delete(online.length() - 2, online.length());
                } else {
                    online.append(" -");
		}

		String tsMsg = Messages.TS_COMMAND_LIST.get();
		String list = online.toString();

		tsMsg = new Replacer().addSender(sender).addList(list).addCount(Bukkit.getOnlinePlayers().size()).replace(tsMsg);
		tsMsg = MessageUtil.toTeamspeak(tsMsg, true, Configuration.TS_ALLOW_LINKS.getBoolean());

		if (tsMsg.isEmpty()) return;
		sender.sendMessage(tsMsg);
	}
}
