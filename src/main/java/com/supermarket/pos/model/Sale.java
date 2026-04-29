package com.supermarket.pos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Sale entity representing a completed transaction.
 * Contains sale information including items, total amount, and timestamp.
 */
public class Sale {

    private int id;
    private LocalDateTime datetime;
    private List<CartItem> items;
    private double totalAmount;
    private double totalProfit;

    /**
     * Default constructor
     */
    public Sale() {
        this.items = new ArrayList<>();
        this.datetime = LocalDateTime.now();
        this.totalAmount = 0.0;
        this.totalProfit = 0.0;
    }

    /**
     * Constructor with datetime
     */
    public Sale(LocalDateTime datetime) {
        this.datetime = datetime;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.totalProfit = 0.0;
    }

    /**
     * Full constructor with all fields
     */
    public Sale(int id, LocalDateTime datetime, List<CartItem> items, double totalAmount, double totalProfit) {
        this.id = id;
        this.datetime = datetime;
        this.items = items != null ? items : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.totalProfit = totalProfit;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public void addItem(CartItem item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", datetime=" + datetime +
                ", itemCount=" + items.size() +
                ", totalAmount=" + totalAmount +
                ", totalProfit=" + totalProfit +
                '}';
    }
}
