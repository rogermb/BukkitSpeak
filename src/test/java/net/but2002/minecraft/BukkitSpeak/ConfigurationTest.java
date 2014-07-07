package net.but2002.minecraft.BukkitSpeak;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.but2002.minecraft.BukkitSpeak.Configuration.Configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.*;

public class ConfigurationTest {

	private FileConfiguration defaults;

	public ConfigurationTest() {
		defaults = new YamlConfiguration();
		try {
			InputStreamReader in = new InputStreamReader(getResource("config.yml"));
			defaults.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void checkAllPathsValid() {
		Pattern p = Pattern.compile("(?:[\\w\\-]+" + Pattern.quote(String.valueOf(defaults.options().pathSeparator()))
				+ ")*[\\w\\-]+");
		for (Configuration c : Configuration.values()) {
			if (!p.matcher(c.getConfigPath()).matches()) {
				fail(c.name() + "'s path did not comply to the path naming rules.");
			}
		}
	}

	@Test
	public void checkAllDefaultValues() {
		for (Configuration c : Configuration.values()) {
			if (c.getDefaultValue() == null) {
				fail(c.name() + "(" + c.getConfigPath() + ") did not have a default value assigned.");
			} else if (!c.getDefaultValue().equals(defaults.get(c.getConfigPath()))) {
				fail(c.name() + "(" + c.getConfigPath() + ") default values did not match.");
			}
		}
	}

	@Test
	public void checkAllValuesExist() {
		List<String> keys = new ArrayList<String>();
		for (String s : defaults.getKeys(true)) {
			if (!defaults.isConfigurationSection(s)) {
				keys.add(s);
			}
		}

		for (Configuration c : Configuration.values()) {
			if (!keys.remove(c.getConfigPath())) {
				fail(c.getConfigPath() + " did not have a value set in the default file.");
			}
		}

		for (String key : keys) {
			fail(key + " was set in the default file, but not in the config.");
		}
	}

	private InputStream getResource(String filename) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (filename == null) {
			throw new IllegalArgumentException("Filename cannot be null");
		}

		try {
			URL url = loader.getResource(filename);
			if (url == null) {
				return null;
			}

			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			return connection.getInputStream();
		} catch (IOException ex) {
			return null;
		}
	}
}
