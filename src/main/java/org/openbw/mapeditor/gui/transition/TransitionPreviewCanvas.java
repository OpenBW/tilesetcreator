package org.openbw.mapeditor.gui.transition;

import java.awt.Point;

import org.openbw.mapeditor.gui.ChangeListener;
import org.openbw.mapeditor.model.tiles.GeneratorConfiguration;
import org.openbw.mapeditor.model.tiles.Tile;
import org.openbw.mapeditor.model.tiles.TransitionPreview;
import org.openbw.mapeditor.tiles.TransitionAlgorithm;
import org.openbw.mapeditor.tiles.TransitionGenerator;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class TransitionPreviewCanvas extends Canvas implements ChangeListener {

	private TransitionPreview transitionPreview;
	
	public TransitionPreviewCanvas(TransitionPreview transitionPreview) {
		super(9 * 64, 11 * 64);
		this.transitionPreview = transitionPreview;
		this.transitionPreview.addListener(this);
		this.transitionPreview.getConfiguration().addListener(this);
		update();
	}
	
	private WritableImage getSubImage(Image source, int x, int y, int width, int height) {
		
		PixelReader reader = source.getPixelReader();
		WritableImage subImage = new WritableImage(width, height);
		PixelWriter writer = subImage.getPixelWriter();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (writer == null || reader == null) {
					System.out.println(i + "," + j);
				}
				writer.setArgb(i, j, reader.getArgb(x + i, y + j));
			}
		}
		return subImage;
	}

	public void update() {
		
		GeneratorConfiguration configuration = transitionPreview.getConfiguration();
		TransitionAlgorithm algorithm = transitionPreview.getAlgorithm();
		Image img0 = transitionPreview.getFrom();
		Image img1 = transitionPreview.getTo();
		
		GraphicsContext gc = this.getGraphicsContext2D();
		
		gc.clearRect(0, 0, this.getWidth(), this.getHeight());
		gc.drawImage(img0, 128 % img0.getWidth(), 128 % img0.getHeight(), 64, 64, 256, 256, 64, 64);
		
		gc.drawImage(img1, 0, 128 % img1.getHeight(), 64, 64, 256 - 128, 256, 64, 64);
		gc.drawImage(img1, 256 % img1.getWidth(), 128 % img1.getHeight(), 64, 64, 256 + 128, 256, 64, 64);
		gc.drawImage(img1, 128 % img1.getWidth(), 0, 64, 64, 256, 256 - 128, 64, 64);
		gc.drawImage(img1, 128 % img1.getWidth(), 256 % img1.getHeight(), 64, 64, 256, 256 + 128, 64, 64);
		
		int[][] c = new int[8][4];
		c[0][0] = Tile.SIZE - configuration.getCoveredArea(); c[0][1] = 64; c[0][2] = Tile.SIZE - configuration.getCoveredArea(); c[0][3] = 0;
		c[1][0] = configuration.getCoveredArea(); c[1][1] = 0; c[1][2] = configuration.getCoveredArea(); c[1][3] = 64;
		c[2][0] = 0; c[2][1] = Tile.SIZE - configuration.getCoveredArea(); c[2][2] = 64; c[2][3] = Tile.SIZE - configuration.getCoveredArea();
		c[3][0] = 64; c[3][1] = configuration.getCoveredArea(); c[3][2] = 0; c[3][3] = configuration.getCoveredArea();
		c[4][0] = Tile.SIZE - configuration.getCoveredArea(); c[4][1] = 64; c[4][2] = 64; c[4][3] = Tile.SIZE - configuration.getCoveredArea();
		c[5][0] = configuration.getCoveredArea(); c[5][1] = 0; c[5][2] = 0; c[5][3] = configuration.getCoveredArea();
		c[6][0] = 0; c[6][1] = Tile.SIZE - configuration.getCoveredArea(); c[6][2] = configuration.getCoveredArea(); c[6][3] = 64;
		c[7][0] = 64; c[7][1] = configuration.getCoveredArea(); c[7][2] = Tile.SIZE - configuration.getCoveredArea(); c[7][3] = 0;
		
		WritableImage image1 = TransitionGenerator.createTransition(
				getSubImage(img0, (int)(64 % img0.getWidth()), (int)(128 % img0.getHeight()), 64, 64), 
				getSubImage(img1, (int)(64 % img1.getWidth()), (int)(128 % img1.getHeight()), 64, 64), new Point(c[0][0], c[0][1]), new Point(c[0][2], c[0][3]), algorithm);
		WritableImage image2 = TransitionGenerator.createTransition(
				getSubImage(img0, (int)(192 % img0.getWidth()), (int)(128 % img0.getHeight()), 64, 64), 
				getSubImage(img1, (int)(192 % img1.getWidth()), (int)(128 % img1.getHeight()), 64, 64), new Point(c[1][0], c[1][1]), new Point(c[1][2], c[1][3]), algorithm);
		WritableImage image3 = TransitionGenerator.createTransition(
				getSubImage(img0, (int)(128 % img0.getWidth()), (int)(64 % img0.getHeight()), 64, 64), 
				getSubImage(img1, (int)(128 % img1.getWidth()), (int)(64 % img1.getHeight()), 64, 64), new Point(c[2][0], c[2][1]), new Point(c[2][2], c[2][3]), algorithm);
		WritableImage image4 = TransitionGenerator.createTransition(
				getSubImage(img0, (int)(128 % img0.getWidth()), (int)(192 % img0.getHeight()), 64, 64), 
				getSubImage(img1, (int)(128 % img1.getWidth()), (int)(192 % img1.getHeight()), 64, 64), new Point(c[3][0], c[3][1]), new Point(c[3][2], c[3][3]), algorithm);
		
		gc.drawImage(image1, 256 - 64, 256, 64, 64);
		gc.drawImage(image2, 256 + 64, 256, 64, 64);
		gc.drawImage(image3, 256, 256 - 64, 64, 64);
		gc.drawImage(image4, 256, 256 + 64, 64, 64);
		
		WritableImage image5 = TransitionGenerator.createTransition(
				getSubImage(img0, (int)(64 % img0.getWidth()), (int)(64 % img0.getHeight()), 64, 64), 
				getSubImage(img1, (int)(64 % img1.getWidth()), (int)(64 % img1.getHeight()), 64, 64), new Point(c[4][0], c[4][1]), new Point(c[4][2], c[4][3]), algorithm);
		WritableImage image6 = TransitionGenerator.createTransition(
				getSubImage(img0, (int)(192 % img0.getWidth()), (int)(192 % img0.getHeight()), 64, 64), 
				getSubImage(img1, (int)(192 % img1.getWidth()), (int)(192 % img1.getHeight()), 64, 64), new Point(c[5][0], c[5][1]), new Point(c[5][2], c[5][3]), algorithm);
		WritableImage image7 = TransitionGenerator.createTransition(
				getSubImage(img0, (int)(192 % img0.getWidth()), (int)(64 % img0.getHeight()), 64, 64), 
				getSubImage(img1, (int)(192 % img1.getWidth()), (int)(64 % img1.getHeight()), 64, 64), new Point(c[6][0], c[6][1]), new Point(c[6][2], c[6][3]), algorithm);
		WritableImage image8 = TransitionGenerator.createTransition(
				getSubImage(img0, (int)(64 % img0.getWidth()), (int)(192 % img0.getHeight()), 64, 64), 
				getSubImage(img1, (int)(64 % img1.getWidth()), (int)(192 % img1.getHeight()), 64, 64), new Point(c[7][0], c[7][1]), new Point(c[7][2], c[7][3]), algorithm);
		
		gc.drawImage(image5, 256 - 64, 256 - 64, 64, 64);
		gc.drawImage(image6, 256 + 64, 256 + 64, 64, 64);
		gc.drawImage(image7, 256 + 64, 256 - 64, 64, 64);
		gc.drawImage(image8, 256 - 64, 256 + 64, 64, 64);
	}
	
}
