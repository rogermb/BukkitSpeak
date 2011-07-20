/**
 * 
 */
package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

/**
 * @author greycap
 *
 */
public class LeaveEvent extends TeamspeakEvent{

	/**
	 * @param plugin
	 * @param msg
	 */
	public LeaveEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		localKeys.add("reasonmsg");
		localKeys.add("clid");
		parseLocalValues(msg);
		
		setUser(plugin.getTs().getUserByID(Integer.parseInt(localValues.get("clid"))));
		
		sendMessage();
		
	}

	/* (non-Javadoc)
	 * @see net.befog.minecraft.BukkitSpeak.teamspeakEvent.TeamspeakEvent#sendMessage()
	 */
	@Override
	protected void sendMessage() {
		if(user != null && !getUser().getName().startsWith("Unknown from") && getUser().getClientType() != 1)
                {
                    plugin.getServer().broadcastMessage(replaceValues(plugin.getStringManager().getMessage("msg_quit"), true));
                }
		
	}
	

}
