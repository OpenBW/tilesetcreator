package org.openbw.mapeditor.gui;

import java.io.File;
import java.util.Optional;

import org.openbw.mapeditor.data.DataLayerException;
import org.openbw.mapeditor.model.Settings;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;

public class SettingsDialog {

	private Settings settings;
	private boolean scdirSelected;
	
	public SettingsDialog(Settings settings) {
		
		this.settings = settings;
	}
	
	public void showAndWait() {
		showAndWait("");
	}
	
	public void showAndWait(String message) {

		scdirSelected = false;
		
		Dialog<Settings> dialog = new Dialog<>();
		dialog.setTitle("Settings");
		dialog.setHeaderText(message);
		dialog.setResizable(true);

		Label label1 = new Label("BW Directory: ");
		TextField text1 = new TextField();
		text1.setPrefWidth(200);
		text1.setText(this.settings.getScdir());

		Button browse1 = new Button("Browse...");
		browse1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				DirectoryChooser fileChooser = new DirectoryChooser();
				fileChooser.setTitle("Choose BW Directory");
				File file = fileChooser.showDialog(null);
				if (file != null) {
					String scdir = file.getAbsolutePath();
					text1.setText(scdir);
					scdirSelected = true;
				}
			}
		});

		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(text1, 2, 1);
		grid.add(browse1, 3, 1);
		dialog.getDialogPane().setContent(grid);

		ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

		dialog.setResultConverter(new Callback<ButtonType, Settings>() {
			@Override
			public Settings call(ButtonType b) {

				if (b == buttonTypeOk) {

					settings.setScdir(text1.getText());
					if (scdirSelected) {
						settings.setTexturesFromMpq(true);
					}
					try {
						settings.store();
					} catch (DataLayerException e) {
						e.printStackTrace();
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("File Save Error");
						alert.setHeaderText("Could not save settings.");
						alert.setContentText(e.getLocalizedMessage());
						alert.showAndWait();
					}
				}

				return settings;
			}
		});

		Optional<Settings> result = dialog.showAndWait();

		if (result.isPresent()) {

			// actionStatus.setText("Result: " + result.get());
		}
	}
}
