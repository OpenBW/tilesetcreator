package org.openbw.mapeditor.gui;

import java.io.File;
import java.net.MalformedURLException;

import org.openbw.mapeditor.data.Tileset;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class HeaderPanel extends VBox {

	private Tileset tileset;
	private MainWindow mainWindow;
	
	public HeaderPanel(Tileset tileset, MainWindow mainWindow) {
		
		this.tileset = tileset;
		this.mainWindow = mainWindow;
		
		this.getChildren().add(createMenuBar(mainWindow.getStage()));
		this.getChildren().add(createTextureBar());
	}
	
	private Button createTextureButton(String url, int index) {
		
		Button button = new Button();
		button.setTooltip(new Tooltip(Tileset.TEXTURE_NAMES[index]));
		button.setStyle("-fx-background-image: url('" + url + "');");
		button.setPrefSize(64, 64);
		
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Texture PNG");
				fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Portable Network Graphics", "*.PNG"));
				File file = fileChooser.showOpenDialog(mainWindow.getStage());
				if (file != null) {
					try {
						button.setStyle("-fx-background-image: url('" + file.toURI().toURL() + "');");
						tileset.setTexture(index, new Image(file.toURI().toURL().toString()));
						mainWindow.update();
					} catch (MalformedURLException e) {
						// ignore
					}
				}
			}
		});
		
		return button;
	}

	private Menu createFileMenu(Stage parentWindow) {
		
		Menu menu = new Menu("File");
        
        MenuItem item1 = new MenuItem("New Project...");
        item1.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            	// TODO action
            }
        });
        MenuItem item2 = new MenuItem("Settings");
        item2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            	// TODO action
            }
        });
        MenuItem item3 = new MenuItem("Exit");
        item3.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            	// TODO add "are you sure?" dialog
            	parentWindow.close();
            }
        });
        
        menu.getItems().addAll(item1, item2, item3);
           
        return menu;
	}
	
	private Menu createViewMenu() {
		
		Menu menu = new Menu("View");
	    
		MenuItem item1 = new MenuItem("Toggle map preview");
        item1.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        item1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
            	mainWindow.switchScene();
            }
        });
        
        menu.getItems().addAll(item1);
        
		return menu;
	}
	
	private MenuBar createMenuBar(Stage parentWindow) {
		
		MenuBar menuBar = new MenuBar();
         
        // Menu menuEdit = new Menu("Edit");
         
        // TODO create menu items
        
        menuBar.getMenus().addAll(createFileMenu(parentWindow), 
        						  //menuEdit, 
        						createViewMenu());
        
        return menuBar;
	}
	
	private ToolBar createTextureBar() {
		
		Button[] textureButtons = new Button[Tileset.TEXTURE_NAMES.length];
		for (int i = 0; i < textureButtons.length; i++) {
			
			// TODO read this from StarDat.MPQ (if location was specified)
			textureButtons[i] = createTextureButton("/org/openbw/mapeditor/data/original_" + (i + 1) + ".png", i);
		}
		ToolBar textureBar = new ToolBar(textureButtons);
		
		return textureBar;
	}
}
