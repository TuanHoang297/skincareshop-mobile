package fpt.edu.vn.skincareshop.services;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fpt.edu.vn.skincareshop.models.Product;
import fpt.edu.vn.skincareshop.models.ProductResponse;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductService {

    // ✅ Interface callback chung cho mọi nơi dùng
    public interface ProductCallback {
        void onSuccess(List<Product> productList);
        void onError(String errorMessage);
    }

    // ✅ Lấy tất cả sản phẩm
    public static void fetchAllProducts(Context context, ProductCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        Call<ProductResponse> call = apiService.getAllProducts();

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    Log.e("ProductService", "Response error: " + response.code());
                    callback.onError("Không thể lấy danh sách sản phẩm");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("ProductService", "Network error: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
