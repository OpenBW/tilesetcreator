package org.openbw.mapeditor.tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class MidpointDisplacementAlgorithm extends TransitionAlgorithm {

	private void midpointDisplacement(Point[] frontier, int sectionStart, int sectionEnd, Point from, Point to, double volatility, int level) {
		
		Point middle = new Point((to.x + from.x) / 2, (to.y + from.y) / 2);
		
		int x = to.y - middle.y;
		int y = from.x - middle.x;
		middle.x += (int)(Math.random() * 2 * volatility * x - x * volatility);
		middle.y += (int)(Math.random() * 2 * volatility * y - y * volatility);
		
		frontier[(sectionEnd + sectionStart) / 2] = middle;
		
		if (level > 0) {
			midpointDisplacement(frontier, sectionStart, (sectionEnd + sectionStart) / 2, from, middle, volatility * 1.2, level - 1);
			midpointDisplacement(frontier, (sectionEnd + sectionStart) / 2, sectionEnd, middle, to, volatility * 1.2, level - 1);
		}
	}

	@Override
	public void generateTransitionMask(BufferedImage autoMask, Point from, Point to) {
		
		int level = 3;
		Point[] frontier = new Point[(int)Math.pow(2, level + 1) + 1];
		
		frontier[0] = from;
		frontier[frontier.length - 1] = to;
		midpointDisplacement(frontier, 0, frontier.length - 1, from, to, 0.75, level);
		
		int p1_x = to.y > from.y ? 0 : 64;
		int p1_y = to.x > from.x ? 64 : 0;
		
		int p4_x = to.y < 64 && from.y < 64 && to.y > 0 && from.y > 0 ? 0 : p1_x;
		int p4_y = to.x < 64 && from.x < 64 ? 64 : p1_y;
		
		int[] xs = new int[frontier.length + 2];
		int[] ys = new int[frontier.length + 2];
		
		for (int i = 0; i < frontier.length; i++) {
			xs[i] = frontier[i].x;
			ys[i] = frontier[i].y;
		}
		if (to.x > from.x || from.y > to.y) {
			xs[xs.length - 2] = p1_x;
			ys[ys.length - 2] = p1_y;
			xs[xs.length - 1] = p4_x;
			ys[ys.length - 1] = p4_y;
		} else {
			xs[xs.length - 1] = p1_x;
			ys[ys.length - 1] = p1_y;
			xs[xs.length - 2] = p4_x;
			ys[ys.length - 2] = p4_y;
		}
		Polygon p = new Polygon(xs, ys, xs.length);
		
		Graphics g = autoMask.createGraphics();
		g.setColor(new Color(0, 0, 255)); 
	    g.fillPolygon(p);
	    
	    for (int i = 0; i < 4; i++) {
			for (int x = 1; x < 63; x++) {
				for (int y = 1; y < 63; y++) {
					
					int alpha = (int)((autoMask.getRGB(x, y) & 0xFF) / 1.5);
					if (autoMask.getRGB(x + 1, y) == 0)
					autoMask.setRGB(x + 1, y, alpha);
					if (autoMask.getRGB(x, y + 1) == 0)
					autoMask.setRGB(x, y + 1, alpha);
					if (autoMask.getRGB(x - 1, y) == 0)
					autoMask.setRGB(x - 1, y, alpha);
					if (autoMask.getRGB(x, y - 1) == 0)
					autoMask.setRGB(x, y - 1, alpha);
				}
			}
	    }
	}

}
