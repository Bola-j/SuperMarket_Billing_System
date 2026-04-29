package com.supermarket.pos.dao;

import com.supermarket.pos.model.Product;
import com.supermarket.pos.util.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductDAO - Data Access Object for Product entities.
 * Handles all database operations for products.
 */
public class ProductDAO {

    /**
     * Insert a new product into database
     */
    public static void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, category, barcode, purchase_price, selling_price, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setString(3, product.getBarcode());
            stmt.setDouble(4, product.getPurchasePrice());
            stmt.setDouble(5, product.getSellingPrice());
            stmt.setInt(6, product.getQuantity());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    product.setId(keys.getInt(1));
                }
            }
        }
    }

    /**
     * Retrieve product by ID
     */
    public static Product getProductById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieve product by barcode
     */
    public static Product getProductByBarcode(String barcode) throws SQLException {
        String sql = "SELECT * FROM products WHERE barcode = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, barcode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        }
        return null;
    }

    /**
     * Retrieve all products
     */
    public static List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT * FROM products ORDER BY name";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        }

        return products;
    }

    /**
     * Update product information
     */
    public static void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, category = ?, barcode = ?, purchase_price = ?, "
                + "selling_price = ?, quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setString(3, product.getBarcode());
            stmt.setDouble(4, product.getPurchasePrice());
            stmt.setDouble(5, product.getSellingPrice());
            stmt.setInt(6, product.getQuantity());
            stmt.setInt(7, product.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Update product quantity by ID
     */
    public static void updateProductQuantity(int productId, int newQuantity) throws SQLException {
        String sql = "UPDATE products SET quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }

    /**
     * Delete product by ID
     */
    public static void deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }

    /**
     * Get products with stock below threshold
     */
    public static List<Product> getLowStockProducts(int threshold) throws SQLException {
        String sql = "SELECT * FROM products WHERE quantity < ? ORDER BY quantity ASC";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        }

        return products;
    }

    /**
     * Check if product exists
     */
    public static boolean productExists(String barcode) throws SQLException {
        String sql = "SELECT 1 FROM products WHERE barcode = ? LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, barcode);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getString("barcode"),
                rs.getDouble("purchase_price"),
                rs.getDouble("selling_price"),
                rs.getInt("quantity")
        );
    }
}
