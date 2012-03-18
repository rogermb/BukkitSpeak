package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

public class EnterEvent extends TeamspeakEvent{
	
	public EnterEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("cfid");
		localKeys.add("ctid");
		localKeys.add("reasonid");
		parseLocalValues(msg);
		
		setUser(new TeamspeakUser(removeLocalKeys(msg)));
		sendMessage();
	}
	
	protected void sendMessage() {
		if (!getUser().getName().startsWith("Unknown from") && getUser().getClientType() == 0) {
			String message = replaceValues(plugin.getStringManager().getMessage("Join"), true);
			for (Player pl : plugin.getServer().getOnlinePlayers()) {
				if (!plugin.getMuted(pl) && CheckPermissions(pl, "join")) pl.sendMessage(message);
			}
			plugin.getLogger().info(message);
		}
	}
}
