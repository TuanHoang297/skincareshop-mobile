package fpt.edu.vn.skincareshop.models;

import java.util.List;

public class MyOrderResponse {
    private String status;
    private List<Order> data;

    public String getStatus() { return status; }
    public List<Order> getData() { return data; }
}
