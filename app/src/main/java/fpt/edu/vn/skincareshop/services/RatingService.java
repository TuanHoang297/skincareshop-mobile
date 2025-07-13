package fpt.edu.vn.skincareshop.services;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fpt.edu.vn.skincareshop.models.Review;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingService {

    public interface ReviewListCallback {
        void onSuccess(List<Review> reviews);
        void onError(String message);
    }

    public interface ReviewCreateCallback {
        void onSuccess();
        void onError(String message);
    }

    public static void getReviewsByProduct(Context context, String productId, ReviewListCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        apiService.getProductReviews(productId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Không thể tải đánh giá");
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Log.e("RatingService", "getReviewsByProduct failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public static void createReview(Context context, Review review, ReviewCreateCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        apiService.createReview(review).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Không thể gửi đánh giá");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RatingService", "createReview failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
