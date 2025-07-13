package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private Product productId;
    private int quantity;
    private double price;

    public OrderItem() {}

    public OrderItem(Product productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Product getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
