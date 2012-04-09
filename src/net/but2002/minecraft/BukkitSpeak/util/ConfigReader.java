package net.but2002.minecraft.BukkitSpeak.util;

import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigReader {
	
	JavaPlugin plugin;
	FileConfiguration config;
	Boolean err;
	
	public ConfigReader(JavaPlugin JavaPlugin) {
		plugin = JavaPlugin;
		config = JavaPlugin.getConfig();
		err = false;
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
	
	public Boolean getBoolean(String dir, String loc, Boolean def) {
		EnsureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isBoolean(loc)) {
			return config.getConfigurationSection(dir).getBoolean(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public Boolean getBoolean(String[] dir, String loc, Boolean def) {
		EnsureSectionCreated(dir);
		
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
	
	public Integer getInteger(String dir, String loc, Integer def) {
		EnsureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isInt(loc)) {
			return config.getConfigurationSection(dir).getInt(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public Integer getInteger(String[] dir, String loc, Integer def) {
		EnsureSectionCreated(dir);
		
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
	
	public String getString(String dir, String loc, String def) {
		EnsureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isString(loc)) {
			return config.getConfigurationSection(dir).getString(loc);
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return def;
		}
	}
	public String getString(String[] dir, String loc, String def) {
		EnsureSectionCreated(dir);
		
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
	
	public Integer getChoice(String dir, String loc, String def, HashMap<String, Integer> poss) {
		EnsureSectionCreated(dir);
		
		if (config.getConfigurationSection(dir).isString(loc) && poss.containsKey(config.getConfigurationSection(dir).getString(loc))) {
			return poss.get(config.getConfigurationSection(dir).getString(loc));
		} else {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
			return poss.get(def);
		}
	}
	public Integer getChoice(String[] dir, String loc, String def, HashMap<String, Integer> poss) {
		EnsureSectionCreated(dir);
		
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
	
	public Integer getChoice(String dir, String loc, Integer def, String[][] poss) {
		EnsureSectionCreated(dir);
		
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
		EnsureSectionCreated(dir);
		
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
	
	public void EnsureSectionCreated(String dir) {
		if (!config.isConfigurationSection(dir)) {
			config.createSection(dir);
		}
	}
	public void EnsureSectionCreated(String[] dir) {
		ConfigurationSection cs = config;
		for (String d : dir) {
			if (!cs.isConfigurationSection(d)) {
				cs.createSection(d);
			}
			cs = cs.getConfigurationSection(d);
		}
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
	
	public Boolean gotErrors() {
		return err;
	}
}
