package org.openbw.mapeditor.data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openbw.mapeditor.model.Settings;
import org.openbw.mapeditor.model.tiles.Tileset;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import mpq.MPQException;

public class TilesetDAO {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private Settings settings;
	
	public TilesetDAO(Settings settings) {
		
		this.settings = settings;
	}
	
	private void loadImagesFromMPQ(Tileset tileset, String mpqPath) throws DataLayerException {
		
		MPQReader reader = new MPQReader(mpqPath);
		try {
			WritableImage[] textures = reader.readOriginalTextures();

			for (int i = 1; i < 14; i++) {
				LOGGER.debug("Setting texture " + tileset.indexToTile(i * 2, 0).getForegroundTextureId());
				tileset.setTexture(tileset.indexToTile(i * 2, 0).getForegroundTextureId(), textures[i - 1]);
			}
			tileset.setTexture(tileset.indexToTile(1, 0).getForegroundTextureId(), textures[13]);
		} catch (IOException | MPQException e) {
			throw new DataLayerException(e.getLocalizedMessage(), e);
		}
	}

	private void loadImagesFromSettings(Tileset tileset) {
		
		for (int i = 0; i < 14; i++) {
			
			// TODO handle invalid file paths
			File file = new File(settings.getTexturePath(i));
			try {
				tileset.setTexture(i, new Image(file.toURI().toURL().toString()));
			} catch (MalformedURLException e) {
				// ignore
				e.printStackTrace();
			}
		}
	}
	
	public void load(Tileset tileset) throws DataLayerException {
		
		if (settings.isTexturesFromMpq()) {
			loadImagesFromMPQ(tileset, settings.getMpqPath());
			LOGGER.debug("loaded texture images from MPQ");
		} else {
			loadImagesFromSettings(tileset);
			LOGGER.debug("loaded custom texture images");
		}
	}
	
	public void store(Tileset tileset) {
		
	}
}
