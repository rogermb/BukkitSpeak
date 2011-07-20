/**
 * 
 */
package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

/**
 * @author greycap
 *
 */
public class ServerMessageEvent extends TeamspeakEvent{

	/**
	 * @param plugin
	 * @param msg
	 */
	public ServerMessageEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		localKeys.add("msg");
		localKeys.add("invokerid");
		localKeys.add("targetmode");
		localKeys.add("invokername");
		parseLocalValues(msg);
		
		setUser(plugin.getTs().getUserByID(Integer.parseInt(localValues.get("invokerid"))));
		
		String msgValue = localValues.get("msg");
		if(msgValue != null && user != null)
			localValues.put("msg", user.convert(msgValue));
		
		String invokerNameValue = localValues.get("invokername");
		if(invokerNameValue != null && user != null)
			localValues.put("invokername", user.convert(invokerNameValue));
	
		sendMessage();
		
	}

	/* (non-Javadoc)
	 * @see net.befog.minecraft.BukkitSpeak.teamspeakEvent.TeamspeakEvent#sendMessage()
	 */
	@Override
	protected void sendMessage() {
		if(user != null)
			plugin.getServer().broadcastMessage(replaceValues(plugin.getStringManager().getMessage("msg_servermsg"), false));
		
	}

}
