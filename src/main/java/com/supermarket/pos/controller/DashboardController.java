package com.supermarket.pos.controller;

import com.supermarket.pos.model.Product;
import com.supermarket.pos.model.ProductSalesSummary;
import com.supermarket.pos.service.DashboardService;
import com.supermarket.pos.service.InventoryService;
import com.supermarket.pos.view.DashboardView;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

/**
 * DashboardController loads analytics data into the dashboard UI.
 */
public class DashboardController extends BaseController {

    private static final int TOP_SELLING_LIMIT = 5;

    private final DashboardView view;

    public DashboardController() {
        this.view = new DashboardView();
        initialize();
    }

    private void initialize() {
        refreshDashboard();
    }

    public void refreshDashboard() {
        loadTotals();
        loadTopSellingChart();
        loadCategoryChart();
        loadLowStockList();
        loadUnsoldList();
    }

    private void loadTotals() {
        try {
            double revenue = DashboardService.getTotalRevenue();
            double profit = DashboardService.getTotalProfit();
            int itemsSold = DashboardService.getTotalItemsSold();

            view.getTotalRevenueLabel().setText(String.format("EGP %.2f", revenue));
            view.getTotalProfitLabel().setText(String.format("EGP %.2f", profit));
            view.getTotalItemsSoldLabel().setText(String.valueOf(itemsSold));
        } catch (SQLException ex) {
            showError("Dashboard Error", ex.getMessage());
        }
    }

    private void loadTopSellingChart() {
        try {
            List<ProductSalesSummary> summaries = DashboardService.getTopSellingSummaries(TOP_SELLING_LIMIT);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (ProductSalesSummary summary : summaries) {
                series.getData().add(new XYChart.Data<>(summary.getProductName(), summary.getQuantitySold()));
            }
            view.getTopSellingChart().getData().clear();
            view.getTopSellingChart().getData().add(series);
        } catch (SQLException ex) {
            showError("Dashboard Error", ex.getMessage());
        }
    }

    private void loadCategoryChart() {
        try {
            Map<String, Integer> categorySales = DashboardService.getCategorySales();

            List<PieChart.Data> pieData = categorySales.entrySet().stream()
                    .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            view.getCategoryData().setAll(pieData);
        } catch (SQLException ex) {
            showError("Dashboard Error", ex.getMessage());
        }
    }

    private void loadLowStockList() {
        try {
            List<Product> lowStock = InventoryService.getLowStockProducts(5);
            view.getLowStockList().setItems(FXCollections.observableArrayList(
                    lowStock.stream()
                            .map(product -> product.getName() + " (" + product.getQuantity() + ")")
                            .collect(Collectors.toList())
            ));
        } catch (SQLException ex) {
            showError("Dashboard Error", ex.getMessage());
        }
    }

    private void loadUnsoldList() {
        try {
            List<Product> unsold = DashboardService.getUnsoldProducts();
            view.getUnsoldList().setItems(FXCollections.observableArrayList(
                    unsold.stream()
                            .map(Product::getName)
                            .collect(Collectors.toList())
            ));
        } catch (SQLException ex) {
            showError("Dashboard Error", ex.getMessage());
        }
    }

    public BorderPane getRoot() {
        return view.getRoot();
    }
}
