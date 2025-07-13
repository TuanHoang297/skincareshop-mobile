package fpt.edu.vn.skincareshop.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.OrderItemAdapter;
import fpt.edu.vn.skincareshop.models.Order;

public class OrderDetailBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_ORDER = "order";
    private Order order;

    public static OrderDetailBottomSheet newInstance(Order order) {
        OrderDetailBottomSheet fragment = new OrderDetailBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order); // ensure Order implements Serializable
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

        // View init
        TextView tvOrderId = view.findViewById(R.id.tvDetailOrderId);
        TextView tvTotal = view.findViewById(R.id.tvDetailOrderTotal);
        TextView tvStatus = view.findViewById(R.id.tvDetailOrderStatus);
        TextView tvPayment = view.findViewById(R.id.tvDetailPaymentMethod);
        RecyclerView recyclerItems = view.findViewById(R.id.recyclerOrderItems);

        // Defensive programming
        if (order == null) {
            tvOrderId.setText("Không thể hiển thị đơn hàng");
            return;
        }

        // Log for debug
        Log.d("OrderDetailBottomSheet", "Order ID: " + order.getId());
        Log.d("OrderDetailBottomSheet", "Items count: " + (order.getItems() != null ? order.getItems().size() : 0));

        // Set data
        tvOrderId.setText("Mã đơn: " + (order.getId() != null ? order.getId() : "Không rõ"));
        tvTotal.setText("Tổng tiền: ₫" + String.format(Locale.getDefault(), "%.0f", order.getTotal()));
        tvStatus.setText("Trạng thái: " + (order.getStatus() != null ? order.getStatus() : "Không rõ"));
        tvPayment.setText("Thanh toán: " + (order.getPaymentMethod() != null ? order.getPaymentMethod() : "Chưa thanh toán"));

        // Adapter
        recyclerItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerItems.setAdapter(new OrderItemAdapter(requireContext(), order.getItems()));
    }
}
