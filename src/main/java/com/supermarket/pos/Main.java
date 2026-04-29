package com.supermarket.pos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main entry point for the Supermarket POS System application.
 * Launches the JavaFX GUI application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Create root layout
            BorderPane root = new BorderPane();
            
            // Add placeholder content
            Label welcomeLabel = new Label("Supermarket POS System - JavaFX Application");
            welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            root.setCenter(welcomeLabel);

            // Create scene
            Scene scene = new Scene(root, 1024, 768);
            
            // Set stage properties
            primaryStage.setTitle("Supermarket POS System");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
