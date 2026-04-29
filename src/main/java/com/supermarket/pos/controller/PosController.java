package com.supermarket.pos.controller;

import com.supermarket.pos.model.Product;
import com.supermarket.pos.service.CartService;
import com.supermarket.pos.service.InventoryService;
import com.supermarket.pos.service.SalesService;
import com.supermarket.pos.view.PosView;
import java.sql.SQLException;
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
        view.getCheckoutButton().setOnAction(event -> handleCheckout());

        refreshCartState();
        view.getBarcodeField().requestFocus();
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

            CartService.addToCart(product, 1);
            refreshCartState();
            view.getBarcodeField().clear();
            view.getBarcodeField().requestFocus();
        } catch (IllegalArgumentException ex) {
            showWarning("Cannot Add Item", ex.getMessage());
        } catch (SQLException ex) {
            showError("Database Error", ex.getMessage());
        }
    }

    private void handleCheckout() {
        if (CartService.isCartEmpty()) {
            showWarning("Empty Cart", "Add items before checkout.");
            return;
        }

        try {
            int saleId = SalesService.completeSale();
            CartService.clearCart();
            refreshCartState();
            showInfo("Checkout Complete", "Sale completed successfully. Sale ID: " + saleId);
        } catch (IllegalArgumentException ex) {
            showWarning("Checkout Failed", ex.getMessage());
        } catch (SQLException ex) {
            showError("Checkout Failed", ex.getMessage());
        }
    }

    private void refreshCartState() {
        view.getCartItems().setAll(CartService.getCart());
        view.getCartTable().refresh();
        view.getTotalLabel().setText(String.format("Total Price: $%.2f", CartService.calculateTotal()));
    }

    public BorderPane getRoot() {
        return view.getRoot();
    }
}