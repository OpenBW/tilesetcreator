package org.openbw.mapeditor.model;

import java.io.File;

import org.openbw.mapeditor.data.DataLayerException;
import org.openbw.mapeditor.data.SettingsDAO;

public class Settings {

	private SettingsDAO settingsDAO;
	
	public Settings() {
		
		this.settingsDAO = new SettingsDAO();
	}
	
	public boolean exists() {
		return settingsDAO.exists();
	}
	
	public void load() throws DataLayerException {
		settingsDAO.load();
	}
	
	public void store() throws DataLayerException {
		settingsDAO.store();
	}
	
	public String getScdir() {
		
		return this.settingsDAO.getProperty("scdir");
	}
	
	public void setScdir(String directory) {
		
		this.settingsDAO.setProperty("scdir", directory);
	}
	
	public String getMpqPath() {
		
		return getScdir() + File.separator + "StarDat.mpq";
	}

	public boolean isTexturesFromMpq() {
		
		return "mpq".equals(this.settingsDAO.getProperty("textures.source"));
	}

	public String getTexturePath(int i) {
		return this.settingsDAO.getProperty("textures." + i + ".path");
	}

	public void setTexturesFromMpq(boolean mpq) {
		this.settingsDAO.setProperty("textures.source", mpq ? "mpq" : "custom");
	}
}
