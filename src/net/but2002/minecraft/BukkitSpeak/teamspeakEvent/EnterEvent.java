/**
 * 
 */
package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

/**
 * @author greycap
 *
 */
public class EnterEvent extends TeamspeakEvent{
	
	public EnterEvent(BukkitSpeak plugin, String msg){
		super(plugin,msg);
		
		localKeys.add("cfid");
		localKeys.add("ctid");
		localKeys.add("reasonid");
		
		parseLocalValues(msg);
		setUser(new TeamspeakUser(removeLocalKeys(msg)));
		sendMessage();
	}
	
	protected void sendMessage(){
                if (!getUser().getName().startsWith("Unknown from"))
                {
                    plugin.getServer().broadcastMessage((replaceValues(plugin.getStringManager().getMessage("msg_join"))));
                }
		
	}

}
