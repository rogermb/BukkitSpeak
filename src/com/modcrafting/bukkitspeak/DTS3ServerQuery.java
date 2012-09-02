package com.modcrafting.bukkitspeak;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class DTS3ServerQuery{
	public final static Logger log = Logger.getLogger("Minecraft");
	/* Adding to the commands from JTS3ServerQuery
	 * There weren't enough for me.
	 */
	
	/* 
	 * @param ip just Integer.toString(arg);
	 * @param name = Client Nickname
	 * @param banReason = Can be anything.
	 */
	public boolean banClient(String name, String banReason){
		if (!BukkitSpeak.getQuery().isConnected()){
			log.log(Level.INFO, "banClient(): Not connected to TS3 server!");
			return false;
		}
		String clid = clientFindID(name);
		HashMap<String, String> hmIn;
		try{
			String command = "banclient";
			if (clid != null && clid.length() > 0){
			command += " clid=" + clid;
			}
			if (banReason != null && banReason.length() > 0){
			command += " banreason=" + BukkitSpeak.getQuery().encodeTS3String(banReason);
			}
			
			hmIn = BukkitSpeak.getQuery().doCommand(command);
			
			if (!hmIn.get("id").equals("0")){
				log.log(Level.INFO, "banClient()" + hmIn.get("id") + hmIn.get("msg") + hmIn.get("extra_msg") + hmIn.get("failed_permid"));
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean banClient(Integer clid, String banReason){
		if (!BukkitSpeak.getQuery().isConnected()){
			log.log(Level.INFO, "banClient(): Not connected to TS3 server!");
			return false;
		}
		HashMap<String, String> hmIn;
		try{
			String command = "banclient";
			if (clid != null && clid > 0){
			command += " clid=" + String.valueOf(clid);
			}
			if (banReason != null && banReason.length() > 0){
			command += " banreason=" + BukkitSpeak.getQuery().encodeTS3String(banReason);
			}
			
			hmIn = BukkitSpeak.getQuery().doCommand(command);
			
			if (!hmIn.get("id").equals("0")){
				log.log(Level.INFO, "banClient()" + hmIn.get("id") + hmIn.get("msg") + hmIn.get("extra_msg") + hmIn.get("failed_permid"));
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean changeGroup(String groupid, String nameid){
		if (!BukkitSpeak.getQuery().isConnected()){
			log.log(Level.INFO, "changeGroup(): Not connected to TS3 server!");
			return false;
		}
		
		HashMap<String, String> hmIn;
		try{
			String command = "servergroupaddclient";
			if (groupid != null && groupid.length() > 0){
			command += " sgid=" + groupid;
			}
			if (nameid != null && nameid.length() > 0){
			command += " cldbid=" + nameid;
			}
			
			hmIn = BukkitSpeak.getQuery().doCommand(command);
			if (!hmIn.get("id").equals("0")){
				log.log(Level.INFO, "changeGroup()" + hmIn.get("id") + hmIn.get("msg") + hmIn.get("extra_msg") + hmIn.get("failed_permid"));
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public String clientFindID(String clientname){
		if (!BukkitSpeak.getQuery().isConnected()){
			log.log(Level.INFO, "clientfind(): Not connected to TS3 server!");
			return null;
		}
		
		HashMap<String, String> hmIn;
		try{
			String command = "clientfind";					
			if (clientname != null && clientname.length() > 0){
			command += " pattern=" + clientname;
			}
			
			hmIn = BukkitSpeak.getQuery().doCommand(command);
			HashMap<String, String> info = parseLine(hmIn.get("response"));
			if (!hmIn.get("msg").equalsIgnoreCase("ok")){
				log.log(Level.INFO, "clientfind()" + hmIn.get("id") + hmIn.get("msg") + hmIn.get("extra_msg") + hmIn.get("failed_permid"));
				return null;
			}
			clientname = info.remove("clid");
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return clientname;
		
	}
	public String clientFind(String clientname){
		if (!BukkitSpeak.getQuery().isConnected()){
			log.log(Level.INFO, "clientfind(): Not connected to TS3 server!");
			return null;
		}
		
		HashMap<String, String> hmIn;
		try{
			String command = "clientfind";					
			if (clientname != null && clientname.length() > 0){
			command += " pattern=" + clientname;
			}
			
			hmIn = BukkitSpeak.getQuery().doCommand(command);
			HashMap<String, String> info = parseLine(hmIn.get("response"));
			if (!hmIn.get("msg").equalsIgnoreCase("ok")){
				log.log(Level.INFO, "clientfind()" + hmIn.get("id") + hmIn.get("msg") + hmIn.get("extra_msg") + hmIn.get("failed_permid"));
				return null;
			}
			clientname = info.remove("client_nickname");
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return clientname;
		
	}
	public String channelFind(String channelname){
		if (!BukkitSpeak.getQuery().isConnected()){
			log.log(Level.INFO, "channelFind(): Not connected to TS3 server!");
			return null;
		}
		
		HashMap<String, String> hmIn;
		try{
			String command = "channelfind";
			if (channelname != null && channelname.length() > 0){
			command += " pattern=" + channelname;
			}
			
			hmIn = BukkitSpeak.getQuery().doCommand(command);
			HashMap<String, String> info = parseLine(hmIn.get("response"));
			if (!hmIn.get("msg").equalsIgnoreCase("ok")){
				log.log(Level.INFO, "clientfind()" + hmIn.get("id") + hmIn.get("msg") + hmIn.get("extra_msg") + hmIn.get("failed_permid"));
				return null;
			}
			channelname = info.remove("cid");
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return channelname;
		
	}
	public boolean poke(String player, String message){
		if (!BukkitSpeak.getQuery().isConnected()){
			log.log(Level.INFO, "poke(): Not connected to TS3 server!");
			return false;
		}
		String clid = clientFindID(player);
		HashMap<String, String> hmIn;
		try{
			String command = "clientpoke";					
			if (player != null && message != null){
			command += " clid=" + clid + " msg=" + BukkitSpeak.getQuery().encodeTS3String(message);
			}
			
			hmIn = BukkitSpeak.getQuery().doCommand(command);
			if (!hmIn.get("msg").equalsIgnoreCase("ok")){
				log.log(Level.INFO, "clientfind()" + hmIn.get("id") + hmIn.get("msg") + hmIn.get("extra_msg") + hmIn.get("failed_permid"));
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void outputHashMap(HashMap<String, String> hm){
		if (hm == null)
		{
			return;
		}
		
		Collection<String> cValue = hm.values();
		Collection<String> cKey = hm.keySet();
		Iterator<String> itrValue = cValue.iterator();
		Iterator<String> itrKey = cKey.iterator();
		
		while (itrValue.hasNext() && itrKey.hasNext())
		{
			System.out.println(itrKey.next() + ": " + itrValue.next());
				
		}
	}
	private HashMap<String, String> parseLine(String line)
	{
		if (line == null || line.length() == 0)
		{
			return null;
		}
		
		StringTokenizer st = new StringTokenizer(line, " ", false);
		HashMap<String, String> retValue = new HashMap<String, String>();
		String key;
		String temp;
		int pos = -1;
		
		while (st.hasMoreTokens())
		{
			temp = st.nextToken();
			
			// The next 10 lines split the key / value pair at the equal sign and put this into the hash map.
			pos = temp.indexOf("=");
			
			if (pos == -1)
			{
				retValue.put(temp, "");
			}
			else
			{
				key = temp.substring(0, pos);
				retValue.put(key, BukkitSpeak.getQuery().decodeTS3String(temp.substring(pos+1)));
			}
		}
		
		return retValue;
	}
	public void echoError(){
		String error = BukkitSpeak.getQuery().getLastError();
		if (error != null)
		{
			System.out.println(error);
			if (BukkitSpeak.getQuery().getLastErrorPermissionID() != -1)
			{
				HashMap<String, String> permInfo = BukkitSpeak.getQuery().getPermissionInfo(BukkitSpeak.getQuery().getLastErrorPermissionID());
				if (permInfo != null)
				{
					System.out.println("Missing Permission: " + permInfo.get("permname"));
				}
			}
		}
	}
	
}
