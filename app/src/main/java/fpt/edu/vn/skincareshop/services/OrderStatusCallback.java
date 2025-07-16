package fpt.edu.vn.skincareshop.services;

public interface OrderStatusCallback {
    void onResult(String status);      // Khi nhận được trạng thái đơn hàng (ví dụ: "confirmed", "pending", ...)
    void onError(String message);      // Khi xảy ra lỗi (API lỗi, mạng lỗi, parsing lỗi, ...)
}
