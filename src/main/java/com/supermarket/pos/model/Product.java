package com.supermarket.pos.model;

/**
 * Product entity representing a supermarket product.
 * Contains product information such as ID, name, price, and quantity.
 */
public class Product {

    private int productId;
    private String name;
    private double price;
    private int quantity;
    private String category;
    private String barcode;

    /**
     * Default constructor
     */
    public Product() {
    }

    /**
     * Constructor with basic fields
     */
    public Product(int productId, String name, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Full constructor
     */
    public Product(int productId, String name, double price, int quantity, String category, String barcode) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.barcode = barcode;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", barcode='" + barcode + '\'' +
                '}';
    }
}
