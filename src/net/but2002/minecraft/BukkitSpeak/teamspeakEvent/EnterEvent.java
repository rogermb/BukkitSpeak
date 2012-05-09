package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class EnterEvent extends TeamspeakEvent{
	
	public EnterEvent(BukkitSpeak plugin, HashMap<String, String> info) {
		super(plugin, info);
		setUser(Integer.valueOf(info.get("clid")));
	}
	
	protected void sendMessage() {
		if (!getClientName().startsWith("Unknown from") && getClientType() == 0) {
			String m = plugin.getStringManager().getMessage("Join");
			for (Player pl : plugin.getServer().getOnlinePlayers()) {
				if (!plugin.getMuted(pl) && CheckPermissions(pl, "join")) {
					pl.sendMessage(replaceValues(m, true));
				}
			}
			plugin.getLogger().info(replaceValues(m, false));
		}
	}
}
