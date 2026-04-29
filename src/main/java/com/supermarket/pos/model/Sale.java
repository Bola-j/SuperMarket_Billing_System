package com.supermarket.pos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Sale entity representing a completed transaction.
 * Contains sale information including items, total amount, and timestamp.
 */
public class Sale {

    private int saleId;
    private LocalDateTime saleDate;
    private List<CartItem> items;
    private double subtotal;
    private double taxAmount;
    private double totalAmount;
    private String paymentMethod;
    private String cashierName;

    /**
     * Default constructor
     */
    public Sale() {
        this.items = new ArrayList<>();
        this.saleDate = LocalDateTime.now();
    }

    /**
     * Constructor with basic fields
     */
    public Sale(int saleId, LocalDateTime saleDate, double totalAmount, String paymentMethod) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(CartItem item) {
        this.items.add(item);
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", saleDate=" + saleDate +
                ", items=" + items.size() +
                ", subtotal=" + subtotal +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", cashierName='" + cashierName + '\'' +
                '}';
    }
}
