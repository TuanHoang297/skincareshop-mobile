package fpt.edu.vn.skincareshop.services;

import android.content.Context;
import android.util.Log;

import fpt.edu.vn.skincareshop.models.User;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;
public class AuthService {

    public interface LoginCallback {
        void onSuccess(User.LoginResponse response);
        void onError(String message);
    }

    public interface RegisterCallback {
        void onSuccess();
        void onError(String message);
    }

    public static void login(Context context, User.LoginRequest request, LoginCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        apiService.login(request).enqueue(new Callback<User.LoginResponse>() {
            @Override
            public void onResponse(Call<User.LoginResponse> call, Response<User.LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Đăng nhập thất bại");
                }
            }

            @Override
            public void onFailure(Call<User.LoginResponse> call, Throwable t) {
                Log.e("AuthService", "login failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public static void register(Context context, User user, RegisterCallback callback) {
        ApiService apiService = ApiClient.getInstance(context).create(ApiService.class);
        apiService.register(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject json = new JSONObject(errorBody);
                        String message = json.optString("message", "Đăng ký thất bại");
                        callback.onError(message);
                    } catch (Exception e) {
                        callback.onError("Đăng ký thất bại");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AuthService", "register failed", t);
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
