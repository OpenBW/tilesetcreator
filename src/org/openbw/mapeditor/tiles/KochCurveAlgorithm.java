package org.openbw.mapeditor.tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class KochCurveAlgorithm extends TransitionAlgorithm {

	private void kochCurve(int[][] frontier, BufferedImage autoMask, Point from, Point to, int volatility, int level) {
		
		double random_sector1 = 1.0 / 3 + Math.random() * 1.0 / 15 - 1.0 / 30 - (3.0 - level) / 30; // add 20% variation to one-third length
		double random_sector2 = 2.0 / 3 + Math.random() * 1.0 / 15 - 1.0 / 30 + (3.0 - level) / 30; // add 20% variation to one-third length
				
		int p1_x = (int)Math.round(from.x + (to.x - from.x) * random_sector1);
		int p1_y = (int)Math.round(from.y + (to.y - from.y) * random_sector1);
		Point p1 = new Point(p1_x, p1_y);
		
		int p3_x = (int)Math.round(from.x + (to.x - from.x) * random_sector2);
		int p3_y = (int)Math.round(from.y + (to.y - from.y) * random_sector2);
		Point p3 = new Point(p3_x, p3_y);
		
		int p2_x = p3_x - p1_x;
		int p2_y = p3_y - p1_y;
		double rx = (p2_x * Math.cos(-1.0472)) - (p2_y * Math.sin(-1.0472));
	    double ry = (p2_x * Math.sin(-1.0472)) + (p2_y * Math.cos(-1.0472));
	    
	    // add random noise to the location of the third triangle point according to volatility parameter
	    p2_x = (int)(rx + p1_x + Math.random() * volatility * 2 - volatility);
	    p2_y = (int)(ry + p1_y + Math.random() * volatility * 2 - volatility);
	    Point p2 = new Point(p2_x, p2_y);
	    
	    Polygon p = new Polygon(new int[] {p1_x, p2_x, p3_x}, new int[] {p1_y, p2_y, p3_y}, 3);
	    Polygon ps = new Polygon(new int[] {p1_x + 2, p2_x + 2, p3_x + 2}, new int[] {p1_y, p2_y, p3_y}, 3);
	    Graphics g = autoMask.createGraphics();
	    
	    // smoothen (set grey tone) depending on level
	    g.setColor(new Color(0, 0, (255 - (3-level) * 50) / 2)); 
	    g.fillPolygon(ps);
	    g.setColor(new Color(0, 0, 255 - (3-level) * 50)); 
	    g.fillPolygon(p);
	    
		if (level > 0) {
			
			kochCurve(frontier, autoMask, from, p1, volatility, level - 1);
			kochCurve(frontier, autoMask, p1, p2, volatility, level - 1);
			kochCurve(frontier, autoMask, p2, p3, volatility, level - 1);
			kochCurve(frontier, autoMask, p3, to, volatility, level - 1);
		}
	}
	
	@Override
	public void generateTransitionMask(BufferedImage autoMask, Point from, Point to) {
		
		int[][] frontier = new int[64][64];
		
		kochCurve(frontier, autoMask, from, to, 5, 3);
		
		Graphics g = autoMask.createGraphics();
		g.setColor(new Color(0, 0, 255)); 
	    
		Polygon p;
		// bottom -> right -> bottom-right or top -> left -> top-left
		if (from.y == 64 && to.x == 64 || to.x == 0 && from.y == 0) {
			p = new Polygon(new int[]{from.x, to.x, to.x}, new int[]{from.y, to.y, from.y}, 3);
			
		// bottom -> top -> top-right -> bottom-right or top -> bottom -> bottom-left -> top-left
		} else if (from.y == 64 && to.y == 0 || to.y == 64 && from.y == 0) {
			p = new Polygon(new int[]{from.x, to.x, from.y, from.y}, new int[]{from.y, to.y, to.y, from.y}, 4);
			
		// left -> right -> bottom-right -> bottom-left or right -> left -> top-left -> top-right
		} else if (from.x == 0 && to.x == 64 || to.x == 0 && from.x == 64) {
			p = new Polygon(new int[]{from.x, to.x, to.x, from.x}, new int[]{from.y, to.y, to.x, to.x}, 4);
			
		// left -> top -> top-right -> bottom-right -> bottom-left or right -> bottom -> bottom-left -> top-left -> top-right
		} else if (from.x == 0 && to.y == 0 || to.y == 64 && from.x == 64) {
			p = new Polygon(new int[]{from.x, to.x, 64 - from.x, 64 - from.x, from.x}, new int[]{from.y, to.y, to.y, 64 - to.y, 64 - to.y}, 5);
			
		// left -> bottom -> left-bottom or right -> top -> top-right
		} else if(from.x == 0 && to.y == 64 || to.y == 0 && from.x == 64) {
			p = new Polygon(new int[]{from.x, to.x, from.x}, new int[]{from.y, to.y, to.y}, 3);
			
		// bottom -> left -> top-left -> top-right -> bottom-right or top -> right -> bottom-right -> bottom-left -> top-left
		} else {
			p = new Polygon(new int[]{from.x, to.x, to.x, 64 - to.x, 64 - to.x}, new int[]{from.y, to.y, 64 - from.y, 64 - from.y, from.y}, 5);
		}
		g.fillPolygon(p);
		
		for (int y = 0; y < 64; y++) {
			for (int x = 0; x < 64; x++) {
				
				autoMask.setRGB(x, y, autoMask.getRGB(x, y) >> 8 | autoMask.getRGB(x, y));
			}
		}
	}
	
}
