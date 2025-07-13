package fpt.edu.vn.skincareshop.models;

import java.util.List;

public class RoutineResponse {
    private int statusCode;
    private String message;
    private List<Routine> data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Routine> getData() {
        return data;
    }
}
