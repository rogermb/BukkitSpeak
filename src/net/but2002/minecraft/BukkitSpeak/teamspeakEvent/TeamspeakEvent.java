package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public abstract class TeamspeakEvent {
	
	protected HashMap<String, String> user;
	protected BukkitSpeak plugin;
	
	public TeamspeakEvent(BukkitSpeak plugin, Integer clid) {
		this.plugin = plugin;
		if (!plugin.getClients().containsKey(clid)) plugin.getClients().addClient(clid);
		setUser(clid);
	}
	
	public HashMap<String, String> getUser() {
		return user;
	}
	
	protected void setUser(Integer clid) {
		user = plugin.getClients().get(clid);
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
	
	protected abstract void sendMessage();
	
	public String replaceValues(String input, boolean color) {
		if (color) {
			input = input.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "§$3");
		} else {
			input = input.replaceAll("((&|$|§)([a-fk-orA-FK-OR0-9]))", "");
		}
		
		for (String key : user.keySet()) {
			if ((key != null) && (user.get(key) != null)) {
				input = input.replaceAll("%" + key + "%", user.get(key));
			}
		}
		
		return input;
	}
	
	public Boolean CheckPermissions(Player player, String perm) {
		return player.hasPermission("bukkitspeak.messages." + perm);
	}
}
