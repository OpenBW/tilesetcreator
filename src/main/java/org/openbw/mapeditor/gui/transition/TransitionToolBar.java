package org.openbw.mapeditor.gui.transition;

import org.openbw.mapeditor.model.tiles.Tileset;
import org.openbw.mapeditor.model.tiles.TransitionPreview;
import org.openbw.mapeditor.tiles.TransitionAlgorithm;
import org.openbw.mapeditor.tiles.TransitionAlgorithm.Algorithm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class TransitionToolBar extends ToolBar {

	/**
	 * Describes the possible transitions from one texture to another.
	 * 0: no transition possible
	 * 1: standard transition using 1 tile
	 * 2: two dedicated tiles requires (typically transitions from normal to elevated)
	 * 3: three dedicated tiles required (typically transitions from high-ground to low-ground)
	 */
	private static int[][] TRANSITIONS = {
			{0,0,3,0,0,0,0,0,0,0,0,0,0,0}, // low-ground empty
			{0,0,1,0,1,1,2,3,0,0,0,0,0,1}, // low-ground primary
			{3,1,0,1,0,0,0,0,0,0,0,0,0,1}, // low-ground secondary
			{0,0,1,0,0,0,0,0,0,0,0,0,0,1}, // low-ground tertiary
			{0,1,0,0,0,0,0,0,0,0,0,0,0,0}, // low-ground primary non-buildable
			{0,1,0,0,0,0,0,0,0,0,0,0,0,0}, // low-ground secondary non-buildable
			{0,2,0,0,0,0,0,0,0,0,0,0,0,1}, // low-ground / primary elevated
			{0,3,0,0,0,0,0,0,0,0,0,0,0,0}, // low-ground / primary elevated high
			{0,0,0,0,0,0,0,0,0,1,1,2,3,1}, // high-ground / primary
			{0,0,0,0,0,0,0,0,1,0,0,0,0,1}, // high-ground / secondary
			{0,0,0,0,0,0,0,0,1,0,0,0,0,0}, // high-ground / primary non-buildable
			{0,0,0,0,0,0,0,0,2,0,0,0,0,1}, // high-ground / primary elevated
			{0,0,0,0,0,0,0,0,3,0,0,0,0,0}, // high-ground / primary elevated high
			{0,1,1,1,0,0,1,0,1,1,0,1,0,0}  // creep
	};
	
	private Tileset tileset;
	private TransitionPreview transitionPreview;
	
	public TransitionToolBar(Tileset tileset, TransitionPreview transitionPreview) {
		
		this.tileset = tileset;
		this.transitionPreview = transitionPreview;
		init();
	}
	
	private void init() {
		
		ObservableList<String> textureOptions = FXCollections.observableArrayList();
		textureOptions.addAll(Tileset.TEXTURE_NAMES);
		
		ObservableList<String> textureOptions2 = FXCollections.observableArrayList();
		for (int i = 0; i < TRANSITIONS[1].length; i++) {
			if (TRANSITIONS[1][i] > 0) {
				textureOptions2.add(Tileset.TEXTURE_NAMES[i]);
			}
		}
		
		ComboBox<String> combobox1 = new ComboBox<String>(textureOptions);
		ComboBox<String> combobox2 = new ComboBox<String>(textureOptions2);
		
		combobox1.setOnAction((event) -> {
		    
			int index = combobox1.getSelectionModel().getSelectedIndex();
			String selection = combobox2.getSelectionModel().getSelectedItem();
			
			ObservableList<String> validTextures = FXCollections.observableArrayList();
			for (int i = 0; i < TRANSITIONS[index].length; i++) {
				if (TRANSITIONS[index][i] > 0) {
					validTextures.add(Tileset.TEXTURE_NAMES[i]);
				}
			}
			combobox2.setItems(validTextures);
			if (validTextures.size() > 0) {
				if (validTextures.contains(selection)) {
					combobox2.getSelectionModel().select(selection);
				} else {
					combobox2.getSelectionModel().select(0);
				}
				combobox2.setDisable(false);
			} else {
				combobox2.setDisable(true);
			}
			transitionPreview.setFrom(tileset.getTexture(index));
		});
		
		combobox2.setOnAction((event) -> {
		    
			String item = combobox2.getSelectionModel().getSelectedItem();
			int index = -1;
			for (int i = 0; i < Tileset.TEXTURE_NAMES.length; i++) {
				if (Tileset.TEXTURE_NAMES[i].equals(item)) {
					index = i;
				}
			}
			if (index >= 0 && index < Tileset.NUMBER_OF_TEXTURES) {
				
				transitionPreview.setTo(tileset.getTexture(index));
			}
		});
		
		ObservableList<Algorithm> algorithmOptions = FXCollections.observableArrayList();
		algorithmOptions.addAll(Algorithm.values());
		ComboBox<Algorithm> combobox3 = new ComboBox<Algorithm>(algorithmOptions);
		combobox3.setOnAction((event) -> {
		    
			transitionPreview.setAlgorithm(TransitionAlgorithm.getAlgorithm(combobox3.getSelectionModel().getSelectedItem()));
		});

		combobox1.getSelectionModel().select(1);
		combobox2.getSelectionModel().select(2);
		combobox3.getSelectionModel().select(Algorithm.MIDPOINT_DISPLACEMENT);
		
		transitionPreview.init(tileset.getTexture(1), tileset.getTexture(2), TransitionAlgorithm.getAlgorithm(Algorithm.MIDPOINT_DISPLACEMENT));
		
		this.getItems().addAll(new Label("Texture transitions: from "),
				combobox1,
				new Label(" to "),
				combobox2,
				new Label(" Algorithm:"),
				combobox3);
	}
}
