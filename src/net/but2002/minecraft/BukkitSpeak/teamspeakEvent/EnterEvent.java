package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

public class EnterEvent extends TeamspeakEvent{
	
	public EnterEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("cfid");
		localKeys.add("reasonid");
		localKeys.add("client_country");
		parseLocalValues(msg);
		
		setUser(new TeamspeakUser(removeLocalKeys(msg)));
		
		if (plugin.getStringManager().getUseServer()) sendMessage();
	}
	
	protected void sendMessage() {
		if (!getUser().getName().startsWith("Unknown from") && getUser().getClientType() == 0) {
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
