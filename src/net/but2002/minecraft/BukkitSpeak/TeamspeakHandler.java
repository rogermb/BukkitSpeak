package net.but2002.minecraft.BukkitSpeak;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.EnterEvent;
import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.LeaveEvent;
import net.but2002.minecraft.BukkitSpeak.teamspeakEvent.ServerMessageEvent;

public class TeamspeakHandler implements Runnable{
	
	BukkitSpeak plugin;
	StringManager stringManager;
	boolean kill = false;
	
	private HashMap<Integer, TeamspeakUser> users = new HashMap<Integer, TeamspeakUser>();
	
	private TeamspeakKeepAlive keepAliveThread;
	
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	
	public TeamspeakHandler(BukkitSpeak plugin){
		this.plugin = plugin;
		stringManager = plugin.getStringManager();
		connect();
	}

	@Override
	public void run() {
		try {
			
			while(!kill) {
				if(socket.isClosed()) {
					connect();
				}
				String line = in.readLine();
				if(line != null) {
					handleMessage(line);
				}
				Thread.sleep(1000);
			}
			
			try {
				out.println("logout");
			} catch(Exception e) {
				plugin.getLogger().warning(plugin + "Could not logout properly. Shouldn't be a problem though.");
			}
			in.close();
			out.close();
			socket.close();
			
		} catch (Exception e) {
			plugin.getLogger().severe(plugin + "Exception while listening to the Teamspeak Query.");
			e.printStackTrace();
		}
	}
	
	public void connect(){
		try {
			socket = new Socket(InetAddress.getByName(stringManager.getIp()),stringManager.getQueryPort());
			out = new PrintWriter(socket.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
			out.println("login " + stringManager.getServerAdmin() + " " + stringManager.getServerPass());
			out.println("use port=" + stringManager.getServerPort());
			out.println("servernotifyregister event=server");
			out.println("servernotifyregister event=textserver");

			socket.setKeepAlive(true);
			if(keepAliveThread != null) keepAliveThread.kill();
			keepAliveThread = new TeamspeakKeepAlive(out);
			keepAliveThread.start();
			
		} catch (Exception e) {
			plugin.getLogger().log(java.util.logging.Level.SEVERE,e.toString());
			e.printStackTrace();
		}
		
	}
	
	public void handleMessage (String message){
		String command = message.split(" ")[0];
		message = message.replaceFirst("\\S* ", "");
		if(command.equals("notifycliententerview")){
			TeamspeakUser user = new EnterEvent(plugin, message).getUser();
			users.put(user.getID(), user);
		}
		if(command.equals("notifyclientleftview")){
			new LeaveEvent(plugin, message);
		}
		if(command.equals("notifytextmessage")){
			new ServerMessageEvent(plugin, message);
		}
	}
	
	public void kill(){
		this.kill = true;
	}
	
	public TeamspeakUser getUserByID(int id){
		return users.get(id);
	}
	
	public TeamspeakUser getUserByName(String name){
		TeamspeakUser[] users = this.users.values().toArray(new TeamspeakUser[this.users.values().size()]);
		for(TeamspeakUser currentUser : users){
			if(name.equals(currentUser.getName())) return currentUser;
		}
		return null;
	}

}
