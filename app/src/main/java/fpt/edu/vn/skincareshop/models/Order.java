package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String _id;
    private String userId;
    private List<OrderItem> items;

    private double totalAmount;
    private String status;
    private String paymentMethod;
    private String createdAt;

    public Order() {}

    public Order(String userId,  List<OrderItem> items, double totalAmount, String paymentMethod) {
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }


    public double getTotal() {
        return totalAmount;
    }

    public void setTotal(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status != null ? status : "Chưa xác định";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreatedAt() {
        return createdAt != null ? createdAt.split("T")[0] : "";
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
