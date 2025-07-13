package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;

public class Review implements Serializable {
    private String _id;
    private String userId;
    private String userName;
    private String comment;
    private float rating;
    private String productId;
    private String createdAt;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName != null ? userName : "Ẩn danh";
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment != null ? comment : "";
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCreatedAt() {
        return createdAt != null ? createdAt.split("T")[0] : "";
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
