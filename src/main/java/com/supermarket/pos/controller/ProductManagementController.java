package com.supermarket.pos.controller;

import com.supermarket.pos.dao.ProductDAO;
import com.supermarket.pos.model.Product;
import com.supermarket.pos.view.ProductManagementView;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * ProductManagementController manages the product CRUD workflow.
 */
public class ProductManagementController extends BaseController {

    private final ProductManagementView view;

    public ProductManagementController() {
        this.view = new ProductManagementView();
        initialize();
    }

    private void initialize() {
        view.getAddButton().setOnAction(event -> handleAdd());
        view.getEditButton().setOnAction(event -> handleEdit());
        view.getDeleteButton().setOnAction(event -> handleDelete());

        view.getProductTable().getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> populateForm(newValue));

        refreshProducts();
    }

    private void handleAdd() {
        try {
            Product product = buildProductFromForm(0);
            ProductDAO.addProduct(product);
            refreshProducts();
            clearForm();
            showInfo("Product Added", "Product saved successfully.");
        } catch (IllegalArgumentException ex) {
            showWarning("Validation Error", ex.getMessage());
        } catch (SQLException ex) {
            showError("Database Error", ex.getMessage());
        }
    }

    private void handleEdit() {
        Product selected = view.getProductTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Select a product to edit.");
            return;
        }

        try {
            Product updated = buildProductFromForm(selected.getId());
            ProductDAO.updateProduct(updated);
            refreshProducts();
            clearForm();
            showInfo("Product Updated", "Product updated successfully.");
        } catch (IllegalArgumentException ex) {
            showWarning("Validation Error", ex.getMessage());
        } catch (SQLException ex) {
            showError("Database Error", ex.getMessage());
        }
    }

    private void handleDelete() {
        Product selected = view.getProductTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Select a product to delete.");
            return;
        }

        try {
            ProductDAO.deleteProduct(selected.getId());
            refreshProducts();
            clearForm();
            showInfo("Product Deleted", "Product removed successfully.");
        } catch (SQLException ex) {
            showError("Database Error", ex.getMessage());
        }
    }

    private Product buildProductFromForm(int id) {
        String name = view.getNameField().getText() == null ? "" : view.getNameField().getText().trim();
        String category = view.getCategoryField().getText() == null ? "" : view.getCategoryField().getText().trim();
        String barcode = view.getBarcodeField().getText() == null ? "" : view.getBarcodeField().getText().trim();
        double purchasePrice = parseDouble(view.getPurchasePriceField().getText(), "Purchase price");
        double sellingPrice = parseDouble(view.getSellingPriceField().getText(), "Selling price");
        int quantity = parseInt(view.getQuantityField().getText(), "Quantity");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (barcode.isEmpty()) {
            throw new IllegalArgumentException("Barcode is required.");
        }
        if (purchasePrice <= 0) {
            throw new IllegalArgumentException("Purchase price must be greater than 0.");
        }
        if (sellingPrice <= 0) {
            throw new IllegalArgumentException("Selling price must be greater than 0.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        Product product = new Product(id, name, category, barcode, purchasePrice, sellingPrice, quantity);
        return product;
    }

    private double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }
    }

    private int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid whole number.");
        }
    }

    private void populateForm(Product product) {
        if (product == null) {
            return;
        }
        view.getNameField().setText(product.getName());
        view.getCategoryField().setText(product.getCategory());
        view.getBarcodeField().setText(product.getBarcode());
        view.getPurchasePriceField().setText(String.valueOf(product.getPurchasePrice()));
        view.getSellingPriceField().setText(String.valueOf(product.getSellingPrice()));
        view.getQuantityField().setText(String.valueOf(product.getQuantity()));
    }

    public void refreshProducts() {
        try {
            List<Product> products = ProductDAO.getAllProducts();
            view.getProducts().setAll(products);
            TableView<Product> table = view.getProductTable();
            table.refresh();
        } catch (SQLException ex) {
            showError("Database Error", ex.getMessage());
        }
    }

    private void clearForm() {
        view.getNameField().clear();
        view.getCategoryField().clear();
        view.getBarcodeField().clear();
        view.getPurchasePriceField().clear();
        view.getSellingPriceField().clear();
        view.getQuantityField().clear();
    }

    public BorderPane getRoot() {
        return view.getRoot();
    }
}
