package com.supermarket.pos.controller;

import com.supermarket.pos.model.Product;
import com.supermarket.pos.service.InventoryService;
import com.supermarket.pos.view.InventoryView;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.layout.BorderPane;

/**
 * InventoryController handles inventory UI actions and data refresh.
 */
public class InventoryController extends BaseController {

    private final InventoryView view;

    public InventoryController() {
        this.view = new InventoryView();
        initialize();
    }

    private void initialize() {
        view.getRestockButton().setOnAction(event -> handleRestock());
        refreshInventory();
    }

    private void handleRestock() {
        Product selected = view.getInventoryTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Select a product to restock.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(view.getRestockQuantityField().getText().trim());
        } catch (NumberFormatException ex) {
            showWarning("Invalid Quantity", "Enter a valid whole number to restock.");
            return;
        }

        if (quantity <= 0) {
            showWarning("Invalid Quantity", "Quantity must be greater than 0.");
            return;
        }

        try {
            InventoryService.restockProduct(selected.getId(), quantity);
            refreshInventory();
            view.getRestockQuantityField().clear();
            showInfo("Restock Complete", "Inventory updated successfully.");
        } catch (SQLException ex) {
            showError("Database Error", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            showWarning("Restock Failed", ex.getMessage());
        }
    }

    public void refreshInventory() {
        try {
            List<Product> products = InventoryService.getAllProducts();
            view.getProducts().setAll(products);
            view.getInventoryTable().refresh();
        } catch (SQLException ex) {
            showError("Database Error", ex.getMessage());
        }
    }

    public BorderPane getRoot() {
        return view.getRoot();
    }
}
