package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

public class ErrorEvent extends TeamspeakEvent {
	
	public ErrorEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("id");
		localKeys.add("msg");
		parseLocalValues(msg);
		
		sendMessage();
	}
	
	@Override
	protected void sendMessage() {
		plugin.getLogger().warning("TS Query error (ID " + localValues.get("id") + "): " + TeamspeakUser.convert(localValues.get("msg")));
	}
}
