package com.supermarket.pos.service;

import com.supermarket.pos.model.CartItem;
import com.supermarket.pos.model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * CartService - Business logic for shopping cart management.
 * Handles cart operations such as adding/removing items and calculating totals.
 */
public class CartService {

    private static final List<CartItem> cart = new ArrayList<>();

    /**
     * Add a product to cart
     * @param product Product to add
     * @param quantity Quantity to add
     * @throws IllegalArgumentException if product or quantity is invalid
     */
    public static void addToCart(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + product.getQuantity());
        }

        // Check if product already in cart
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                // Product already in cart, increase quantity
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        // Add new cart item
        CartItem cartItem = new CartItem(product, quantity);
        cart.add(cartItem);
    }

    /**
     * Remove a product from cart
     * @param productId ID of product to remove
     * @throws IllegalArgumentException if product not found in cart
     */
    public static void removeFromCart(int productId) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                cart.remove(item);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found in cart");
    }

    /**
     * Update quantity of item in cart
     * @param productId ID of product to update
     * @param quantity New quantity
     * @throws IllegalArgumentException if product not found or quantity invalid
     */
    public static void updateCartItemQuantity(int productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                if (item.getProduct().getQuantity() < quantity) {
                    throw new IllegalArgumentException("Insufficient stock. Available: " + item.getProduct().getQuantity());
                }
                item.setQuantity(quantity);
                return;
            }
        }
        throw new IllegalArgumentException("Product not found in cart");
    }

    /**
     * Calculate total amount for all items in cart
     * @return Total cart amount
     */
    public static double calculateTotal() {
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Calculate total cost (purchase price) for all items in cart
     * @return Total cost amount
     */
    public static double calculateCost() {
        double cost = 0.0;
        for (CartItem item : cart) {
            cost += item.getProduct().getPurchasePrice() * item.getQuantity();
        }
        return cost;
    }

    /**
     * Calculate profit for current cart
     * @return Profit amount (total - cost)
     */
    public static double calculateProfit() {
        return calculateTotal() - calculateCost();
    }

    /**
     * Get all items in cart
     * @return List of cart items
     */
    public static List<CartItem> getCart() {
        return new ArrayList<>(cart);
    }

    /**
     * Get item count in cart
     * @return Number of items
     */
    public static int getCartItemCount() {
        return cart.size();
    }

    /**
     * Get quantity of all items in cart
     * @return Total quantity
     */
    public static int getTotalQuantity() {
        int total = 0;
        for (CartItem item : cart) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Clear the cart
     */
    public static void clearCart() {
        cart.clear();
    }

    /**
     * Check if cart is empty
     * @return true if cart is empty
     */
    public static boolean isCartEmpty() {
        return cart.isEmpty();
    }

    /**
     * Get specific cart item by product ID
     * @param productId Product ID
     * @return CartItem or null if not found
     */
    public static CartItem getCartItem(int productId) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }
}
