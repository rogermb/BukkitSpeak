/**
 * 
 */
package net.but2002.minecraft.BukkitSpeak;

import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.TeamspeakEvent;

/**
 * @author greycap
 *
 */
public class TeamspeakUser {
	
	/*public static final String[] FIELDS = {"cfid","ctid","reasonid","clid","client_unique_identifier",
			"client_nickname","client_input_muted","client_output_muted","client_input_hardware",
			"client_output_hardware", "client_is_recording","client_database_id",
			"client_channel_group_id","client_servergroups","client_away","client_away_message",
			"client_type","client_meta_data","client_flag_avatar","client_talk_power",
			"client_talk_request","client_talk_request_msg","client_description",
			"client_is_talker","client_is_priority_speaker","client_unread_messages",
			"client_nickname_phonetic","client_needed_serverquery_view_power",
			"client_icon_id","client_is_channel_commander","client_country"
	};*/

	private HashMap<String,String> values = new HashMap<String, String>();
	


	private static final String[] NEEDCONVERT = {"client_nickname","client_talk_request_msg","client_description"};
		
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
		if(input != null)
			return input.replaceAll("\\\\s", " ");
		return null;
	}
	
	
	public String getValue(String key){
		return values.get(key);
	}
	
	public String setValue(String key, String value){
		return values.put(key, value);
	}
	
	public int getID(){
		return Integer.parseInt(getValue("clid"));
	}
	
	public String getName(){
		return getValue("client_nickname");
	}
	
	/**
	 * @return the values
	 */
	public HashMap<String, String> getValues() {
		return values;
	}
	
}
