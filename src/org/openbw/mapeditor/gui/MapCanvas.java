package org.openbw.mapeditor.gui;

import java.awt.Point;

import org.openbw.mapeditor.data.Tile;
import org.openbw.mapeditor.data.Tileset;
import org.openbw.mapeditor.tiles.TransitionAlgorithm;
import org.openbw.mapeditor.tiles.TransitionAlgorithm.Algorithm;
import org.openbw.mapeditor.tiles.TransitionGenerator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class MapCanvas extends Canvas {

	public static final int TILE_SIZE = 64;
	
	private Tileset tileset;
	private int[] tiles;
	
	public MapCanvas(Tileset tileset, int[] tiles, int width, int height) {
		
		super(width * TILE_SIZE, height * TILE_SIZE);
		this.tileset = tileset;
		this.tiles = tiles;
		update();
	}
	
	public void update() {
		
		GraphicsContext gc = this.getGraphicsContext2D();
		int width = 128;
		int height = 128;
		
		for(int i = 0; i < tiles.length; i++) {

			int index = tiles[i] >> 4;
			int subIndex = tiles[i] & 0xf;
			Tile tile = tileset.indexToTile(index, subIndex);
			if (tile != null) {
				Image foreground = tileset.getTexture(tile.getForegroundTextureId());
				if (tile.getMaskId() == 0) {
					if (foreground != null) {
						gc.drawImage(foreground, 
							 ((i % width) * TILE_SIZE) % foreground.getWidth(), ((i / height) * TILE_SIZE) % foreground.getHeight(), TILE_SIZE, TILE_SIZE, 
							 (i % width) * TILE_SIZE, (i / height) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					} else {
						System.out.println("texture for id " + tile.getForegroundTextureId() + " was null.");
					}
				} else if (foreground != null) {
					Image background = tileset.getTexture(tile.getBackgroundTextureId());
					int[] coord = tileset.getMaskCoordinates(tile.getMaskId());
					Point section = new Point((i % width) * TILE_SIZE, (i / height) * TILE_SIZE);
					
					// TODO possibly read the algorithm from a map configuration object
					TransitionAlgorithm algorithm = TransitionAlgorithm.getAlgorithm(Algorithm.KOCH_CURVE);
					WritableImage blended = TransitionGenerator.createTransition(foreground, background, section, new Point(coord[1], coord[2]), new Point(coord[3], coord[4]), algorithm);
					gc.drawImage(blended, (i % width) * TILE_SIZE, (i / height) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
				} else {
					gc.fillText(index + "-" + subIndex, (i % width) * TILE_SIZE + 10, (i / height) * TILE_SIZE + 20);
				}
			} else {
				gc.fillText(index + "-" + subIndex, (i % width) * TILE_SIZE + 10, (i / height) * TILE_SIZE + 20);
			}
		}
	}
}
