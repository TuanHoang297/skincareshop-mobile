package fpt.edu.vn.skincareshop.services;

import android.content.Context;
import android.util.Log;

import fpt.edu.vn.skincareshop.models.Order;
import fpt.edu.vn.skincareshop.models.OrderStatusResponse;
import fpt.edu.vn.skincareshop.models.PaymentRequest;
import fpt.edu.vn.skincareshop.models.PaymentResponse;
import fpt.edu.vn.skincareshop.models.VnpayReturnResult;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentService {

    public interface PaymentCallback {
        void onSuccess(PaymentResponse response);
        void onError(String message);
    }

    public interface OrderStatusCallback {
        void onResult(String status);
        void onError(String message);
    }

    public static void createVnpayPayment(Context context, PaymentRequest request, PaymentCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        String token = SharedPrefManager.getInstance(context).getToken();
        apiService.createVnpayPayment("Bearer " + token, request).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Không thể tạo liên kết thanh toán: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.e("PaymentService", "createVnpayPayment failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });

    }

    // ✅ API check trạng thái đơn hàng sau thanh toán
    public static void checkOrderStatus(Context context, String orderId, OrderStatusCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        apiService.getOrderStatus(orderId).enqueue(new Callback<OrderStatusResponse>() {
            @Override
            public void onResponse(Call<OrderStatusResponse> call, Response<OrderStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    String status = response.body().getData().getData().getStatus();

                    callback.onResult(status);
                } else {
                    callback.onError("Không thể lấy trạng thái đơn hàng");
                }
            }

            @Override
            public void onFailure(Call<OrderStatusResponse> call, Throwable t) {
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }




    // ❌ Không còn dùng nữa nếu dùng cách 2, có thể xóa nếu không cần
    public interface VnpayResultCallback {
        void onResult(VnpayReturnResult result);
        void onError(String message);
    }
}
