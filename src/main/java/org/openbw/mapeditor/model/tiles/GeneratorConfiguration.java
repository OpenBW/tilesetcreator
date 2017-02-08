package org.openbw.mapeditor.model.tiles;

import org.openbw.mapeditor.model.Notifier;

public class GeneratorConfiguration extends Notifier {

	private int coveredArea;
	private int volatility;
	private int gradient;
	
	public GeneratorConfiguration(int coveredArea, int volatility, int gradient) {
	
		this.coveredArea = coveredArea;
		this.volatility = volatility;
		this.gradient = gradient;
	}
	
	public int getCoveredArea() {
		return coveredArea;
	}
	public void setCoveredArea(int coveredArea) {
		this.coveredArea = coveredArea;
		super.notifyListeners();
	}
	public int getVolatility() {
		return volatility;
	}
	public void setVolatility(int volatility) {
		this.volatility = volatility;
		super.notifyListeners();
	}
	public int getGradient() {
		return gradient;
	}
	public void setGradient(int gradient) {
		this.gradient = gradient;
		super.notifyListeners();
	}
}
