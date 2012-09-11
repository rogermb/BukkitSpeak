package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;
import java.util.regex.Matcher;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public abstract class TeamspeakEvent {
	
	private HashMap<String, String> user;
	
	public TeamspeakEvent(Integer clid) {
		if (!BukkitSpeak.getClients().containsKey(clid)) BukkitSpeak.getClients().addClient(clid);
		setUser(clid);
	}
	
	public HashMap<String, String> getUser() {
		return user;
	}
	
	protected void setUser(Integer clid) {
		user = BukkitSpeak.getClients().get(clid);
	}
	
	protected Player[] getOnlinePlayers() {
		return Bukkit.getServer().getOnlinePlayers();
	}
	
	protected boolean isMuted(Player p) {
		return BukkitSpeak.getMuted(p);
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
		String output = input;
		output = Matcher.quoteReplacement(output);
		if (color) {
			output = output.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "\u00A7$3");
		} else {
			output = output.replaceAll("((&|$|\u00A7)([a-fk-orA-FK-OR0-9]))", "");
		}
		
		for (String key : user.keySet()) {
			if ((key != null) && (user.get(key) != null)) {
				output = output.replace("%" + key + "%", user.get(key));
			}
		}
		
		return output;
	}
	
	public boolean checkPermissions(Player player, String perm) {
		return player.hasPermission("bukkitspeak.messages." + perm);
	}
}
