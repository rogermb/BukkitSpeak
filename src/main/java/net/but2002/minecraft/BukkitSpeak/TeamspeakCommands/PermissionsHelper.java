package net.but2002.minecraft.BukkitSpeak.TeamspeakCommands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public final class PermissionsHelper implements Runnable {
	
	private File permissionsFile;
	private FileConfiguration permissionsConfig;
	private HashMap<String, ServerGroup> serverGroupMap;
	
	public PermissionsHelper() {
		serverGroupMap = new HashMap<String, ServerGroup>();
	}
	
	public void setUp() {
		// Load the config
		reload();
		
		// Start the permissions assignment task
		BukkitSpeak.getInstance().getServer().getScheduler().runTaskAsynchronously(BukkitSpeak.getInstance(), this);
	}
	
	public void run() {
		HashMap<String, ServerGroup> serverGroups = new HashMap<String, ServerGroup>();
		HashMap<String, HashMap<String, Boolean>> perms = new HashMap<String, HashMap<String, Boolean>>();
		HashMap<String, List<String>> inherits = new HashMap<String, List<String>>();
		Queue<String> resolved = new LinkedList<String>();
		Queue<String> unresolved = new LinkedList<String>();
		
		// Set up a raw list of permissions
		Vector<HashMap<String, String>> groups = BukkitSpeak.getQuery().getList(JTS3ServerQuery.LISTMODE_SERVERGROUPLIST);
		for (HashMap<String, String> group : groups) {
			String id = group.get("sgid");
			String type = group.get("type");
			if ((type == null) || !("1".equals(type))) continue;
			
			if (permissionsConfig.isConfigurationSection(id)) {
				ConfigurationSection section = permissionsConfig.getConfigurationSection(id);
				section.set("name", group.get("name"));
				if (!section.isBoolean("op")) {
					section.set("op", false);
				}
				if (!section.isConfigurationSection("permissions")) {
					section.createSection("permissions").set("somePermission", true);
				}
				if (!section.isList("plugin-whitelist")) {
					section.set("plugin-whitelist", (List<String>) Lists.newArrayList("PluginNameFromPluginsCommand"));
				}
				if (!section.isList("command-blacklist")) {
					section.set("command-blacklist", (List<String>) Lists.newArrayList("SomeBlockedCommand"));
				}
				Boolean op = section.getBoolean("op");
				ConfigurationSection cs = section.getConfigurationSection("permissions");
				List<String> pluginWhitelist = section.getStringList("plugin-whitelist");
				List<String> commandBlacklist = section.getStringList("command-blacklist");
				inherits.put(id, section.getStringList("inherits"));
				
				if ((op == null) || (cs == null) || (pluginWhitelist == null) || (commandBlacklist == null)
						|| (inherits == null)) {
					BukkitSpeak.log().severe("Error parsing TS3 server group " + id + ".");
					continue;
				}
				
				serverGroups.put(id, new ServerGroup(op.booleanValue(), pluginWhitelist, commandBlacklist));
				perms.put(id, parseConfigSection(cs));
			} else {
				ConfigurationSection section = permissionsConfig.createSection(id);
				section.set("name", group.get("name"));
				section.set("op", false);
				section.createSection("permissions").set("somePermission", true);
				section.set("plugin-whitelist", (List<String>) Lists.newArrayList("PluginNameFromPluginsCommand"));
				section.set("command-blacklist", (List<String>) Lists.newArrayList("SomeBlockedCommand"));
				section.set("inherits", (List<String>) new ArrayList<String>());
				
				serverGroups.put(id, new ServerGroup());
			}
		}
		
		// Save the config
		save();
		
		// Get the initial resolved groups
		for (String id : serverGroups.keySet()) {
			List<String> i = inherits.get(id);
			if ((i == null) || (i.size() == 0)) {
				resolved.add(id);
				inherits.remove(id);
			} else {
				unresolved.add(id);
			}
		}
		
		if (resolved.size() == 0) {
			BukkitSpeak.log().severe("Teamspeak permissions: Circular inheritance (No groups with no 'inherits').");
		}
		
		do {
			// Add the permissions of resolved groups thus resolving the permissions of other groups.
			
			while (resolved.size() > 0) {
				String id = resolved.poll();
				ServerGroup sg = serverGroups.get(id);
				sg.getPermissions().putAll(perms.get(id));
				
				for (String u : inherits.keySet()) {
					List<String> i = inherits.get(u);
					if ((i.size() > 0) && (i.contains(id))) {
						i.remove(id);
						serverGroups.get(u).getPermissions().putAll(sg.getPermissions());
						if (i.size() == 0) {
							resolved.add(u);
							unresolved.remove(u);
						}
					}
				}
			}
			
			// When everything went well, this queue is now empty.
			if (unresolved.size() == 0) {
				break;
			}
			
			// Otherwise, just choose a group with an unresolved dependency
			String id = unresolved.poll();
			ServerGroup sg = serverGroups.get(id);
			sg.getPermissions().putAll(perms.get(id));
			
			// Notify about unresolved dependencies.
			List<String> inheritances = inherits.get(id);
			StringBuilder sb = new StringBuilder();
			sb.append("Server group ").append(id).append(" had unresolved dependencies: ");
			for (String i : inheritances) {
				sb.append(i).append(", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append(".");
			BukkitSpeak.log().warning(sb.toString());
			
			inherits.put(id, new ArrayList<String>()); // Clear the stored dependencies for this one.
			
			for (String u : inherits.keySet()) {
				List<String> i = inherits.get(u);
				if ((i.size() > 0) && (i.contains(id))) {
					i.remove(id);
					serverGroups.get(u).getPermissions().putAll(sg.getPermissions());
					if (i.size() == 0) {
						resolved.add(u);
						unresolved.remove(u);
					}
				}
			}
		} while (unresolved.size() > 0);
		
		// Done
		serverGroupMap = serverGroups;
	}
	
	private HashMap<String, Boolean> parseConfigSection(ConfigurationSection cs) {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		for (Map.Entry<String, Object> entry : cs.getValues(true).entrySet()) {
			String[] path = entry.getKey().split("/");
			StringBuilder key = new StringBuilder(path[0]);
			for (int i = 1; i < path.length; i++) {
				key.append(".").append(path[i]);
			}
			if (entry.getValue() instanceof Boolean) {
				map.put(key.toString(), (Boolean) entry.getValue());
			} else if (!(entry.getValue() instanceof ConfigurationSection)) {
				BukkitSpeak.log().warning("Key " + key.toString() + " in the permissions for server group " 
						+ cs.getCurrentPath().split("/")[0] + " did not have a boolean value assigned.");
			}
		}
		return map;
	}
	
	public ServerGroup getServerGroup(String id) {
		return serverGroupMap.get(id);
	}
	
	public void reload() {
		if (permissionsFile == null) {
			permissionsFile = new File(BukkitSpeak.getInstance().getDataFolder(), "permissions.yml");
		}
		permissionsConfig = YamlConfiguration.loadConfiguration(permissionsFile);
		permissionsConfig.options().pathSeparator('/');
	}
	
	public void save() {
		if ((permissionsFile == null) || (permissionsConfig == null)) return;
		try {
			permissionsConfig.save(permissionsFile);
		} catch (IOException e) {
			BukkitSpeak.log().log(Level.SEVERE, "Could not save the locale file to " + permissionsFile, e);
		}
	}
}
