package com.supermarket.pos;

import com.supermarket.pos.controller.PosController;
import com.supermarket.pos.util.DatabaseManager;
import javafx.application.Application;
import javafx.scene.Scene;
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
            DatabaseManager.initializeDatabase();
            PosController controller = new PosController();
            BorderPane root = controller.getRoot();
            Scene scene = new Scene(root, 1100, 720);

            primaryStage.setTitle("Supermarket POS System");
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(event -> {
                DatabaseManager.close();
            });

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("✗ Application startup error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
