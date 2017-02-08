package org.openbw.mapeditor.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openbw.mapeditor.data.DataLayerException;
import org.openbw.mapeditor.data.MapReader;
import org.openbw.mapeditor.gui.transition.TileTransitionPanel;
import org.openbw.mapeditor.model.Settings;
import org.openbw.mapeditor.model.tiles.Tileset;
import org.openbw.mapeditor.model.tiles.TransitionPreview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mpq.MPQException;

public class MainWindow extends Application {

	private static final Logger LOGGER = LogManager.getLogger();
	
	private static int DEFAULT_HEIGHT = 800;
	private static int DEFAULT_WIDTH = 1200;

//	private static int[][] HC = {
//			{16, 64, 16, 0},
//			{48, 0, 48, 64},
//			{0, 16, 64, 16},
//			{64, 48, 0, 48},
//			{16, 64, 64, 16},
//			{48, 0, 0, 48},
//			{0, 16, 48, 64},
//			{64, 48, 16, 0}
//	};
//	private static int[][] MC = {
//			{32, 64, 32, 0},
//			{32, 0, 32, 64},
//			{0, 32, 64, 32},
//			{64, 32, 0, 32},
//			{32, 64, 64, 32},
//			{32, 0, 0, 32},
//			{0, 32, 32, 64},
//			{64, 32, 32, 0}
//	};
//	private static int[][] LC = {
//			{32, 64, 32, 0},
//			{32, 0, 32, 64},
//			{0, 32, 64, 32},
//			{64, 32, 0, 32},
//			{32, 64, 64, 32},
//			{32, 0, 0, 32},
//			{0, 32, 32, 64},
//			{64, 32, 32, 0}
//	};
	
	private Tileset tileset;
	private TransitionPreview transitionPreview;
	
	private Stage primaryStage;
	private Scene transitionScene;
	private Scene mapScene;
	private MapCanvas mapCanvas;
	
	public Stage getStage() {
		return primaryStage;
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		LOGGER.debug("application starting...");
		
		Settings settings = new Settings();
		SettingsDialog settingsDialog = new SettingsDialog(settings);
		
		if (settings.exists()) {
			try {
				settings.load();
			} catch (DataLayerException e) {
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("File Load Error");
				alert.setHeaderText("Could not load settings.");
				alert.setContentText(e.getLocalizedMessage());
				alert.showAndWait();
			}
		} else {
			settingsDialog.showAndWait("It appears you have not specified any settings yet.\nYou can do so now or open this dialog later\nvia 'File->Settings' or ctrl-P.");
		}
		
		this.tileset = new Tileset(settings);
		try {
			this.tileset.load();
		} catch (DataLayerException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("File Load Error");
			alert.setHeaderText("Could not load tileset.");
			alert.setContentText(e.getLocalizedMessage());
			alert.showAndWait();
		}
		
		this.transitionPreview = new TransitionPreview();
		
		this.primaryStage = primaryStage;
		
		primaryStage.setTitle("OpenBW Map Editor");
		
		VBox root1 = new VBox();
		TileTransitionPanel transitionPanel = new TileTransitionPanel(tileset, transitionPreview);
		root1.getChildren().addAll(new HeaderPanel(tileset, settingsDialog, this), transitionPanel);
		this.transitionScene = new Scene(root1, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		VBox root2 = new VBox();
		try {
			this.mapCanvas = new MapCanvas(tileset, new MapReader().readMap(), 128, 128);
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setContent(mapCanvas);
			root2.getChildren().addAll(new HeaderPanel(tileset, settingsDialog, this), scrollPane);
			
		} catch (IOException | MPQException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.mapScene = new Scene(root2, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		primaryStage.setScene(transitionScene);
		primaryStage.show();
	}

	public static void main(String[] args) throws Exception {
		
		launch(args);
	}

	public void update() {
		
		mapCanvas.update();
	}
	
	public void switchScene() {
		
		if (this.primaryStage.getScene().equals(transitionScene)) {
			this.primaryStage.setScene(mapScene);
		} else {
			this.primaryStage.setScene(transitionScene);
		}
	}
}
