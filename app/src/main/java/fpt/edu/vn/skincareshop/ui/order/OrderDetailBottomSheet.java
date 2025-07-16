package fpt.edu.vn.skincareshop.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.OrderItemAdapter;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import fpt.edu.vn.skincareshop.models.Order;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_ORDER = "order";
    private Order order;

    public static OrderDetailBottomSheet newInstance(Order order) {
        OrderDetailBottomSheet fragment = new OrderDetailBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable(ARG_ORDER);
        }

        TextView tvOrderId = view.findViewById(R.id.tvDetailOrderId);
        TextView tvTotal = view.findViewById(R.id.tvDetailOrderTotal);
        TextView tvStatus = view.findViewById(R.id.tvDetailOrderStatus);
        TextView tvPayment = view.findViewById(R.id.tvDetailPaymentMethod);
        TextView tvFullName = view.findViewById(R.id.tvDetailOrderFullName);
        TextView tvPhone = view.findViewById(R.id.tvDetailOrderPhone);
        TextView tvAddress = view.findViewById(R.id.tvDetailOrderAddress);
        TextView tvPaymentId = view.findViewById(R.id.tvDetailPaymentId);
        RecyclerView recyclerItems = view.findViewById(R.id.recyclerOrderItems);
        Button btnCancelOrder = view.findViewById(R.id.btnCancelOrder);

        if (order == null) {
            Log.e("OrderDetailBottomSheet", "Order is null");
            tvOrderId.setText("Không thể hiển thị đơn hàng");
            return;
        }

        // Set thông tin đơn hàng
        tvOrderId.setText("Mã đơn: " + (order.getId() != null ? order.getId() : "Không rõ"));
        tvTotal.setText("Tổng tiền: ₫" + String.format(Locale.getDefault(), "%.0f", order.getTotal()));
        tvStatus.setText("Trạng thái: " + order.getStatus());
        tvPayment.setText("Thanh toán: " + (order.getPaymentMethod() != null ? order.getPaymentMethod() : "Chưa thanh toán"));
        tvPaymentId.setText("Mã thanh toán: " + (order.getPaymentId() != null ? order.getPaymentId() : "Chưa thanh toán"));
        tvFullName.setText("Họ tên người nhận: " + order.getFullName());
        tvPhone.setText("Số điện thoại: " + order.getPhoneNumber());
        tvAddress.setText("Địa chỉ nhận hàng: " + order.getAddress());

        recyclerItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerItems.setAdapter(new OrderItemAdapter(requireContext(), order.getItems()));

        // Kiểm tra điều kiện hiển thị nút Hủy/Trả hàng
        boolean canCancel = false;
        String status = order.getStatus();

        if (status.equals("pending")) {
            canCancel = true;
        } else if (status.equals("confirmed") && order.getPaymentId() != null) {
            canCancel = true;
        } else if (status.equals("delivered") && order.getUpdatedAt() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date updatedDate = sdf.parse(order.getUpdatedAt());
                long days7 = 7L * 24 * 60 * 60 * 1000;
                if (System.currentTimeMillis() - updatedDate.getTime() <= days7) {
                    canCancel = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (canCancel) {
            btnCancelOrder.setVisibility(View.VISIBLE);
            btnCancelOrder.setOnClickListener(v -> cancelOrder(order.getId()));
        } else {
            btnCancelOrder.setVisibility(View.GONE);
        }
    }

    private void cancelOrder(String orderId) {
        ApiService apiService = ApiClient.getInstance(requireContext()).create(ApiService.class);

        Call<Order> call = apiService.cancelOrder(orderId);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Đơn hàng đã được xử lý", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(requireContext(), "Không thể hủy đơn hàng", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", "Lỗi huỷ đơn: " + response.code() + " - " + response.message());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
