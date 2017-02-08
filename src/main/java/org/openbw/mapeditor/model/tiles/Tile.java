package org.openbw.mapeditor.model.tiles;

public class Tile {

	public static final int SIZE = 64;
	
	private int foregroundTextureId;
	private int backgroundTextureId;
	private int maskId;
	
	public Tile(int foregroundTextureId, int backgroundTextureId, int maskId) {
		
		this.setForegroundTextureId(foregroundTextureId);
		this.setBackgroundTextureId(backgroundTextureId);
		this.setMaskId(maskId);
	}

	public Tile() {
		
	}
	
	public int getForegroundTextureId() {
		return foregroundTextureId;
	}

	public void setForegroundTextureId(int foregroundTextureId) {
		this.foregroundTextureId = foregroundTextureId;
	}

	public int getBackgroundTextureId() {
		return backgroundTextureId;
	}

	public void setBackgroundTextureId(int backgroundTextureId) {
		this.backgroundTextureId = backgroundTextureId;
	}

	public int getMaskId() {
		return maskId;
	}

	public void setMaskId(int maskId) {
		this.maskId = maskId;
	}
	
	
}
