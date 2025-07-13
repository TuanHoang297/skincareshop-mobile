package fpt.edu.vn.skincareshop.network;

import java.util.List;
import java.util.Map;

import fpt.edu.vn.skincareshop.models.AiResponse;
import fpt.edu.vn.skincareshop.models.CreateOrderResponse;
import fpt.edu.vn.skincareshop.models.MyOrderResponse;
import fpt.edu.vn.skincareshop.models.Order;
import fpt.edu.vn.skincareshop.models.OrderRequest;
import fpt.edu.vn.skincareshop.models.OrderResponse;
import fpt.edu.vn.skincareshop.models.PaymentRequest;
import fpt.edu.vn.skincareshop.models.PaymentResponse;
import fpt.edu.vn.skincareshop.models.Product;
import fpt.edu.vn.skincareshop.models.ProductResponse;
import fpt.edu.vn.skincareshop.models.Review;
import fpt.edu.vn.skincareshop.models.Routine;
import fpt.edu.vn.skincareshop.models.RoutineResponse;
import fpt.edu.vn.skincareshop.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    // ====================== AUTH ======================
    @POST("auth/login")
    Call<User.LoginResponse> login(@Body User.LoginRequest request);

    @POST("auth/register")
    Call<Void> register(@Body User user);

    // ====================== PRODUCT ======================
    @GET("products")
    Call<ProductResponse> getAllProducts();

    @GET("products/{id}/reviews")
    Call<List<Review>> getProductReviews(@Path("id") String productId);

    // ====================== REVIEW ======================
    @POST("reviews/create")
    Call<Void> createReview(@Body Review review);

    // ====================== ROUTINE ======================
    @GET("routines/skintype/{skinType}")
    Call<RoutineResponse> getRoutineBySkinType(@Path("skinType") String skinType);





    // ====================== ORDER ======================
    @POST("orders/create")
    Call<CreateOrderResponse> createOrder(
            @Header("Authorization") String token,
            @Body OrderRequest request
    );





    @GET("orders/my-orders")
    Call<MyOrderResponse> getMyOrders();


    // ✅ Đổi sang kiểu truy vấn ?orderId=... để phù hợp controller mới
    @GET("orders/order-detail")
    Call<Order> getOrderDetail(@Query("orderId") String orderId, @Header("Authorization") String token);


    @PATCH("orders/{id}/cancel")
    Call<Void> cancelOrder(@Path("id") String orderId);

    // ====================== PAYMENT ======================
    @POST("payments/create")
    Call<PaymentResponse> createVnpayPayment(
            @Header("Authorization") String token,
            @Body PaymentRequest request
    );
    @POST("ai/chat")
    Call<AiResponse> getSkincareAdvice(@Body Map<String, String> body);



    @GET("orders/filter")
    Call<OrderResponse> getOrdersByStatus(
            @Query("status") String status,
            @Query("userId") String userId
    );






}
