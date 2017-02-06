package org.openbw.mapeditor.tiles;

import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class TransitionAlgorithm {

	public static enum Algorithm {MIDPOINT_DISPLACEMENT, KOCH_CURVE};
	
	public static TransitionAlgorithm getAlgorithm(Algorithm algorithm) {
	
		if (algorithm.equals(Algorithm.MIDPOINT_DISPLACEMENT)) {
			
			return new MidpointDisplacementAlgorithm();
		} else {
			
			return new KochCurveAlgorithm();
		}
	}
	
	public abstract void generateTransitionMask(BufferedImage autoMask, Point from, Point to);
}
