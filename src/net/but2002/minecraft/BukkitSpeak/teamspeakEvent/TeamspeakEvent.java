package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

public abstract class TeamspeakEvent {
	
	protected TeamspeakUser user;
	protected HashMap<String,String> localValues = new HashMap<String, String>();
	protected BukkitSpeak plugin;
	protected ArrayList<String> localKeys = new ArrayList<String>();
	
	public TeamspeakEvent(BukkitSpeak plugin, String msg) {
		this.plugin = plugin;
	}
	
	public TeamspeakUser getUser() {
		return user;
	}
	public void setUser(TeamspeakUser user) {
		this.user = user;
	}
	
	protected abstract void sendMessage();
	
	public static HashMap<String,String> split(String msg) {
		HashMap<String, String> result = new HashMap<String,String>();
		String[] split = msg.split(" ");
		for(String current: split) {
			if(current != null) {
				String[] key_value = current.split("=", 2);
				if(key_value.length == 1) {
					result.put(current, null);
				} else if(key_value.length == 2) {
					result.put(key_value[0], key_value[1]);
				}
			}
		}
		return result;
	}
	
	protected void parseLocalValues(String msg) {
		HashMap<String, String> split = split(msg);
		for(String key : localKeys){
			localValues.put(key, split.get(key));
		}
	}
	
	protected String removeLocalKeys(String input) {
		String result = new String(input);
		for(String key : localKeys)
			result = result.replaceFirst(key + "=\\S* ", "");
		return result;
	}
	
	public String replaceValues(String input, boolean color) {
		if (color) {
			input = input.replaceAll("(&([a-fk-orA-FK-OR0-9]))", "§$2").replaceAll("($([a-fk-orA-FK-OR0-9]))", "§$2");
		} else {
			input = input.replaceAll("(&([a-fk-orA-FK-OR0-9]))", "").replaceAll("($([a-fk-orA-FK-OR0-9]))", "");
		}
		
		String[] keys = (String[]) this.user.getValues().keySet().toArray(new String[this.user.getValues().size()]);
		for (String key : keys) {
			if ((key != null) && (this.user.getValue(key) != null)) {
				input = input.replaceAll("%" + key + "%", this.user.getValue(key));
			}
		}
		
		keys = (String[]) this.localValues.keySet().toArray(new String[this.localValues.size()]);
		for (String key : keys) {
			if ((key != null) && (this.localValues.get(key) != null)) {
				input = input.replaceAll("%" + key + "%", (String) this.localValues.get(key));
			}
		}
		
		return input;
	}
	
	public Boolean CheckPermissions(Player player, String perm) {
		return player.hasPermission("bukkitspeak.messages." + perm);
	}
}
