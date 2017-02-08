package org.openbw.mapeditor.tiles;

import java.awt.Point;
import java.awt.image.BufferedImage;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class TransitionGenerator {

	public static WritableImage createTransition(Image from, Image to, Point transitionFrom, Point transitionTo, TransitionAlgorithm algorithm) {
		
		return createTransition(from, to, new Point(0, 0), transitionFrom, transitionTo, algorithm);
	}
	
	public static WritableImage createTransition(Image from, Image to, Point section, Point transitionFrom, Point transitionTo, TransitionAlgorithm algorithm) {
		
		BufferedImage autoMask = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		algorithm.generateTransitionMask(autoMask, transitionFrom, transitionTo);
		WritableImage transition = createBlendedImage(from, to, section, autoMask);
		
		return transition;
	}
	
	private static WritableImage createBlendedImage(Image from, Image to, Point section, BufferedImage alphaSource) {
		
		WritableImage blendedImage = new WritableImage((int)alphaSource.getWidth(), (int)alphaSource.getHeight());
		PixelReader fromReader = from.getPixelReader();
		PixelReader toReader = to.getPixelReader();
		
		PixelWriter writer = blendedImage.getPixelWriter();
		
		for (int x = 0; x < 64; x++) {
			for (int y = 0; y < 64; y++) {
				
				// read foreground data
				int foreGroundPixel = fromReader.getArgb((x + section.x) % (int)from.getWidth(), (y + section.y) % (int)from.getHeight());
			    int f_red = (foreGroundPixel >> 16) & 0xff;
			    int f_green = (foreGroundPixel >> 8) & 0xff;
			    int f_blue = (foreGroundPixel) & 0xff;
			    
			    // read background data
			    int backgroundPixel = toReader.getArgb((x + section.x) % (int)to.getWidth(), (y + section.y) % (int)to.getHeight());
				int b_red = (backgroundPixel >> 16) & 0xff;
			    int b_green = (backgroundPixel >> 8) & 0xff;
			    int b_blue = (backgroundPixel) & 0xff;
			    
			    // read blue channel as alpha mask (really any of the RGB channels could be read, since it is a gray scale mask)
				int alpha = alphaSource.getRGB(x, y) & 0xFF;
				
				int mergedPixel = ((f_red * alpha + b_red * (255 - alpha)) / 255) << 16;
				mergedPixel += ((f_green * alpha + b_green * (255 - alpha)) / 255) << 8;
				mergedPixel += ((f_blue * alpha + b_blue * (255 - alpha)) / 255);
				mergedPixel += 0xFF000000;
				
				writer.setArgb(x, y, mergedPixel);
			}
		}
		return blendedImage;
	}
}
