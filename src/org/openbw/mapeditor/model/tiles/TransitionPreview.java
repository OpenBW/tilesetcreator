package org.openbw.mapeditor.model.tiles;

import org.openbw.mapeditor.model.Notifier;
import org.openbw.mapeditor.tiles.TransitionAlgorithm;
import org.openbw.mapeditor.tiles.TransitionAlgorithm.Algorithm;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class TransitionPreview extends Notifier {

	private Image from;
	private Image to;
	private TransitionAlgorithm algorithm;
	private GeneratorConfiguration configuration;
	
	public TransitionPreview() {
		
		this.from = new WritableImage(64, 64);
		this.to = new WritableImage(64, 64);
		this.algorithm = TransitionAlgorithm.getAlgorithm(Algorithm.KOCH_CURVE);
		this.configuration = new GeneratorConfiguration(32, 10, 8);
	}
	
	public void init(Image from, Image to, TransitionAlgorithm algorithm) {
		
		System.out.println("initializing preview with images " + from + " and " + to);
		this.from = from;
		this.to = to;
		this.algorithm = algorithm;
	}

	public Image getFrom() {
		return from;
	}
	public void setFrom(Image from) {
		this.from = from;
		super.notifyListeners();
	}
	public Image getTo() {
		return to;
	}
	public void setTo(Image to) {
		this.to = to;
		super.notifyListeners();
	}
	public TransitionAlgorithm getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(TransitionAlgorithm algorithm) {
		this.algorithm = algorithm;
		super.notifyListeners();
	}
	public GeneratorConfiguration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(GeneratorConfiguration configuration) {
		this.configuration = configuration;
		super.notifyListeners();
	}
}
