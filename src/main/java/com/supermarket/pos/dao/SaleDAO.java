package com.supermarket.pos.dao;

import com.supermarket.pos.model.CartItem;
import com.supermarket.pos.model.Product;
import com.supermarket.pos.model.Sale;
import com.supermarket.pos.util.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SaleDAO - Data Access Object for Sale entities.
 * Handles all database operations for sales and sale items.
 */
public class SaleDAO {

    /**
     * Insert a new sale into database
     */
    public static int addSale(Sale sale) throws SQLException {
        String saleSql = "INSERT INTO sales (datetime, total_amount, total_profit) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO sale_items (sale_id, product_id, quantity, price_at_sale) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement saleStmt = conn.prepareStatement(saleSql, Statement.RETURN_GENERATED_KEYS)) {
                saleStmt.setString(1, sale.getDatetime().toString());
                saleStmt.setDouble(2, sale.getTotalAmount());
                saleStmt.setDouble(3, sale.getTotalProfit());
                saleStmt.executeUpdate();

                int saleId;
                try (ResultSet keys = saleStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        saleId = keys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve generated sale ID");
                    }
                }

                try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                    for (CartItem item : sale.getItems()) {
                        itemStmt.setInt(1, saleId);
                        itemStmt.setInt(2, item.getProduct().getId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.setDouble(4, item.getProduct().getSellingPrice());
                        itemStmt.addBatch();
                    }
                    itemStmt.executeBatch();
                }

                conn.commit();
                conn.setAutoCommit(autoCommit);
                return saleId;
            } catch (SQLException ex) {
                conn.rollback();
                conn.setAutoCommit(autoCommit);
                throw ex;
            }
        }
    }

    /**
     * Retrieve sale by ID
     */
    public static Sale getSaleById(int saleId) throws SQLException {
        String sql = "SELECT * FROM sales WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Sale sale = mapSale(rs);
                    sale.setItems(getSaleItems(conn, saleId));
                    return sale;
                }
            }
        }
        return null;
    }

    /**
     * Retrieve all sales
     */
    public static List<Sale> getAllSales() throws SQLException {
        String sql = "SELECT * FROM sales ORDER BY datetime DESC";
        List<Sale> sales = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Sale sale = mapSale(rs);
                sale.setItems(getSaleItems(conn, sale.getId()));
                sales.add(sale);
            }
        }

        return sales;
    }

    /**
     * Retrieve sales within date range
     */
    public static List<Sale> getSalesByDateRange(String startDate, String endDate) throws SQLException {
        String sql = "SELECT * FROM sales WHERE datetime BETWEEN ? AND ? ORDER BY datetime DESC";
        List<Sale> sales = new ArrayList<>();
        String start = startDate + "T00:00:00";
        String end = endDate + "T23:59:59";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, start);
            stmt.setString(2, end);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Sale sale = mapSale(rs);
                    sale.setItems(getSaleItems(conn, sale.getId()));
                    sales.add(sale);
                }
            }
        }

        return sales;
    }

    /**
     * Get total revenue from all sales
     */
    public static double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(total_amount) FROM sales";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double value = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : value;
            }
        }
        return 0.0;
    }

    /**
     * Get total profit from all sales
     */
    public static double getTotalProfit() throws SQLException {
        String sql = "SELECT SUM(total_profit) FROM sales";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                double value = rs.getDouble(1);
                return rs.wasNull() ? 0.0 : value;
            }
        }
        return 0.0;
    }

    /**
     * Delete sale by ID
     */
    public static void deleteSale(int saleId) throws SQLException {
        String deleteItems = "DELETE FROM sale_items WHERE sale_id = ?";
        String deleteSale = "DELETE FROM sales WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement itemStmt = conn.prepareStatement(deleteItems);
                 PreparedStatement saleStmt = conn.prepareStatement(deleteSale)) {
                itemStmt.setInt(1, saleId);
                itemStmt.executeUpdate();

                saleStmt.setInt(1, saleId);
                saleStmt.executeUpdate();

                conn.commit();
                conn.setAutoCommit(autoCommit);
            } catch (SQLException ex) {
                conn.rollback();
                conn.setAutoCommit(autoCommit);
                throw ex;
            }
        }
    }

    /**
     * Clear all sales history
     */
    public static void clearAllSales() throws SQLException {
        String deleteItems = "DELETE FROM sale_items";
        String deleteSales = "DELETE FROM sales";
        try (Connection conn = DatabaseManager.getConnection()) {
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteItems);
                stmt.executeUpdate(deleteSales);
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(autoCommit);
            }
        }
    }

    private static Sale mapSale(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setId(rs.getInt("id"));
        sale.setDatetime(java.time.LocalDateTime.parse(rs.getString("datetime")));
        sale.setTotalAmount(rs.getDouble("total_amount"));
        sale.setTotalProfit(rs.getDouble("total_profit"));
        return sale;
    }

    private static List<CartItem> getSaleItems(Connection conn, int saleId) throws SQLException {
        String sql = "SELECT si.quantity, si.price_at_sale, p.id, p.name, p.category, p.barcode, "
                + "p.purchase_price, p.selling_price, p.quantity "
                + "FROM sale_items si JOIN products p ON si.product_id = p.id WHERE si.sale_id = ?";
        List<CartItem> items = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, saleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getString("barcode"),
                            rs.getDouble("purchase_price"),
                            rs.getDouble("selling_price"),
                            rs.getInt("quantity")
                    );
                    CartItem item = new CartItem(product, rs.getInt("quantity"));
                    items.add(item);
                }
            }
        }

        return items;
    }
}
