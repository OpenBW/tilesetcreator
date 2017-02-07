package org.openbw.mapeditor.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openbw.mapeditor.data.MapReader;
import org.openbw.mapeditor.data.Tileset;
import org.openbw.mapeditor.gui.transition.TileTransitionPanel;
import org.openbw.mapeditor.model.tiles.TransitionPreview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mpq.MPQException;

public class MainWindow extends Application {

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
	
	private Properties properties;
	
	private Tileset tileset;
	private TransitionPreview transitionPreview;
	
	private Stage primaryStage;
	private Scene transitionScene;
	private Scene mapScene;
	private MapCanvas mapCanvas;
	
	public Stage getStage() {
		return primaryStage;
	}
	
	private void readProperties() {
		
		properties = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("config.properties");
		} catch (FileNotFoundException e1) {
			System.out.println("Could not find property file - creating one...");
			try {
				FileOutputStream output = new FileOutputStream("config.properties");
				input = new FileInputStream("config.properties");
			} catch (FileNotFoundException e) {
				System.out.println("Could not create property file.");
				e.printStackTrace();
			}
		}
		
		try {
			properties.load(input);
			String scdir = properties.getProperty("scdir");
			
			if (scdir == null) {
				
			}
			
			input.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		readProperties();
		this.tileset = new Tileset(properties.getProperty("scdir"));
		this.transitionPreview = new TransitionPreview();
		
		this.primaryStage = primaryStage;
		
		primaryStage.setTitle("OpenBW Map Editor");
		
		VBox root1 = new VBox();
		TileTransitionPanel transitionPanel = new TileTransitionPanel(tileset, transitionPreview);
		root1.getChildren().addAll(new HeaderPanel(tileset, this), transitionPanel);
		this.transitionScene = new Scene(root1, DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		VBox root2 = new VBox();
		try {
			this.mapCanvas = new MapCanvas(tileset, new MapReader().readMap(), 128, 128);
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setContent(mapCanvas);
			root2.getChildren().addAll(new HeaderPanel(tileset, this), scrollPane);
			
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
