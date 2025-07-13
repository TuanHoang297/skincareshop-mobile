package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;

public class CreateOrderResponse implements Serializable {
    private int statusCode;
    private String message;
    private CreateOrderWrapperResponse data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public CreateOrderWrapperResponse getData() {
        return data;
    }
}
