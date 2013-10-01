package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.dthielke.herochat.Herochat;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.util.MessageUtil;
import net.but2002.minecraft.BukkitSpeak.util.Replacer;

public abstract class TeamspeakEvent {
	
	private HashMap<String, String> user;
	
	public HashMap<String, String> getUser() {
		return user;
	}
	
	public String getClientName() {
		return user.get("client_nickname");
	}
	
	public Integer getClientId() {
		return Integer.valueOf(user.get("clid"));
	}
	
	public Integer getClientType() {
		return Integer.valueOf(user.get("client_type"));
	}
	
	protected void setUser(Integer clid) {
		user = BukkitSpeak.getClientList().get(clid);
	}
	
	protected void sendMessage(String messageName, String permission) {
		String m = BukkitSpeak.getStringManager().getMessage(messageName);
		if (m.isEmpty()) return;
		m = new Replacer().addClient(getUser()).replace(m);
		m = MessageUtil.toMinecraft(m, true, true);
		
		if (BukkitSpeak.useHerochat() && BukkitSpeak.getStringManager().getHerochatUsesEvents()) {
			// Send to Herochat channel
			String c = BukkitSpeak.getStringManager().getHerochatChannel();
			Herochat.getChannelManager().getChannel(c).announce(m);
		} else {
			// Directly send to players with permissions
			for (Player pl : BukkitSpeak.getInstance().getServer().getOnlinePlayers()) {
				if (!BukkitSpeak.getMuted(pl) && checkPermissions(pl, permission)) {
					pl.sendMessage(m);
				}
			}
		}
		
		// Finally log in console if enabled
		if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(m);
	}
	
	protected boolean checkPermissions(Player player, String perm) {
		return player.hasPermission("bukkitspeak.messages." + perm);
	}
	
	protected abstract void performAction();
}
