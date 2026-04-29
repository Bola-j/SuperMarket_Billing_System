package com.supermarket.pos.model;

/**
 * CartItem entity representing a product item in the shopping cart.
 * Contains product reference and quantity in cart.
 */
public class CartItem {

    private Product product;
    private int quantity;
    private double totalPrice;

    /**
     * Default constructor
     */
    public CartItem() {
    }

    /**
     * Constructor with product and quantity
     */
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        calculateTotalPrice();
    }

    /**
     * Calculate total price for this cart item
     */
    private void calculateTotalPrice() {
        if (product != null) {
            this.totalPrice = product.getPrice() * quantity;
        }
    }

    // Getters and Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        calculateTotalPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
