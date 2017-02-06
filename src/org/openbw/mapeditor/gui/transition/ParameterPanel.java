package org.openbw.mapeditor.gui.transition;

import org.openbw.mapeditor.model.tiles.GeneratorConfiguration;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ParameterPanel extends GridPane {

	 	final Slider coveredArea = new Slider(0, 64, 32);    
	    final Slider volatility = new Slider(0, 10, 5);
	    final Slider gradient = new Slider (0, 8, 4);
	 
	    final Label coveredAreaValue = new Label(
	        Integer.toString((int)coveredArea.getValue()));
	    final Label volatilityValue = new Label(
	    		Integer.toString((int)volatility.getValue()));
	    final Label gradientValue = new Label(
	    		Integer.toString((int)gradient.getValue()));
	 
	    public ParameterPanel(GeneratorConfiguration configuration) {
	    	
	        this.setPadding(new Insets(10, 10, 10, 10));
	        this.setVgap(10);
	        this.setHgap(40);
	        final Color textColor = Color.BLACK;
		    
	        coveredAreaValue.setPrefWidth(30);
	        volatilityValue.setPrefWidth(30);
	        gradientValue.setPrefWidth(30);
	        
	        final Label coveredAreaCaption = new Label("Covered Area:");
		    coveredAreaCaption.setTextFill(textColor);
	        GridPane.setConstraints(coveredAreaCaption, 0, 1);
	        this.getChildren().add(coveredAreaCaption);
	        
	 
	        coveredArea.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    
	            	coveredAreaValue.setText(String.format("%d", new_val.intValue()));
	            	configuration.setCoveredArea(new_val.intValue());
	            }
	        });
	 
	        GridPane.setConstraints(coveredArea, 1, 1);
	        this.getChildren().add(coveredArea);
	 
	        coveredAreaValue.setTextFill(textColor);
	        GridPane.setConstraints(coveredAreaValue, 2, 1);
	        this.getChildren().add(coveredAreaValue);
	 
	        final Label volatilityCaption = new Label("Volatility:");
		    volatilityCaption.setTextFill(textColor);
	        GridPane.setConstraints(volatilityCaption, 0, 2);
	        this.getChildren().add(volatilityCaption);
	 
	        volatility.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    
	            	volatilityValue.setText(String.format("%d", new_val.intValue()));
	            	configuration.setVolatility(new_val.intValue());
	            }
	        });
	        GridPane.setConstraints(volatility, 1, 2);
	        this.getChildren().add(volatility);
	 
	        volatilityValue.setTextFill(textColor);
	        GridPane.setConstraints(volatilityValue, 2, 2);
	        this.getChildren().add(volatilityValue);
	 
	        final Label borderGradientCaption = new Label("Border Gradient:");
	   	 	borderGradientCaption.setTextFill(textColor);
	        GridPane.setConstraints(borderGradientCaption, 0, 3);
	        this.getChildren().add(borderGradientCaption);
	 
	        gradient.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    
	            	gradientValue.setText(String.format("%d", new_val.intValue()));
	            	configuration.setGradient(new_val.intValue());
	            }
	        });
	        GridPane.setConstraints(gradient, 1, 3);
	        this.getChildren().add(gradient);
	 
	        gradientValue.setTextFill(textColor);
	        GridPane.setConstraints(gradientValue, 2, 3);
	        this.getChildren().add(gradientValue);
	    }
	 
}
