package fpt.edu.vn.skincareshop.models;

import com.google.gson.annotations.SerializedName;

public class OrderStatusResponse {
    private InnerData data;

    public InnerData getData() {
        return data;
    }

    public static class InnerData {
        private StatusData data;

        public StatusData getData() {
            return data;
        }
    }

    public static class StatusData {
        private String status;

        public String getStatus() {
            return status;
        }
    }
}


