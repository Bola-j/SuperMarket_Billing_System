package com.supermarket.pos.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * DashboardView builds the analytics dashboard screen.
 */
public class DashboardView extends BaseView {

    private final BorderPane root;
    private final Label totalRevenueLabel;
    private final Label totalProfitLabel;
    private final Label totalItemsSoldLabel;
    private final BarChart<String, Number> topSellingChart;
    private final PieChart categoryChart;
    private final ListView<String> lowStockList;
    private final ListView<String> unsoldList;

    private final ObservableList<PieChart.Data> categoryData;

    public DashboardView() {
        this.root = new BorderPane();
        this.totalRevenueLabel = new Label("$0.00");
        this.totalProfitLabel = new Label("$0.00");
        this.totalItemsSoldLabel = new Label("0");
        this.categoryData = FXCollections.observableArrayList();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        this.topSellingChart = new BarChart<>(xAxis, yAxis);
        this.categoryChart = new PieChart(categoryData);
        this.lowStockList = new ListView<>();
        this.unsoldList = new ListView<>();

        buildView();
    }

    private void buildView() {
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #f0fdf4, #fefce8);");
        root.setTop(createHeader());
        root.setCenter(createChartsSection());
        root.setBottom(createListsSection());
    }

    private Node createHeader() {
        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        HBox metrics = new HBox(16,
                buildMetricCard("Total Revenue", totalRevenueLabel),
                buildMetricCard("Total Profit", totalProfitLabel),
                buildMetricCard("Total Items Sold", totalItemsSoldLabel)
        );
        metrics.setAlignment(Pos.CENTER_LEFT);

        VBox header = new VBox(10, title, metrics);
        header.setPadding(new Insets(0, 0, 12, 0));
        return header;
    }

    private Node buildMetricCard(String title, Label valueLabel) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        VBox card = new VBox(6, titleLabel, valueLabel);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-radius: 6; -fx-background-radius: 6;");
        return card;
    }

    private Node createChartsSection() {
        topSellingChart.setTitle("Top Selling Products");
        topSellingChart.setLegendVisible(false);
        topSellingChart.setAnimated(false);

        categoryChart.setTitle("Category Distribution");
        categoryChart.setLegendVisible(true);

        HBox charts = new HBox(16, topSellingChart, categoryChart);
        charts.setPadding(new Insets(8, 0, 12, 0));
        HBox.setHgrow(topSellingChart, Priority.ALWAYS);
        HBox.setHgrow(categoryChart, Priority.ALWAYS);
        return charts;
    }

    private Node createListsSection() {
        VBox lowStockBox = new VBox(6, new Label("Low Stock Products"), lowStockList);
        VBox unsoldBox = new VBox(6, new Label("Unsold Products"), unsoldList);

        lowStockList.setPrefHeight(140);
        unsoldList.setPrefHeight(140);

        HBox lists = new HBox(16, lowStockBox, unsoldBox);
        HBox.setHgrow(lowStockBox, Priority.ALWAYS);
        HBox.setHgrow(unsoldBox, Priority.ALWAYS);
        return lists;
    }

    public BorderPane getRoot() {
        return root;
    }

    public Label getTotalRevenueLabel() {
        return totalRevenueLabel;
    }

    public Label getTotalProfitLabel() {
        return totalProfitLabel;
    }

    public Label getTotalItemsSoldLabel() {
        return totalItemsSoldLabel;
    }

    public BarChart<String, Number> getTopSellingChart() {
        return topSellingChart;
    }

    public PieChart getCategoryChart() {
        return categoryChart;
    }

    public ObservableList<PieChart.Data> getCategoryData() {
        return categoryData;
    }

    public ListView<String> getLowStockList() {
        return lowStockList;
    }

    public ListView<String> getUnsoldList() {
        return unsoldList;
    }
}
