package org.openbw.mapeditor.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SettingsDAO {

	private static final String CONFIG_FILE = "config.properties";
	
	private Properties properties;
	private Properties defaultProperties;
	
	public SettingsDAO() {
		
		this.defaultProperties = new Properties();
		this.defaultProperties.setProperty("scdir", "");
		this.defaultProperties.setProperty("textures.source", "custom");
		String path = System.getProperty("user.dir") + File.separator;
		for (int i = 0; i < 14; i++) {
			
			this.defaultProperties.setProperty("textures." + i + ".path", path + "dummyTexture.png");
		}
		
		this.properties = new Properties(defaultProperties);
	}
	
	private Properties readPropertyFile() throws DataLayerException {
		
		try {
			InputStream input = new FileInputStream(CONFIG_FILE);
			properties.load(input);
			input.close();
		} catch (IOException e) {
			throw new DataLayerException(e.getLocalizedMessage(), e);
		}
		
		return this.properties;
	}
	
	public void load() throws DataLayerException {
		
		readPropertyFile();
	}
	
	public void store() throws DataLayerException {
		
		try {
			FileOutputStream output = new FileOutputStream(CONFIG_FILE);
			properties.store(output, null);
			output.flush();
			output.close();
		} catch (IOException e) {
			throw new DataLayerException(e.getLocalizedMessage(), e);
		}
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public void setProperty(String key, String value) {
		
		this.properties.setProperty(key, value);
	}

	public boolean exists() {
		return new File(CONFIG_FILE).exists();
	}
}
