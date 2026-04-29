package com.supermarket.pos.service;

import com.supermarket.pos.dao.ProductDAO;
import com.supermarket.pos.model.Product;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * InventoryService - Business logic for inventory management.
 * Handles product management, restocking, and stock level monitoring.
 */
public class InventoryService {

    private static final int LOW_STOCK_THRESHOLD = 10;

    /**
     * Add a new product to inventory
     * @param product Product to add
     * @throws SQLException if database operation fails
     */
    public static void addProduct(Product product) throws SQLException {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getBarcode() == null || product.getBarcode().trim().isEmpty()) {
            throw new IllegalArgumentException("Product barcode cannot be empty");
        }
        if (product.getSellingPrice() <= 0) {
            throw new IllegalArgumentException("Selling price must be greater than 0");
        }
        if (product.getPurchasePrice() <= 0) {
            throw new IllegalArgumentException("Purchase price must be greater than 0");
        }

        // Check if product already exists
        if (ProductDAO.productExists(product.getBarcode())) {
            throw new IllegalArgumentException("Product with barcode " + product.getBarcode() + " already exists");
        }

        ProductDAO.addProduct(product);
    }

    /**
     * Restock a product by increasing its quantity
     * @param productId ID of product to restock
     * @param quantity Quantity to add to stock
     * @throws SQLException if database operation fails
     */
    public static void restockProduct(int productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be greater than 0");
        }

        Product product = ProductDAO.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with ID " + productId + " not found");
        }

        int newQuantity = product.getQuantity() + quantity;
        ProductDAO.updateProductQuantity(productId, newQuantity);
    }

    /**
     * Get all products with stock below threshold
     * @return List of low stock products
     * @throws SQLException if database operation fails
     */
    public static List<Product> getLowStockProducts() throws SQLException {
        return getLowStockProducts(LOW_STOCK_THRESHOLD);
    }

    /**
     * Get all products with stock below specified threshold
     * @param threshold Stock level threshold
     * @return List of low stock products
     * @throws SQLException if database operation fails
     */
    public static List<Product> getLowStockProducts(int threshold) throws SQLException {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }

        List<Product> lowStockProducts = ProductDAO.getLowStockProducts(threshold);
        return lowStockProducts != null ? lowStockProducts : new ArrayList<>();
    }

    /**
     * Get all products in inventory
     * @return List of all products
     * @throws SQLException if database operation fails
     */
    public static List<Product> getAllProducts() throws SQLException {
        List<Product> products = ProductDAO.getAllProducts();
        return products != null ? products : new ArrayList<>();
    }

    /**
     * Get product by ID
     * @param productId Product ID
     * @return Product or null if not found
     * @throws SQLException if database operation fails
     */
    public static Product getProductById(int productId) throws SQLException {
        return ProductDAO.getProductById(productId);
    }

    /**
     * Get product by barcode
     * @param barcode Product barcode
     * @return Product or null if not found
     * @throws SQLException if database operation fails
     */
    public static Product getProductByBarcode(String barcode) throws SQLException {
        if (barcode == null || barcode.trim().isEmpty()) {
            throw new IllegalArgumentException("Barcode cannot be empty");
        }
        return ProductDAO.getProductByBarcode(barcode);
    }

    /**
     * Update product information
     * @param product Updated product
     * @throws SQLException if database operation fails
     */
    public static void updateProduct(Product product) throws SQLException {
        if (product == null || product.getId() <= 0) {
            throw new IllegalArgumentException("Invalid product");
        }
        ProductDAO.updateProduct(product);
    }

    /**
     * Delete product from inventory
     * @param productId ID of product to delete
     * @throws SQLException if database operation fails
     */
    public static void deleteProduct(int productId) throws SQLException {
        if (productId <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        ProductDAO.deleteProduct(productId);
    }
}
