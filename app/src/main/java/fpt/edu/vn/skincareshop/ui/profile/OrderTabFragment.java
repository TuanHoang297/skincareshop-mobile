package fpt.edu.vn.skincareshop.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.OrderAdapter;
import fpt.edu.vn.skincareshop.models.Order;
import fpt.edu.vn.skincareshop.models.OrderResponse;
import fpt.edu.vn.skincareshop.network.ApiClient;
import fpt.edu.vn.skincareshop.network.ApiService;
import fpt.edu.vn.skincareshop.ui.order.OrderDetailActivity;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderTabFragment extends Fragment {

    private static final String ARG_STATUS = "status";
    private String status;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    public static OrderTabFragment newInstance(String status) {
        OrderTabFragment fragment = new OrderTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString(ARG_STATUS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_tab, container, false);

        recyclerView = view.findViewById(R.id.recyclerOrders);
        progressBar = view.findViewById(R.id.progressOrders);
        tvEmpty = view.findViewById(R.id.tvEmptyOrders);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchOrdersByStatus();

        return view;
    }

    private void fetchOrdersByStatus() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        String userId = SharedPrefManager.getInstance(requireContext()).getUserId();
        if (userId == null || userId.isEmpty()) {
            tvEmpty.setText("Không tìm thấy ID người dùng.");
            tvEmpty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        ApiService apiService = ApiClient.getInstance(requireContext()).create(ApiService.class);
        Call<OrderResponse> call = apiService.getOrdersByStatus(status, userId);

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponse> call,
                                   @NonNull Response<OrderResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null &&
                        response.body().getData() != null && !response.body().getData().isEmpty()) {

                    List<Order> orders = response.body().getData();
                    OrderAdapter adapter = new OrderAdapter(
                            requireContext(),
                            orders,
                            order -> {
                                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                                intent.putExtra("orderId", order.getId());
                                startActivity(intent);
                            }
                    );
                    recyclerView.setAdapter(adapter);
                } else {
                    tvEmpty.setText("Không có đơn hàng nào.");
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvEmpty.setText("Lỗi kết nối: " + t.getMessage());
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });

    }
}
