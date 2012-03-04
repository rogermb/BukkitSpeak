package net.but2002.minecraft.BukkitSpeak.util;

import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class ConfigReader {
	
	BukkitSpeak plugin;
	FileConfiguration config;
	
	public ConfigReader(BukkitSpeak BukkitSpeak) {
		plugin = BukkitSpeak;
		config = BukkitSpeak.getConfig();
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
		
		try {
			return config.getConfigurationSection(dir).getBoolean(loc);
		} catch (Exception e) {
			config.getConfigurationSection(dir).set(loc, def);
			logConfigError(dir + "." + loc);
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
	
	public void EnsureSectionCreated(String dir) {
		if (!config.isConfigurationSection(dir)) {
			config.createSection(dir);
		}
	}
	
	private void logConfigError(String message){
		plugin.getLogger().severe(plugin + "Error while parsing " + message);
	}
	
}
