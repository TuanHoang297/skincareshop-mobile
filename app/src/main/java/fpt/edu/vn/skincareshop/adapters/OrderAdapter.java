package fpt.edu.vn.skincareshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.Order;
import fpt.edu.vn.skincareshop.ui.order.OrderDetailBottomSheet;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onClick(Order order);
    }

    private Context context;
    private List<Order> orders;
    private OnOrderClickListener listener;

    public OrderAdapter(Context context, List<Order> orders, OnOrderClickListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Mã đơn
        holder.tvOrderId.setText("Mã đơn: " + (order.getId() != null ? order.getId() : "Không rõ"));

        // Ẩn ngày tạo
        holder.tvOrderDate.setVisibility(View.GONE);

        // Tổng tiền
        holder.tvTotal.setText("Tổng tiền: ₫" + String.format("%.0f", order.getTotal()));

        // Trạng thái
        holder.tvStatus.setText("Trạng thái: " + (order.getStatus() != null ? order.getStatus() : "Không rõ"));

        // Click
        holder.itemView.setOnClickListener(v -> {
            OrderDetailBottomSheet bottomSheet = OrderDetailBottomSheet.newInstance(order);
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), "OrderDetail");
        });

    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvTotal, tvStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
