package fpt.edu.vn.skincareshop.services;

import android.content.Context;
import android.util.Log;

import java.util.List;

import fpt.edu.vn.skincareshop.models.Routine;
import fpt.edu.vn.skincareshop.models.RoutineResponse;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineService {

    public interface RoutineCallback {
        void onSuccess(Routine routine);
        void onError(String message);
    }

    public static void getRoutineBySkinType(Context context, String skinType, RoutineCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        String normalizedSkinType = skinType.trim().toLowerCase();

        apiService.getRoutineBySkinType(normalizedSkinType).enqueue(new Callback<RoutineResponse>() {
            @Override
            public void onResponse(Call<RoutineResponse> call, Response<RoutineResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Routine> routines = response.body().getData();
                    if (!routines.isEmpty()) {
                        callback.onSuccess(routines.get(0)); // hoặc xử lý nhiều routines nếu muốn
                    } else {
                        callback.onError("Không có routine phù hợp");
                    }
                } else {
                    callback.onError("Không tìm thấy routine cho loại da này");
                }
            }

            @Override
            public void onFailure(Call<RoutineResponse> call, Throwable t) {
                Log.e("RoutineService", "getRoutineBySkinType failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

}
