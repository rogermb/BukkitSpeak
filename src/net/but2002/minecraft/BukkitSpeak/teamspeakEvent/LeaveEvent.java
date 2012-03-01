package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class LeaveEvent extends TeamspeakEvent{

	public LeaveEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("reasonmsg");
		localKeys.add("clid");
		
		parseLocalValues(msg);
		try {
		setUser(plugin.getTs().getUserByID(Integer.parseInt(localValues.get("clid"))));
		} catch(Exception e) {
			plugin.getServer().getLogger().info(plugin + "Could not identify user. May have logged off.");
			return;
		}
		sendMessage();
		
	}
	
	@Override
	protected void sendMessage() {
		if(user != null && !getUser().getName().startsWith("Unknown from") && getUser().getClientType() != 1) {
			String message = replaceValues(plugin.getStringManager().getMessage("msg_quit"), true);
			plugin.getServer().broadcastMessage(message);
		}
	}
}