package net.but2002.minecraft.BukkitSpeak.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ConfigReader {
	
	JavaPlugin plugin;
	FileConfiguration config;
	boolean err;
	
	public ConfigReader(JavaPlugin JavaPlugin) {
		plugin = JavaPlugin;
		config = JavaPlugin.getConfig();
		err = false;
	}
	
	public ConfigReader(JavaPlugin JavaPlugin, FileConfiguration FileConfiguration) {
		plugin = JavaPlugin;
		config = FileConfiguration;
		err = false;
	}
	
	public ConfigReader(JavaPlugin JavaPlugin, String filePath) {
		plugin = JavaPlugin;
		err = false;
		try {
			File configFile = new File(plugin.getDataFolder(), filePath);
			if (!configFile.exists()) configFile.createNewFile();
			config = YamlConfiguration.loadConfiguration(configFile);
		} catch (Exception e) {
			plugin.getLogger().severe("Could not load the given file.");
			err = true;
		}
	}
	
	public boolean contains(String path) {
		return config.contains(path);
	}
	
	public boolean isEmpty() {
		return (config.getKeys(false).isEmpty());
	}
	
	public HashMap<String, ?> getAll(HashMap<String, ?> map) {
		Object def;
		String[] loc;
		for (String key : map.keySet()) {
			try {
				def = map.get(key);
				loc = key.split(".");
				if (def.getClass() == Boolean.class) {
					getBoolean(loc[0], loc[1], (Boolean) def);
				} else if (def.getClass() == Integer.class) {
					getInteger(loc[0], loc[1], (Integer) def);
				} else if (def.getClass() == String.class) {
					getString(loc[0], loc[1], (String) def);
				} else {
					logConfigError(key);
				}
			} catch (Exception e) {
				logConfigError(key);
			}
			
		}
		def = null;
		loc = null;
		return map;
	}
	
	public Boolean getBoolean(String loc, Boolean def) {
		if (config.isBoolean(loc)) {
			return config.getBoolean(loc);
		} else {
			config.set(loc, def);
			logConfigError(loc);
			return def;
		}
	}
	public Boolean getBoolean(String dir, String loc, Boolean def) {
		ensureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isBoolean(loc)) {
			return config.getConfigurationSection(dir).getBoolean(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public Boolean getBoolean(String[] dir, String loc, Boolean def) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (cs.isBoolean(loc)) {
			return cs.getBoolean(loc);
		} else {
			cs.set(loc, def);
			logConfigError(getAbsolutePath(dir) + "." + loc);
			return def;
		}
	}
	
	public Integer getInteger(String loc, Integer def) {
		if (config.isInt(loc)) {
			return config.getInt(loc);
		} else {
			config.set(loc, def);
			logConfigError(loc);
			return def;
		}
	}
	public Integer getInteger(String dir, String loc, Integer def) {
		ensureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isInt(loc)) {
			return config.getConfigurationSection(dir).getInt(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public Integer getInteger(String[] dir, String loc, Integer def) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (cs.isInt(loc)) {
			return cs.getInt(loc);
		} else {
			cs.set(loc, def);
			logConfigError(getAbsolutePath(dir) + "." + loc);
			return def;
		}
	}
	
	public String getString(String loc, String def) {
		if (config.isString(loc)) {
			return config.getString(loc);
		} else {
			config.set(loc, def);
			logConfigError(loc);
			return def;
		}
	}
	public String getString(String dir, String loc, String def) {
		ensureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isString(loc)) {
			return config.getConfigurationSection(dir).getString(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public String getString(String[] dir, String loc, String def) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (cs.isString(loc)) {
			return cs.getString(loc);
		} else {
			cs.set(loc, def);
			logConfigError(getAbsolutePath(dir) + "." + loc);
			return def;
		}
	}
	
	public Integer getChoice(String loc, String def, HashMap<String, Integer> poss) {
		if (config.isString(loc) && poss.containsKey(config.getString(loc))) {
			return poss.get(config.getString(loc));
		} else {
			config.set(loc, def);
			logConfigError(loc);
			return poss.get(def);
		}
	}
	public Integer getChoice(String dir, String loc, String def, HashMap<String, Integer> poss) {
		ensureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isString(loc) && poss.containsKey(config.getConfigurationSection(dir).getString(loc))) {
			return poss.get(config.getConfigurationSection(dir).getString(loc));
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return poss.get(def);
		}
	}
	public Integer getChoice(String[] dir, String loc, String def, HashMap<String, Integer> poss) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (cs.isString(loc) && poss.containsKey(cs.getString(loc))) {
			return poss.get(cs.getString(loc));
		} else {
			cs.set(loc, def);
			logConfigError(getAbsolutePath(dir) + "." + loc);
			return poss.get(def);
		}
	}
	
	public Integer getChoice(String loc, Integer def, String[][] poss) {
		if (config.isString(loc)) {
			String v = config.getString(loc);
			Integer res = -1;
			for (Integer i = 0; i < poss.length; i++) {
				String[] p = poss[i];
				if (ArrContains(p, v)) {
					res = i;
					break;
				}
			}
			if (res >= 0) {
				return res;
			} else {
				config.set(loc, poss[def][0]);
				logConfigError(loc);
				return def;
			}
		} else {
			config.set(loc, poss[def][0]);
			logConfigError(loc);
			return def;
		}
	}
	public Integer getChoice(String dir, String loc, Integer def, String[][] poss) {
		ensureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isString(loc)) {
			String v = config.getConfigurationSection(dir).getString(loc);
			Integer res = -1;
			for (Integer i = 0; i < poss.length; i++) {
				String[] p = poss[i];
				if (ArrContains(p, v)) {
					res = i;
					break;
				}
			}
			if (res >= 0) {
				return res;
			} else {
				config.getConfigurationSection(dir).set(loc, poss[def][0]);
				logConfigError(dir + "." + loc);
				return def;
			}
		} else {
			config.getConfigurationSection(dir).set(loc, poss[def][0]);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public Integer getChoice(String[] dir, String loc, Integer def, String[][] poss) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (cs.isString(loc)) {
			String v = cs.getString(loc);
			Integer res = -1;
			for (Integer i = 0; i < poss.length; i++) {
				String[] p = poss[i];
				if (ArrContains(p, v)) {
					res = i;
					break;
				}
			}
			if (res >= 0) {
				return res;
			} else {
				cs.set(loc, poss[def][0]);
				logConfigError(getAbsolutePath(dir) + "." + loc);
				return def;
			}
		} else {
			cs.set(loc, poss[def][0]);
			logConfigError(getAbsolutePath(dir) + "." + loc);
			return def;
		}
	}
	
	public List<?> getList(String loc, List<?> def) {
		if (config.isList(loc)) {
			return config.getList(loc);
		} else {
			config.set(loc, def);
			logConfigError(loc);
			return def;
		}
	}
	public List<?> getList(String dir, String loc, List<?> def) {
		ensureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isList(loc)) {
			return config.getConfigurationSection(dir).getList(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public List<?> getList(String[] dir, String loc, List<?> def) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (cs.isList(loc)) {
			return cs.getList(loc);
		} else {
			cs.set(loc, def);
			logConfigError(getAbsolutePath(dir) + "." + loc);
			return def;
		}
	}
	
	public List<String> getStringList(String loc, List<String> def) {
		if (config.isList(loc)) {
			return config.getStringList(loc);
		} else {
			config.set(loc, def);
			logConfigError(loc);
			return def;
		}
	}
	public List<String> getStringList(String dir, String loc, List<String> def) {
		ensureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isList(loc)) {
			return config.getConfigurationSection(dir).getStringList(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public List<String> getStringList(String[] dir, String loc, List<String> def) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (cs.isList(loc)) {
			return cs.getStringList(loc);
		} else {
			cs.set(loc, def);
			logConfigError(getAbsolutePath(dir) + "." + loc);
			return def;
		}
	}
	
	public Vector getVector(String loc, Vector def) {
		if (config.isVector(loc)) {
			return config.getVector(loc);
		} else {
			config.set(loc, def);
			logConfigError(loc);
			return def;
		}
	}
	public Vector getVector(String dir, String loc, Vector def) {
		ensureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isVector(loc)) {
			return config.getConfigurationSection(dir).getVector(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public Vector getVector(String[] dir, String loc, Vector def) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (cs.isVector(loc)) {
			return cs.getVector(loc);
		} else {
			cs.set(loc, def);
			logConfigError(getAbsolutePath(dir) + "." + loc);
			return def;
		}
	}
	
	public ConfigurationSection getConfigSection(String loc, Map<?,?> def) {
		if (!config.isConfigurationSection(loc)) {
			if (def == null) {
				config.createSection(loc);
			} else {
				config.createSection(loc, def);
			}
			logConfigError(loc);
		}
		
		return config.getConfigurationSection(loc);
	}
	public ConfigurationSection getConfigSection(String dir, String loc, Map<?,?> def) {
		ensureSectionCreated(dir);
		
		if (!config.getConfigurationSection(dir).isConfigurationSection(loc)) {
			if (def == null) {
				config.getConfigurationSection(dir).createSection(loc);
			} else {
				config.getConfigurationSection(dir).createSection(loc, def);
			}
			logConfigError(dir + "." + loc);
		}
		
		return config.getConfigurationSection(dir).getConfigurationSection(loc);
	}
	public ConfigurationSection getConfigSection(String[] dir, String loc, Map<?,?> def) {
		ensureSectionCreated(dir);
		
		ConfigurationSection cs = config;
		for (String d : dir) {
			cs = cs.getConfigurationSection(d);
		}
		
		if (!cs.isConfigurationSection(loc)) {
			if (def == null) {
				cs.createSection(loc);
			} else {
				cs.createSection(loc, def);
			}
			logConfigError(getAbsolutePath(dir) + "." + loc);
		}
		
		return cs.getConfigurationSection(loc);
	}
	
	public Set<String> getKeys(boolean b) {
		return config.getKeys(b);
	}
	
	public Map<String, Object> getValues(boolean b) {
		return config.getValues(b);
	}
	
	public boolean ensureSectionCreated(String dir) {
		if (!config.isConfigurationSection(dir)) {
			config.createSection(dir);
			return true;
		}
		return false;
	}
	public boolean ensureSectionCreated(String[] dir) {
		boolean ret = false;
		ConfigurationSection cs = config;
		for (String d : dir) {
			if (!cs.isConfigurationSection(d)) {
				cs.createSection(d);
				ret = true;
			}
			cs = cs.getConfigurationSection(d);
		}
		return ret;
	}
	
	private String getAbsolutePath(String[] dir) {
		StringBuilder sb = new StringBuilder();;
		for (String d : dir) {
			sb.append(d);
			sb.append(config.options().pathSeparator());
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	private boolean ArrContains(String[] arr, String con) {
		
		boolean ret = false;
		for (String a : arr) {
			if (a.equalsIgnoreCase(con)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	private void logConfigError(String message){
		plugin.getLogger().severe("Error while parsing " + message);
		err = true;
	}
	
	public boolean gotErrors() {
		return err;
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
}
