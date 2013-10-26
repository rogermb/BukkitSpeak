package net.but2002.minecraft.BukkitSpeak;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public class ChannelList {
	
	private ConcurrentHashMap<Integer, HashMap<String, String>> channels;
	private Logger logger;
	
	public ChannelList() {
		logger = BukkitSpeak.getInstance().getLogger();
		channels = new ConcurrentHashMap<Integer, HashMap<String, String>>();
	}
	
	public void asyncUpdateAll() {
		(new Thread(new ChannelUpdater(this))).start();
	}
	
	public void asyncUpdateChannel(int cid) {
		if (!channels.containsKey(cid)) return;
		(new Thread(new ChannelUpdater(this, cid))).start();
	}
	
	public void clear() {
		channels.clear();
	}
	
	public boolean containsID(int cid) {
		return channels.containsKey(cid);
	}
	
	public HashMap<String, String> get(int cid) {
		return channels.get(cid);
	}
	
	public HashMap<String, String> getByName(String name) {
		for (HashMap<String, String> channel : channels.values()) {
			if (channel.get("channel_name").equals(name)) return channel;
		}
		return null;
	}
	
	public HashMap<String, String> getByPartialName(String name) {
		
		HashMap<String, String> ret = null;
		
		for (HashMap<String, String> channel : channels.values()) {
			String n = channel.get("channel_name").toLowerCase().replaceAll(" ", "");
			if (n.startsWith(name.toLowerCase())) {
				if (ret == null) {
					ret = channel;
				} else {
					throw new IllegalArgumentException("There is more than one client matching " + name);
				}
			}
		}
		
		return ret;
	}
	
	public List<String> getChannelNames() {
		List<String> ret = new ArrayList<String>();
		for (HashMap<String, String> channel : channels.values()) {
			ret.add(channel.get("channel_name"));
		}
		return ret;
	}
	
	public ConcurrentHashMap<Integer, HashMap<String, String>> getChannels() {
		return channels;
	}
	
	public boolean isEmpty() {
		return channels.isEmpty();
	}
	
	public void removeChannel(int cid) {
		if (channels.containsKey(cid)) channels.remove(cid);
	}
	
	public int size() {
		return channels.size();
	}
	
	public void updateChannel(int cid) {
		(new ChannelUpdater(this, cid)).run();
	}
	
	public void updateAll() {
		(new ChannelUpdater(this)).run();
	}
	
	private void setChannelData(HashMap<String, String> channel, int cid) {
		if (channel != null && channel.size() != 0) {
			channel.put("cid", String.valueOf(cid));
			channels.put(cid, channel);
		} else {
			logger.warning("Received no information for channel id " + cid + ".");
		}
	}
	
	private class ChannelUpdater implements Runnable {
		
		private ChannelList cl;
		private int cid;
		private boolean updateAll;
		
		public ChannelUpdater(ChannelList channelList, int channelID) {
			cl = channelList;
			cid = channelID;
			updateAll = false;
		}
		
		public ChannelUpdater(ChannelList channelList) {
			cl = channelList;
			updateAll = true;
		}
		
		@Override
		public void run() {
			if (!BukkitSpeak.getQuery().isConnected()) return;
			
			if (updateAll) {
				Vector<HashMap<String, String>> channelList = BukkitSpeak.getQuery().getList(JTS3ServerQuery.LISTMODE_CHANNELLIST);
				if (channelList == null) {
					BukkitSpeak.log().severe("Error while receiving channel information.");
					return;
				}
				for (HashMap<String, String> channel : channelList) {
					if (channel == null) {
						BukkitSpeak.log().severe("Error while receiving channel information.");
						return;
					}
					cl.setChannelData(channel, Integer.valueOf(channel.get("cid")));
				}
			} else {
				HashMap<String, String> channel = BukkitSpeak.getQuery().getInfo(JTS3ServerQuery.INFOMODE_CHANNELINFO, cid);
				if (channel == null) {
					BukkitSpeak.log().severe("Error while receiving channel information.");
					return;
				}
				cl.setChannelData(channel, cid);
			}
		}
	}
}
