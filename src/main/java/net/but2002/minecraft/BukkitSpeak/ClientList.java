package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ClientList {
	
	private ConcurrentHashMap<Integer, HashMap<String, String>> clients;
	private JTS3ServerQuery query;
	private Logger logger;
	
	public ClientList() {
		query = BukkitSpeak.getQuery();
		logger = BukkitSpeak.getInstance().getLogger();
		clients = new ConcurrentHashMap<Integer, HashMap<String, String>>();
		
		asyncUpdateAll();
	}
	
	public boolean addClient(int clid) {
		if (clid <= 0) return false;
		if (!query.isConnected()) return false;
		
		HashMap<String, String> user;
		try {
			user = query.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
			if (user != null && user.size() != 0) {
				if (user.get("client_type").equals("0") && !clients.containsKey(clid)) {
					user.put("clid", String.valueOf(clid));
					clients.put(clid, user);
					return true;
				}
				return false;
			} else {
				logger.warning("Received no information for user id " + clid + ". (Adding)");
				return false;
			}
		} catch (Exception e) {
			logger.severe("Error while receiving client information.");
			e.printStackTrace();
			return false;
		}
	}
	
	public void asyncUpdateAll() {
		(new Thread(new ClientUpdater(this))).start();
	}
	
	public void asyncUpdateClient(int clid) {
		if (!clients.containsKey(clid)) return;
		(new Thread(new ClientUpdater(this, clid))).start();
	}
	
	public void clear() {
		clients.clear();
	}
	
	public boolean containsID(int clid) {
		return clients.containsKey(clid);
	}
	
	public HashMap<String, String> get(int clid) {
		return clients.get(clid);
	}
	
	public HashMap<String, String> getByName(String name) {
		for (HashMap<String, String> client : clients.values()) {
			if (client.get("client_nickname").equals(name)) return client;
		}
		return null;
	}
	
	public HashMap<String, String> getByPartialName(String name) {
		
		HashMap<String, String> ret = null;
		
		for (HashMap<String, String> client : clients.values()) {
			String n = client.get("client_nickname").toLowerCase();
			if (n.startsWith(name.toLowerCase())) {
				if (ret == null) {
					ret = client;
				} else {
					throw new IllegalArgumentException("There is more than one client matching " + name);
				}
			}
		}
		
		return ret;
	}
	
	public List<String> getClientNames() {
		List<String> ret = new ArrayList<String>();
		for (HashMap<String, String> client : clients.values()) {
			ret.add(client.get("client_nickname"));
		}
		return ret;
	}
	
	public ConcurrentHashMap<Integer, HashMap<String, String>> getClients() {
		return clients;
	}
	
	public boolean isEmpty() {
		return clients.isEmpty();
	}
	
	public void removeClient(int clid) {
		if (clients.containsKey(clid)) clients.remove(clid);
	}
	
	public int size() {
		return clients.size();
	}
	
	public HashMap<String, String> updateClient(int clid) {
		
		if (!clients.containsKey(clid) || !query.isConnected()) return null;
		
		HashMap<String, String> user;
		try {
			user = query.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
			if (user != null && user.size() != 0) {
				if (user.get("client_type").equals("0")) {
					user.put("clid", String.valueOf(clid));
					clients.put(clid, user);
					return user;
				}
				return null;
			} else {
				logger.warning("Received no information for user id " + clid + ". (ClientUpdate)");
				return null;
			}
		} catch (Exception e) {
			logger.severe("Error while receiving client information.");
			e.printStackTrace();
			return null;
		}
	}
	
	void setClientData(HashMap<String, String> user, int clid) {
		
		if (user != null && user.size() != 0) {
			if (user.get("client_type").equals("0")) {
				user.put("clid", String.valueOf(clid));
				clients.put(clid, user);
			}
		} else {
			logger.warning("Received no information for user id " + clid + ". (AsyncClientUpdate)");
		}
	}
}

class ClientUpdater implements Runnable {
	
	private ClientList cl;
	private int clid;
	private boolean updateAll;
	
	public ClientUpdater(ClientList clientList, int clientID) {
		cl = clientList;
		clid = clientID;
		updateAll = false;
	}
	
	public ClientUpdater(ClientList clientList) {
		cl = clientList;
		updateAll = true;
	}
	
	@Override
	public void run() {
		if (!BukkitSpeak.getQuery().isConnected()) return;
		
		if (updateAll) {
			Vector<HashMap<String, String>> users;
			try {
				users = BukkitSpeak.getQuery().getList(JTS3ServerQuery.LISTMODE_CLIENTLIST, "-info -country");
				for (HashMap<String, String> user : users) {
					cl.setClientData(user, Integer.valueOf(user.get("clid")));
				}
			} catch (Exception e) {
				BukkitSpeak.log().severe("Error while receiving client information.");
				e.printStackTrace();
			}
		} else {
			HashMap<String, String> user;
			try {
				user = BukkitSpeak.getQuery().getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
				cl.setClientData(user, clid);
			} catch (Exception e) {
				BukkitSpeak.log().severe("Error while receiving client information.");
				e.printStackTrace();
			}
		}
	}
}
