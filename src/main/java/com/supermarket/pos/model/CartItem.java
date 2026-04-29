package com.supermarket.pos.model;

/**
 * CartItem entity representing a product item in the shopping cart.
 * Contains product reference and quantity in cart.
 */
public class CartItem {

    private Product product;
    private int quantity;

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
    }

    /**
     * Get subtotal for this cart item (quantity * selling price)
     * @return subtotal amount
     */
    public double getSubtotal() {
        if (product != null) {
            return product.getSellingPrice() * quantity;
        }
        return 0.0;
    }

    // Getters and Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + (product != null ? product.getName() : "null") +
                ", quantity=" + quantity +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
