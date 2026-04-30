package com.supermarket.pos.controller;

import com.supermarket.pos.model.CartItem;
import com.supermarket.pos.model.Product;
import com.supermarket.pos.service.CartService;
import com.supermarket.pos.service.InventoryService;
import com.supermarket.pos.service.SalesService;
import com.supermarket.pos.view.PosView;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.layout.BorderPane;

/**
 * PosController coordinates the barcode scan flow, cart updates, and checkout action.
 */
public class PosController extends BaseController {

    private final PosView view;

    public PosController() {
        this.view = new PosView();
        initialize();
    }

    private void initialize() {
        view.getBarcodeField().setOnAction(event -> handleBarcodeEntry());
        view.getAddButton().setOnAction(event -> handleDropdownEntry());
        view.getCheckoutButton().setOnAction(event -> handleCheckout());

        loadProducts();
        refreshCartState();
        view.getBarcodeField().requestFocus();
    }

    public void loadProducts() {
        try {
            List<Product> products = InventoryService.getAllProducts();
            view.getProductComboBox().getItems().setAll(products);
        } catch (SQLException ex) {
            showError("Database Error", "Failed to load products: " + ex.getMessage());
        }
    }
    private void handleBarcodeEntry() {
        String barcode = view.getBarcodeField().getText() == null ? "" : view.getBarcodeField().getText().trim();
        if (barcode.isEmpty()) {
            showWarning("Barcode Required", "Please enter or scan a barcode.");
            return;
        }

        try {
            Product product = InventoryService.getProductByBarcode(barcode);
            if (product == null) {
                showError("Product Not Found", "No product matches barcode: " + barcode);
                return;
            }

            int quantity = 1;
            try {
                quantity = Integer.parseInt(view.getQuantityField().getText().trim());
            } catch (NumberFormatException ex) {
                showWarning("Invalid Quantity", "Please enter a valid number for quantity.");
                return;
            }

            CartService.addToCart(product, quantity);
            refreshCartState();
            view.getBarcodeField().clear();
            view.getBarcodeField().requestFocus();
        } catch (IllegalArgumentException ex) {
            showWarning("Cannot Add Item", ex.getMessage());
        } catch (SQLException ex) {
            showError("Database Error", ex.getMessage());
        }
    }

    private void handleDropdownEntry() {
        Product selectedProduct = view.getProductComboBox().getValue();
        if (selectedProduct == null) {
            showWarning("Selection Required", "Please select a product from the dropdown.");
            return;
        }

        int quantity = 1;
        try {
            quantity = Integer.parseInt(view.getQuantityField().getText().trim());
        } catch (NumberFormatException ex) {
            showWarning("Invalid Quantity", "Please enter a valid number for quantity.");
            return;
        }

        try {
            CartService.addToCart(selectedProduct, quantity);
            refreshCartState();
            view.getProductComboBox().setValue(null);
            view.getQuantityField().setText("1");
        } catch (IllegalArgumentException ex) {
            showWarning("Cannot Add Item", ex.getMessage());
        }
    }

    private void handleCheckout() {
        if (CartService.isCartEmpty()) {
            showWarning("Empty Cart", "Add items before checkout.");
            return;
        }

        try {
            List<CartItem> receiptItems = CartService.getCart();
            double total = CartService.calculateTotal();
            int saleId = SalesService.completeSale();
            refreshCartState();
            showInfo("Checkout Complete", buildReceipt(saleId, receiptItems, total));
        } catch (IllegalArgumentException ex) {
            showWarning("Checkout Failed", ex.getMessage());
        } catch (SQLException ex) {
            showError("Checkout Failed", ex.getMessage());
        }
    }

    private String buildReceipt(int saleId, List<CartItem> items, double total) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Sale ID: ").append(saleId).append("\n\n");
        receipt.append("Items:\n");
        for (CartItem item : items) {
            receipt.append("- ")
                    .append(item.getProduct().getName())
                    .append(" x ")
                    .append(item.getQuantity())
                    .append(" @ EGP ")
                    .append(String.format("%.2f", item.getProduct().getSellingPrice()))
                    .append(" = EGP ")
                    .append(String.format("%.2f", item.getSubtotal()))
                    .append("\n");
        }
        receipt.append("\nTotal: EGP ").append(String.format("%.2f", total));
        return receipt.toString();
    }

    private void refreshCartState() {
        view.getCartItems().setAll(CartService.getCart());
        view.getCartTable().refresh();
        view.getTotalLabel().setText(String.format("Total Price: EGP %.2f", CartService.calculateTotal()));
    }

    public BorderPane getRoot() {
        return view.getRoot();
    }
}