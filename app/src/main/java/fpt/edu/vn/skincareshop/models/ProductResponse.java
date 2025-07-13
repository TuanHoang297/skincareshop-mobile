package fpt.edu.vn.skincareshop.models;

import java.util.List;

public class ProductResponse {
    private String author;
    private int statusCode;
    private String message;
    private List<Product> data;

    public String getAuthor() { return author; }
    public int getStatusCode() { return statusCode; }
    public String getMessage() { return message; }
    public List<Product> getData() { return data; }

    public void setAuthor(String author) { this.author = author; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public void setMessage(String message) { this.message = message; }
    public void setData(List<Product> data) { this.data = data; }
}
