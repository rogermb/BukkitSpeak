package net.but2002.minecraft.BukkitSpeak.TeamspeakCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerGroup {
	
	private boolean blocked;
	private boolean operator;
	private Map<String, Boolean> permissions;
	private List<String> pluginWhitelist;
	private List<String> commandBlacklist;
	
	public ServerGroup() {
		this(false);
	}
	
	public ServerGroup(boolean block) {
		blocked = block;
		operator = false;
		permissions = new HashMap<String, Boolean>();
		pluginWhitelist = new ArrayList<String>();
		commandBlacklist = new ArrayList<String>();
	}
	
	public ServerGroup(boolean op, List<String> pw, List<String> cb) {
		this(false, op, new HashMap<String, Boolean>(), pw, cb);
	}
	
	public ServerGroup(boolean op, Map<String, Boolean> perms, List<String> pw, List<String> cb) {
		this(false, op, perms, pw, cb);
	}
	
	public ServerGroup(boolean block, boolean op, Map<String, Boolean> perms, List<String> pw, List<String> cb) {
		blocked = block;
		operator = op;
		permissions = perms;
		pluginWhitelist = pw;
		commandBlacklist = cb;
	}
	
	public boolean isBlocked() {
		return blocked;
	}
	
	public boolean isOp() {
		return operator;
	}
	
	public void setOp(boolean op) {
		operator = op;
	}
	
	public Map<String, Boolean> getPermissions() {
		return permissions;
	}
	
	public List<String> getPluginWhitelist() {
		return pluginWhitelist;
	}
	
	public List<String> getCommandBlacklist() {
		return commandBlacklist;
	}
}
