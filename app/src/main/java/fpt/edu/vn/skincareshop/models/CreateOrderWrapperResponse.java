package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;

public class CreateOrderWrapperResponse implements Serializable {
    private boolean success;
    private Order data;

    public boolean isSuccess() {
        return success;
    }

    public Order getData() {
        return data;
    }
}
