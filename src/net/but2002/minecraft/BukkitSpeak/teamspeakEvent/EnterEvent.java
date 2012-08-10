package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class EnterEvent extends TeamspeakEvent{
	
	public EnterEvent(BukkitSpeak plugin, HashMap<String, String> info) {
		super(plugin, Integer.valueOf(info.get("clid")));
		sendMessage();
	}
	
	protected void sendMessage() {
		String m = plugin.getStringManager().getMessage("Join");
		if (m.isEmpty()) return;
		for (Player pl : plugin.getServer().getOnlinePlayers()) {
			if (!plugin.getMuted(pl) && checkPermissions(pl, "join")) {
				pl.sendMessage(replaceValues(m, true));
			}
		}
		if (plugin.getStringManager().getLogInConsole()) plugin.getLogger().info(replaceValues(m, false));
	}
}
