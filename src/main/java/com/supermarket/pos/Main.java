package com.supermarket.pos;

import com.supermarket.pos.util.DatabaseManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main entry point for the Supermarket POS System application.
 * Launches the JavaFX GUI application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            System.out.println("═══════════════════════════════════════");
            System.out.println("Initializing Supermarket POS System");
            System.out.println("═══════════════════════════════════════");
            DatabaseManager.initializeDatabase();
            System.out.println();

            // Test database connection
            boolean connectionTest = DatabaseManager.testConnection();
            System.out.println();

            if (!connectionTest) {
                System.err.println("Warning: Database connection test failed!");
            }

            // Create root layout
            BorderPane root = new BorderPane();

            // Add header
            Label titleLabel = new Label("Supermarket POS System");
            titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 15px;");

            // Add status info
            VBox statusBox = new VBox(10);
            statusBox.setStyle("-fx-padding: 15px; -fx-border-color: #cccccc; -fx-border-width: 1;");

            Label statusTitle = new Label("System Status");
            statusTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Label dbStatus = new Label("Database: " + DatabaseManager.getDatabasePath());
            Label connectionStatus = new Label("Connection: " + (connectionTest ? "✓ OK" : "✗ Failed"));

            statusBox.getChildren().addAll(statusTitle, dbStatus, connectionStatus);

            // Add to root
            root.setTop(titleLabel);
            root.setCenter(statusBox);

            // Create scene
            Scene scene = new Scene(root, 1024, 768);

            // Set stage properties
            primaryStage.setTitle("Supermarket POS System");
            primaryStage.setScene(scene);

            // Handle window close event
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("═══════════════════════════════════════");
                System.out.println("Closing application...");
                DatabaseManager.close();
                System.out.println("═══════════════════════════════════════");
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
