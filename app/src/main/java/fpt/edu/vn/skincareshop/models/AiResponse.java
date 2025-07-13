package fpt.edu.vn.skincareshop.models;

import java.util.List;

public class AiResponse {
    private int statusCode;
    private String message;
    private AiData data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public AiData getData() {
        return data;
    }

    public static class AiData {
        private String reply;
        private List<Product> products;

        public String getReply() {
            return reply;
        }

        public List<Product> getProducts() {
            return products;
        }
    }
}
