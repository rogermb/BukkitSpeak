package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.TeamspeakEvent;

public class TeamspeakUser {
	
	private HashMap<String,String> values = new HashMap<String, String>();
	
	private static final String[] NEEDCONVERT = {"client_nickname","client_talk_request_msg","client_description","client_type"};
	
	public TeamspeakUser(String msg){
		values = TeamspeakEvent.split(msg);
		for(String currentConvert: NEEDCONVERT){
			if(values.containsKey(currentConvert)){
				String converted = convert(values.get(currentConvert));
				values.put(currentConvert, converted);
			}
		}
	}
	
	public String convert(String input){
		if(input != null) {
			String s = input;
			s = s.replaceAll("\\\\s", " ");
			s = s.replaceAll("\\\\/", "/");
			s = s.replaceAll("\\\\p", "|");
			return s;
		}
		return null;
	}
	
	
	public String getValue(String key){
		return values.get(key);
	}
	
	public String setValue(String key, String value){
		return values.put(key, value);
	}
	
	public int getID() {
		return Integer.parseInt(getValue("clid"));
	}
	
	public String getName() {
		return getValue("client_nickname");
	}
	
	public int getClientType() {
		return Integer.parseInt(getValue("client_type"));
	}
	
	public HashMap<String, String> getValues() {
		return values;
	}
	
}