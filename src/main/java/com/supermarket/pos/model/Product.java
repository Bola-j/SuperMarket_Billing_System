package com.supermarket.pos.model;

/**
 * Product entity representing a supermarket product.
 * Contains product information such as ID, name, price, and quantity.
 */
public class Product {

    private int id;
    private String name;
    private String category;
    private String barcode;
    private double purchasePrice;
    private double sellingPrice;
    private int quantity;

    /**
     * Default constructor
     */
    public Product() {
    }

    /**
     * Constructor with required fields
     */
    public Product(String name, String barcode, double purchasePrice, double sellingPrice) {
        this.name = name;
        this.barcode = barcode;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = 0;
    }

    /**
     * Constructor with all fields except ID (for DB insert)
     */
    public Product(String name, String category, String barcode, double purchasePrice, double sellingPrice, int quantity) {
        this.name = name;
        this.category = category;
        this.barcode = barcode;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
    }

    /**
     * Full constructor with ID (for DB retrieve)
     */
    public Product(int id, String name, String category, String barcode, double purchasePrice, double sellingPrice, int quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.barcode = barcode;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", barcode='" + barcode + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", sellingPrice=" + sellingPrice +
                ", quantity=" + quantity +
                '}';
    }
}
