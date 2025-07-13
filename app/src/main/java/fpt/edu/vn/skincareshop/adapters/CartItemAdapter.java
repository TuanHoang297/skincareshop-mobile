package fpt.edu.vn.skincareshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.CartItem;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(String productId, int newQuantity);
        void onItemRemoved(String productId, int position);
    }

    public CartItemAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvName.setText(item.getProduct().getName());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvPrice.setText("₫" + item.getProduct().getPrice());

        Glide.with(context)
                .load(item.getProduct().getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.imgProduct);

        // ✅ Tăng số lượng (giới hạn 50)
        holder.btnIncrease.setOnClickListener(v -> {
            int quantity = item.getQuantity();
            if (quantity >= 50) {
                Toast.makeText(context, "Tối đa 50 sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }
            quantity++;
            item.setQuantity(quantity);
            notifyItemChanged(position);
            listener.onQuantityChanged(item.getProduct().getId(), quantity);
        });

        // ✅ Giảm số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            int quantity = item.getQuantity();
            if (quantity > 1) {
                quantity--;
                item.setQuantity(quantity);
                notifyItemChanged(position);
                listener.onQuantityChanged(item.getProduct().getId(), quantity);
            }
        });

        // ✅ Xóa sản phẩm
        holder.btnDelete.setOnClickListener(v -> {
            listener.onItemRemoved(item.getProduct().getId(), position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice;
        ImageView imgProduct;
        ImageButton btnIncrease, btnDecrease, btnDelete;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
