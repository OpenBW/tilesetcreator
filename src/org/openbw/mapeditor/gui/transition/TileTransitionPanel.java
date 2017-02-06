package org.openbw.mapeditor.gui.transition;

import org.openbw.mapeditor.data.Tileset;
import org.openbw.mapeditor.model.tiles.TransitionPreview;

import javafx.scene.layout.BorderPane;

public class TileTransitionPanel extends BorderPane {

	public TileTransitionPanel(Tileset tileset, TransitionPreview transitionPreview) {
		
		TransitionToolBar toolBar = new TransitionToolBar(tileset, transitionPreview);
		ParameterPanel parameterPanel = new ParameterPanel(transitionPreview.getConfiguration());
		TransitionPreviewCanvas transitionPreviewCanvas = new TransitionPreviewCanvas(transitionPreview);
		this.setTop(toolBar);
		this.setCenter(transitionPreviewCanvas);
		this.setRight(parameterPanel);
	}
}
