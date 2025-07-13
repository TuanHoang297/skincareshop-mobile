package fpt.edu.vn.skincareshop.models;

import java.util.List;

public class PaymentRequest {
    private String orderId;                 // dùng khi đã tạo đơn trước
    private String userId;                  // nếu tạo mới đơn ngay khi thanh toán
    private double amount;
    private String paymentMethod;           // "COD" hoặc "VNPAY"
    private List<SimplifiedCartItem> items; // danh sách sản phẩm rút gọn gửi lên server

    public PaymentRequest() {}

    public PaymentRequest(String orderId, String userId, double amount, String paymentMethod, List<SimplifiedCartItem> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.items = items;
    }

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
}
