package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {
    private int statusCode;
    private String message;
    private List<Order> data;  // ✅ Phải là danh sách

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
