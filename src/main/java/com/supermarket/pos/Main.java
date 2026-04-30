package com.supermarket.pos;

import com.supermarket.pos.controller.PosController;
import com.supermarket.pos.controller.InventoryController;
import com.supermarket.pos.controller.ProductManagementController;
import com.supermarket.pos.controller.DashboardController;
import com.supermarket.pos.util.DatabaseManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
            PosController posController = new PosController();
            ProductManagementController productController = new ProductManagementController();
            InventoryController inventoryController = new InventoryController();
            DashboardController dashboardController = new DashboardController();

            TabPane tabPane = new TabPane();
            Tab dashboardTab = new Tab("Dashboard", dashboardController.getRoot());
            Tab posTab = new Tab("POS", posController.getRoot());
            Tab productsTab = new Tab("Products", productController.getRoot());
            Tab inventoryTab = new Tab("Inventory", inventoryController.getRoot());
            dashboardTab.setClosable(false);
            posTab.setClosable(false);
            productsTab.setClosable(false);
            inventoryTab.setClosable(false);

            // Refresh dashboard when its tab is selected
            dashboardTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    dashboardController.refreshDashboard();
                }
            });

            // Refresh POS product list when its tab is selected
            posTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    posController.loadProducts();
                }
            });

            // Refresh Products list when its tab is selected
            productsTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    productController.refreshProducts();
                }
            });

            // Refresh Inventory list when its tab is selected
            inventoryTab.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    inventoryController.refreshInventory();
                }
            });

            tabPane.getTabs().addAll(dashboardTab, posTab, productsTab, inventoryTab);

            Scene scene = new Scene(tabPane, 1200, 760);

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
