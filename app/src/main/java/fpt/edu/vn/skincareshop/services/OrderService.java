package fpt.edu.vn.skincareshop.services;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fpt.edu.vn.skincareshop.models.CreateOrderResponse;
import fpt.edu.vn.skincareshop.models.MyOrderResponse;
import fpt.edu.vn.skincareshop.models.Order;
import fpt.edu.vn.skincareshop.models.OrderRequest;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {

    public interface OrderListCallback {
        void onSuccess(List<Order> orders);
        void onError(String message);
    }

    public interface OrderDetailCallback {
        void onSuccess(Order order);
        void onError(String message);
    }

    public interface OrderActionCallback {
        void onSuccess();
        void onError(String message);
    }

    // ✅ Sửa lại: dùng CreateOrderResponse thay vì Void
    public static void createOrder(Context context, OrderRequest orderRequest, OrderActionCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        String token = SharedPrefManager.getInstance(context).getToken();

        // 🟨 Log token
        Log.d("ORDER_TOKEN", "Bearer " + token);

        // 🟨 Log request JSON
        try {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            String json = gson.toJson(orderRequest);
            Log.d("ORDER_JSON", json);
        } catch (Exception e) {
            Log.e("ORDER_JSON", "Lỗi khi convert JSON", e);
        }

        apiService.createOrder("Bearer " + token, orderRequest).enqueue(new Callback<CreateOrderResponse>() {
            @Override
            public void onResponse(Call<CreateOrderResponse> call, Response<CreateOrderResponse> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getData() != null
                        && response.body().getData().isSuccess()) {

                    callback.onSuccess();
                } else {
                    String error = "Không thể tạo đơn hàng";
                    if (response.errorBody() != null) {
                        try {
                            error = response.errorBody().string();
                        } catch (Exception ignored) {}
                    }
                    callback.onError(error);
                }
            }


            @Override
            public void onFailure(Call<CreateOrderResponse> call, Throwable t) {
                Log.e("ORDER_DEBUG", "createOrder failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }


    public static void getMyOrders(Context context, OrderListCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        apiService.getMyOrders().enqueue(new Callback<MyOrderResponse>() {
            @Override
            public void onResponse(Call<MyOrderResponse> call, Response<MyOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Không thể lấy danh sách đơn hàng");
                }
            }

            @Override
            public void onFailure(Call<MyOrderResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public static void getOrderDetail(Context context, String orderId, OrderDetailCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        String token = SharedPrefManager.getInstance(context).getToken();

        apiService.getOrderDetail(orderId, "Bearer " + token)
                .enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Không thể lấy chi tiết đơn hàng");
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        callback.onError("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }


}
