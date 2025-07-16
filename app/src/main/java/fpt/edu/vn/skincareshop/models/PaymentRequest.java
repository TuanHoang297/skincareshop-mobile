package fpt.edu.vn.skincareshop.models;

import java.util.List;

public class PaymentRequest {
    private String orderId;
    private String userId;
    private String fullName;
    private double amount;
    private String paymentMethod;
    private List<SimplifiedCartItem> items;

    private String phoneNumber; // ✅ thêm mới
    private String address;     // ✅ thêm mới
    private String email;       // ✅ thêm mới

    public PaymentRequest() {}

    public PaymentRequest(String orderId, String userId, double amount, String paymentMethod,
                          List<SimplifiedCartItem> items, String phoneNumber, String address, String email, String fullName ) {
        this.orderId = orderId;
        this.userId = userId;
        this.fullName = fullName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.items = items;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getters & Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<SimplifiedCartItem> getItems() {
        return items;
    }

    public void setItems(List<SimplifiedCartItem> items) {
        this.items = items;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
