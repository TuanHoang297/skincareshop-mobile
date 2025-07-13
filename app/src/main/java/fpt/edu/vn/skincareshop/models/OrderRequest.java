package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;
import java.util.List;

public class OrderRequest implements Serializable {

    private String userId;         // ✅ THÊM DÒNG NÀY
    private String phoneNumber;
    private String address;
    private List<OrderItem> items;
    private double totalAmount;

    public OrderRequest() {}

    public OrderRequest(String userId, String phoneNumber, String address, List<OrderItem> items, double totalAmount) {
        this.userId = userId;     // ✅ THÊM DÒNG NÀY
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    // ✅ Getter & Setter cho userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
