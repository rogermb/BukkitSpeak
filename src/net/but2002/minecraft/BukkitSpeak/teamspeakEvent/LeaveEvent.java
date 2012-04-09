package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class LeaveEvent extends TeamspeakEvent{
	
	public LeaveEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("cfid");
		localKeys.add("ctid");
		localKeys.add("reasonid");
		localKeys.add("reasonmsg");
		localKeys.add("clid");
		parseLocalValues(msg);
		
		try {
			setUser(plugin.getTs().getUserByID(Integer.parseInt(localValues.get("clid"))));
		} catch(Exception e) {
			plugin.getLogger().info("Could not identify user.");
			return;
		}
		
		if (plugin.getStringManager().getUseServer()) sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		if(user != null && !getUser().getName().startsWith("Unknown from") && getUser().getClientType() == 0) {
			String m = plugin.getStringManager().getMessage("Quit");
			for (Player pl : plugin.getServer().getOnlinePlayers()) {
				if (!plugin.getMuted(pl) && CheckPermissions(pl, "leave")) {
					pl.sendMessage(replaceValues(m, true));
				}
			}
			plugin.getLogger().info(replaceValues(m, false));
		}
	}
}
