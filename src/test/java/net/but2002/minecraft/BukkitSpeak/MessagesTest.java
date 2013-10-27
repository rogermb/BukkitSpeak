package net.but2002.minecraft.BukkitSpeak;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.but2002.minecraft.BukkitSpeak.Configuration.Messages;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.*;

public class MessagesTest {
	
	private FileConfiguration defaults;
	
	public MessagesTest() {
		defaults = new YamlConfiguration();
		try {
			defaults.load(getResource("locale.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void checkAllPathsValid() {
		Pattern p = Pattern.compile("(?:[\\w\\-]+" + Pattern.quote(String.valueOf(defaults.options().pathSeparator()))
				+ ")*[\\w\\-]+");
		for (Messages m : Messages.values()) {
			if (!p.matcher(m.getConfigPath()).matches()) {
				Assert.fail(m.name() + "'s path did not comply to the path naming rules.");
			}
		}
	}
	
	@Test
	public void checkAllDefaultValues() {
		for (Messages m : Messages.values()) {
			if (m.getDefaultValue() == null) {
				fail(m.name() + "(" + m.getConfigPath() + ") did not have a default value assigned.");
			} else if (!m.getDefaultValue().equals(defaults.get(m.getConfigPath()))) {
				fail(m.name() + "(" + m.getConfigPath() + ") default values did not match.");
			} else if (m.getDefaultValue() instanceof String && String.valueOf(m.getDefaultValue()).isEmpty()) {
				fail(m.name() + "(" + m.getConfigPath() + ") had an empty String as default value assigned.");
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
		
		for (Messages m : Messages.values()) {
			if (!keys.remove(m.getConfigPath())) {
				fail(m.getConfigPath() + " did not have a value set in the default file.");
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
