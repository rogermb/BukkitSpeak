package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

public class ClientlistEvent extends TeamspeakEvent {
	
	public ClientlistEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("cid");
		parseLocalValues(msg);
		
		setUser(new TeamspeakUser(removeLocalKeys(msg)));
		getUser().setValue("ctid", localValues.get("cid"));
	}
	
	@Override
	protected void sendMessage() {}
}
