package fpt.edu.vn.skincareshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.OrderItem;
import fpt.edu.vn.skincareshop.models.Product;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ItemViewHolder> {

    private Context context;
    private List<OrderItem> items;

    public OrderItemAdapter(Context context, List<OrderItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        OrderItem item = items.get(position);
        Product product = item.getProductId(); // populated từ backend

        String name = product != null ? product.getName() : "Không rõ";
        double price = product != null ? product.getPrice() : item.getPrice(); // fallback
        int quantity = item.getQuantity();
        double total = price * quantity;

        holder.tvName.setText(name);
        holder.tvQty.setText("Số lượng: " + quantity);
        holder.tvPrice.setText("Đơn giá: ₫" + formatCurrency(price));
        holder.tvTotal.setText("Thành tiền: ₫" + formatCurrency(total));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvPrice, tvTotal;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQty = itemView.findViewById(R.id.tvItemQuantity);
            tvPrice = itemView.findViewById(R.id.tvItemPrice);
            tvTotal = itemView.findViewById(R.id.tvItemTotal);
        }
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f", amount);
    }
}
