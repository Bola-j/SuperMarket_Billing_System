package com.supermarket.pos.service;

import com.supermarket.pos.dao.SaleDAO;
import com.supermarket.pos.model.Product;
import com.supermarket.pos.model.Sale;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DashboardService - Business logic for dashboard analytics and reporting.
 * Provides metrics for sales, revenue, profit, and product performance.
 */
public class DashboardService {

    /**
     * Get total revenue from all sales
     * @return Total revenue amount
     * @throws SQLException if database operation fails
     */
    public static double getTotalRevenue() throws SQLException {
        return SaleDAO.getTotalRevenue();
    }

    /**
     * Get total profit from all sales
     * @return Total profit amount
     * @throws SQLException if database operation fails
     */
    public static double getTotalProfit() throws SQLException {
        return SaleDAO.getTotalProfit();
    }

    /**
     * Get top selling products
     * @param limit Number of top products to return
     * @return List of top selling products
     * @throws SQLException if database operation fails
     */
    public static List<Product> getTopSellingProducts(int limit) throws SQLException {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }

        List<Sale> allSales = SaleDAO.getAllSales();
        if (allSales == null || allSales.isEmpty()) {
            return new ArrayList<>();
        }

        // Count quantity sold for each product
        Map<Integer, Integer> productSalesCount = allSales.stream()
                .flatMap(sale -> sale.getItems().stream())
                .collect(Collectors.groupingBy(
                        cartItem -> cartItem.getProduct().getId(),
                        Collectors.summingInt(cartItem -> cartItem.getQuantity())
                ));

        // Sort by quantity sold and return top products
        return productSalesCount.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> {
                    try {
                        return InventoryService.getProductById(entry.getKey());
                    } catch (SQLException e) {
                        return null;
                    }
                })
                .filter(product -> product != null)
                .collect(Collectors.toList());
    }

    /**
     * Get unsold products (zero sales)
     * @return List of unsold products
     * @throws SQLException if database operation fails
     */
    public static List<Product> getUnsoldProducts() throws SQLException {
        List<Product> allProducts = InventoryService.getAllProducts();
        List<Sale> allSales = SaleDAO.getAllSales();

        if (allProducts == null || allProducts.isEmpty()) {
            return new ArrayList<>();
        }

        if (allSales == null || allSales.isEmpty()) {
            return allProducts;
        }

        // Get IDs of sold products
        java.util.Set<Integer> soldProductIds = allSales.stream()
                .flatMap(sale -> sale.getItems().stream())
                .map(cartItem -> cartItem.getProduct().getId())
                .collect(Collectors.toSet());

        // Return products not in sold set
        return allProducts.stream()
                .filter(product -> !soldProductIds.contains(product.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Get total number of sales
     * @return Number of sales transactions
     * @throws SQLException if database operation fails
     */
    public static int getTotalSalesCount() throws SQLException {
        List<Sale> sales = SaleDAO.getAllSales();
        return sales != null ? sales.size() : 0;
    }

    /**
     * Get average transaction value
     * @return Average sale amount
     * @throws SQLException if database operation fails
     */
    public static double getAverageTransactionValue() throws SQLException {
        List<Sale> sales = SaleDAO.getAllSales();
        if (sales == null || sales.isEmpty()) {
            return 0.0;
        }

        double totalRevenue = sales.stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();

        return totalRevenue / sales.size();
    }

    /**
     * Get average profit per transaction
     * @return Average profit
     * @throws SQLException if database operation fails
     */
    public static double getAverageProfitPerTransaction() throws SQLException {
        List<Sale> sales = SaleDAO.getAllSales();
        if (sales == null || sales.isEmpty()) {
            return 0.0;
        }

        double totalProfit = sales.stream()
                .mapToDouble(Sale::getTotalProfit)
                .sum();

        return totalProfit / sales.size();
    }

    /**
     * Get total inventory value (selling price)
     * @return Total inventory value
     * @throws SQLException if database operation fails
     */
    public static double getTotalInventoryValue() throws SQLException {
        List<Product> products = InventoryService.getAllProducts();
        if (products == null || products.isEmpty()) {
            return 0.0;
        }

        return products.stream()
                .mapToDouble(p -> p.getSellingPrice() * p.getQuantity())
                .sum();
    }

    /**
     * Get total inventory cost (purchase price)
     * @return Total inventory cost
     * @throws SQLException if database operation fails
     */
    public static double getTotalInventoryCost() throws SQLException {
        List<Product> products = InventoryService.getAllProducts();
        if (products == null || products.isEmpty()) {
            return 0.0;
        }

        return products.stream()
                .mapToDouble(p -> p.getPurchasePrice() * p.getQuantity())
                .sum();
    }

    /**
     * Get profit margin percentage
     * @return Profit margin as percentage
     * @throws SQLException if database operation fails
     */
    public static double getProfitMargin() throws SQLException {
        double totalRevenue = getTotalRevenue();
        if (totalRevenue == 0) {
            return 0.0;
        }

        double totalProfit = getTotalProfit();
        return (totalProfit / totalRevenue) * 100;
    }

    /**
     * Get total unique products in inventory
     * @return Number of unique products
     * @throws SQLException if database operation fails
     */
    public static int getTotalUniqueProducts() throws SQLException {
        List<Product> products = InventoryService.getAllProducts();
        return products != null ? products.size() : 0;
    }

    /**
     * Get total items in stock
     * @return Total quantity of all items
     * @throws SQLException if database operation fails
     */
    public static int getTotalItemsInStock() throws SQLException {
        List<Product> products = InventoryService.getAllProducts();
        if (products == null || products.isEmpty()) {
            return 0;
        }

        return products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
