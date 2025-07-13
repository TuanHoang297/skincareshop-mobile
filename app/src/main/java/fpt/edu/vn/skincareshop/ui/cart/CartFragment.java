package fpt.edu.vn.skincareshop.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.CartItemAdapter;
import fpt.edu.vn.skincareshop.models.CartItem;
import fpt.edu.vn.skincareshop.ui.payment.PaymentActivity;
import fpt.edu.vn.skincareshop.utils.CartManager;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvTotal;
    private Button btnCheckout;
    private Button btnClearCart;
    private List<CartItem> cartItems;
    private CartItemAdapter adapter;

    public CartFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerCart);
        tvTotal = view.findViewById(R.id.tvCartTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        btnClearCart = view.findViewById(R.id.btnClearCart); // ✅ ánh xạ nút xoá tất cả

        cartItems = CartManager.getCart(requireContext());

        adapter = new CartItemAdapter(requireContext(), cartItems, new CartItemAdapter.CartItemListener() {
            @Override
            public void onQuantityChanged(String productId, int newQuantity) {
                CartManager.updateQuantity(requireContext(), productId, newQuantity);
                updateTotal();
            }

            @Override
            public void onItemRemoved(String productId, int position) {
                CartManager.removeItemFromCart(requireContext(), productId);
                cartItems.remove(position);
                adapter.notifyItemRemoved(position);
                updateTotal();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PaymentActivity.class);
            intent.putExtra("cartItems", new ArrayList<>(cartItems));
            intent.putExtra("totalAmount", calculateTotal());
            startActivity(intent);
        });

        btnClearCart.setOnClickListener(v -> {
            CartManager.clearCart(requireContext());
            cartItems.clear();
            adapter.notifyDataSetChanged();
            updateTotal();
        });

        updateTotal();

        return view;
    }

    private void updateTotal() {
        double total = calculateTotal();
        tvTotal.setText("Tổng tiền: ₫" + total);

        boolean isEmpty = cartItems == null || cartItems.isEmpty();

        btnCheckout.setEnabled(!isEmpty);
        btnCheckout.setAlpha(isEmpty ? 0.5f : 1.0f);

        btnClearCart.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            double itemTotal = item.getTotalPrice();
            Log.d("CartItemDebug", "Sản phẩm: " + item.getProduct().getName() + ", SL: " + item.getQuantity() + ", Giá: " + item.getProduct().getPrice() + ", Tổng: " + itemTotal);
            total += itemTotal;
        }
        return total;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật giỏ hàng khi trở lại Fragment
        cartItems.clear();
        cartItems.addAll(CartManager.getCart(requireContext()));
        adapter.notifyDataSetChanged();
        updateTotal();
    }
}
