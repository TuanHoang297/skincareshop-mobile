package fpt.edu.vn.skincareshop.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.CartItemAdapter;
import fpt.edu.vn.skincareshop.models.CartItem;
import fpt.edu.vn.skincareshop.ui.payment.PaymentActivity;
import fpt.edu.vn.skincareshop.utils.CartManager;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotal;
    private Button btnCheckout;
    private List<CartItem> cartItems;
    private CartItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerCart);
        tvTotal = findViewById(R.id.tvCartTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        cartItems = CartManager.getCart(this);

        adapter = new CartItemAdapter(this, cartItems, new CartItemAdapter.CartItemListener() {
            @Override
            public void onQuantityChanged(String productId, int newQuantity) {
                CartManager.updateQuantity(CartActivity.this, productId, newQuantity);
                updateTotal();
            }

            @Override
            public void onItemRemoved(String productId, int position) {
                CartManager.removeItemFromCart(CartActivity.this, productId);
                cartItems.remove(position);
                adapter.notifyItemRemoved(position);
                updateTotal();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        updateTotal();

        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("cartItems", new ArrayList<>(cartItems));
            intent.putExtra("totalAmount", calculateTotal());
            startActivity(intent);
        });
    }

    private void updateTotal() {
        double total = calculateTotal();
        tvTotal.setText("Tổng tiền: ₫" + total);
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
}
