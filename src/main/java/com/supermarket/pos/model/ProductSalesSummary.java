package com.supermarket.pos.model;

/**
 * ProductSalesSummary represents a product name with quantity sold.
 */
public class ProductSalesSummary {

    private final String productName;
    private final int quantitySold;

    public ProductSalesSummary(String productName, int quantitySold) {
        this.productName = productName;
        this.quantitySold = quantitySold;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }
}
