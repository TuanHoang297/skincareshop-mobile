package fpt.edu.vn.skincareshop.models;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private String _id;
    private String name;
    private String imageUrl;
    private String description;
    private double price;
    private String image;
    private String category;

    private double averageRating;
    private int stock;
    private String gender;
    private String volume;
    private List<String> suitableFor;

    // Optional nếu bạn dùng ở backend
    private boolean isDeleted;
    private String deletedAt;
    private String updatedAt;

    // Getters and Setters

    public String getId() { return _id; }
    public void setId(String _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }

    public List<String> getSuitableFor() { return suitableFor; }
    public void setSuitableFor(List<String> suitableFor) { this.suitableFor = suitableFor; }

    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }

    public String getDeletedAt() { return deletedAt; }
    public void setDeletedAt(String deletedAt) { this.deletedAt = deletedAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
