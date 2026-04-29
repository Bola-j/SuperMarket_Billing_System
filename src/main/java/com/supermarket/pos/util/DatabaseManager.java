package com.supermarket.pos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseManager handles SQLite database connections and operations.
 * Provides methods for database initialization and connection management.
 */
public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:supermarket_pos.db";
    private static Connection connection;

    /**
     * Private constructor to prevent instantiation
     */
    private DatabaseManager() {
    }

    /**
     * Establish a database connection
     */
    public static Connection connect() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DATABASE_URL);
                System.out.println("✓ Connected to database: " + DATABASE_URL);
            }
        } catch (SQLException e) {
            System.err.println("✗ Connection error: " + e.getMessage());
            throw e;
        }
        return connection;
    }

    /**
     * Get the active database connection
     */
    public static Connection getConnection() throws SQLException {
        return connect();
    }

    /**
     * Close the database connection
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Initialize the database and create tables if they don't exist
     */
    public static void initializeDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Create PRODUCTS table
            String createProductsTable = "CREATE TABLE IF NOT EXISTS products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "category TEXT," +
                    "barcode TEXT UNIQUE NOT NULL," +
                    "purchase_price REAL NOT NULL," +
                    "selling_price REAL NOT NULL," +
                    "quantity INTEGER DEFAULT 0" +
                    ")";
            stmt.execute(createProductsTable);
            System.out.println("✓ Products table created/verified");

            // Create SALES table
            String createSalesTable = "CREATE TABLE IF NOT EXISTS sales (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "datetime TEXT NOT NULL," +
                    "total_amount REAL NOT NULL," +
                    "total_profit REAL NOT NULL" +
                    ")";
            stmt.execute(createSalesTable);
            System.out.println("✓ Sales table created/verified");

            // Create SALE_ITEMS table
            String createSaleItemsTable = "CREATE TABLE IF NOT EXISTS sale_items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "sale_id INTEGER NOT NULL," +
                    "product_id INTEGER NOT NULL," +
                    "quantity INTEGER NOT NULL," +
                    "price_at_sale REAL NOT NULL," +
                    "FOREIGN KEY (sale_id) REFERENCES sales(id)," +
                    "FOREIGN KEY (product_id) REFERENCES products(id)" +
                    ")";
            stmt.execute(createSaleItemsTable);
            System.out.println("✓ Sale Items table created/verified");

            seedSampleData(conn);

            System.out.println("✓ Database initialization completed successfully");

        } catch (SQLException e) {
            System.err.println("✗ Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try {
            Connection conn = connect();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection test PASSED");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Database connection test FAILED: " + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Get database file path
     */
    public static String getDatabasePath() {
        return "supermarket_pos.db";
    }

    private static void seedSampleData(Connection conn) throws SQLException {
        String countSql = "SELECT COUNT(*) FROM products";
        try (PreparedStatement countStmt = conn.prepareStatement(countSql);
             ResultSet rs = countStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }

        String insertSql = "INSERT INTO products (name, category, barcode, purchase_price, selling_price, quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            addSampleProduct(stmt, "Milk 1L", "Dairy", "100000001", 1.20, 1.75, 20);
            addSampleProduct(stmt, "Bread", "Bakery", "100000002", 0.70, 1.20, 15);
            addSampleProduct(stmt, "Eggs 12pk", "Dairy", "100000003", 1.50, 2.30, 12);
            addSampleProduct(stmt, "Apples", "Produce", "100000004", 0.80, 1.50, 18);
            addSampleProduct(stmt, "Rice 2kg", "Grocery", "100000005", 2.40, 3.80, 8);
            addSampleProduct(stmt, "Coffee", "Beverage", "100000006", 3.00, 4.50, 6);
        }
    }

    private static void addSampleProduct(PreparedStatement stmt, String name, String category, String barcode,
                                         double purchasePrice, double sellingPrice, int quantity) throws SQLException {
        stmt.setString(1, name);
        stmt.setString(2, category);
        stmt.setString(3, barcode);
        stmt.setDouble(4, purchasePrice);
        stmt.setDouble(5, sellingPrice);
        stmt.setInt(6, quantity);
        stmt.executeUpdate();
    }
}
