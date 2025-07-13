package fpt.edu.vn.skincareshop.services;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import fpt.edu.vn.skincareshop.models.AiResponse;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkintypeService {

    public interface SkincareCallback {
        void onSuccess(AiResponse.AiData data);
        void onError(String message);
    }

    public void getAdvice(Context context, String skinText, String userId, final SkincareCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);

        if (userId == null || userId.isEmpty()) {
            callback.onError("Không tìm thấy userId. Vui lòng đăng nhập lại.");
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("skinType", skinText);
        body.put("userId", userId);

        Call<AiResponse> call = apiService.getSkincareAdvice(body);
        call.enqueue(new Callback<AiResponse>() {
            @Override
            public void onResponse(@NonNull Call<AiResponse> call, @NonNull Response<AiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ✅ Lưu skinType vào Shared Preferences
                    SharedPrefManager.getInstance(context).saveSkinType(skinText.trim().toLowerCase());

                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Lỗi phản hồi từ AI");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AiResponse> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Lỗi kết nối đến AI");
            }
        });
    }
}
