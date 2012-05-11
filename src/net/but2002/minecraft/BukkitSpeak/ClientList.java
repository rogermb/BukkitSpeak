package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ClientList {
	
	ConcurrentHashMap<Integer, HashMap<String, String>> clients;
	JTS3ServerQuery query;
	Logger logger;
	
	public ClientList(BukkitSpeak plugin) {
		query = plugin.getQuery();
		logger = plugin.getLogger();
		clients = new ConcurrentHashMap<Integer, HashMap<String, String>>();
		
		asyncUpdateAll();
	}
	
	public void addClient(Integer clid) {
		if (clid <= 0) return;
		
		HashMap<String, String> user;
		try {
			user = query.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
			if (user != null && user.size() != 0) {
				if (user.get("client_type").equals("0")) {
					user.put("clid", clid.toString());
					clients.put(clid, user);
				}
			} else {
				logger.warning("Received no information for user id " + clid + ".");
			}
		} catch (Exception e) {
			logger.severe("Error while receiving client information.");
			e.printStackTrace();
		}
	}
	
	public void asyncUpdateAll() {
		
		ArrayList<Integer> clids = new ArrayList<Integer>();
		for (HashMap<String, String> c : query.getList(JTS3ServerQuery.LISTMODE_CLIENTLIST)) {
			clids.add(Integer.valueOf(c.get("clid")));
		}
		
		for (Integer clid : clids) {
			(new Thread(new ClientUpdater(clients, query, logger, clid))).start();
		}
	}
	
	public void asyncUpdateClient(Integer clid) {
		if (!clients.containsKey(clid)) return;
		(new Thread(new ClientUpdater(clients, query, logger, clid))).start();
	}
	
	public void clear() {
		clients.clear();
	}
	
	public boolean contains(HashMap<String, String> client) {
		return clients.contains(client);
	}
	
	public boolean containsKey(Integer clid) {
		return clients.containsKey(clid);
	}
	
	public boolean containsValue(HashMap<String, String> client) {
		return clients.containsValue(client);
	}
	
	public Enumeration<HashMap<String, String>> elements() {
		return clients.elements();
	}
	
	public Set<Entry<Integer, HashMap<String, String>>> entrySet() {
		return clients.entrySet();
	}
	
	public HashMap<String, String> get(Integer clid) {
		if (clients.containsKey(clid)) {
			return clients.get(clid);
		} else {
			return null;
		}
	}
	
	public HashMap<String, String> getByName(String name) {
		
		for (Integer key : clients.keySet()) {
			if (clients.get(key).get("client_nickname").equals(name)) return clients.get(key);
		}
		return null;
		
	}
	
	public HashMap<String, String> getByPartialName(String name) throws TwoOrMoreResultsException {
		
		ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		
		for (Integer key : clients.keySet()) {
			if (clients.get(key).get("client_nickname").toLowerCase().startsWith(name.toLowerCase())) {
				results.add(clients.get(key));
			}
		}
		
		if (results.size() == 0) {
			return null;
		} else if (results.size() == 1) {
			return results.get(0);
		} else {
			throw new TwoOrMoreResultsException();
		}
	}
	
	public boolean isEmpty() {
		return clients.isEmpty();
	}
	
	public Enumeration<Integer> keys() {
		return clients.keys();
	}
	
	public Set<Integer> keySet() {
		return clients.keySet();
	}
	
	public void removeClient(Integer clid) {
		if (clients.containsKey(clid)) clients.remove(clid);
	}
	
	public int size() {
		return clients.size();
	}
	
	public HashMap<String, String> updateClient(Integer clid) {
		
		if (!clients.containsKey(clid)) return null;
		
		HashMap<String, String> user;
		try {
			user = query.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
			if (user != null && user.size() != 0) {
				user.put("clid", clid.toString());
				clients.put(clid, user);
				return user;
			} else {
				logger.warning("Received no information for user id " + clid + ".");
				return null;
			}
		} catch (Exception e) {
			logger.severe("Error while receiving client information.");
			e.printStackTrace();
			return null;
		}
	}
	
	public Collection<HashMap<String, String>> values() {
		return clients.values();
	}
}

class ClientUpdater implements Runnable {
	
	ConcurrentHashMap<Integer, HashMap<String, String>> clients;
	JTS3ServerQuery query;
	Logger logger;
	Integer clid;
	
	public ClientUpdater(ConcurrentHashMap<Integer, HashMap<String, String>> clients, JTS3ServerQuery query, Logger logger, Integer clid) {
		this.clients = clients;
		this.query = query;
		this.logger = logger;
		this.clid = clid;
	}
	
	@Override
	public void run() {
		HashMap<String, String> user;
		try {
			user = query.getInfo(JTS3ServerQuery.INFOMODE_CLIENTINFO, clid);
			if (user != null && user.size() != 0) {
				user.put("clid", clid.toString());
				clients.put(clid, user);
			} else {
				logger.warning("Received no information for user id " + clid + ".");
			}
		} catch (Exception e) {
			logger.severe("Error while receiving client information.");
			e.printStackTrace();
		}
	}
}

class TwoOrMoreResultsException extends Exception {
	
	private static final long serialVersionUID = -1337430017338302854L;
	
}
