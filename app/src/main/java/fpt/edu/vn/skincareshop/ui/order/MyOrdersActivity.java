package fpt.edu.vn.skincareshop.ui.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.OrderAdapter;
import fpt.edu.vn.skincareshop.models.Order;
import fpt.edu.vn.skincareshop.services.OrderService;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private ProgressBar progressBar;
    private OrderAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        recyclerOrders = findViewById(R.id.recyclerOrders);
        progressBar = findViewById(R.id.progressOrders);

        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);

        OrderService.getMyOrders(this, new OrderService.OrderListCallback() {
            @Override
            public void onSuccess(List<Order> orders) {
                progressBar.setVisibility(View.GONE);

                adapter = new OrderAdapter(MyOrdersActivity.this, orders, order -> {
                    Intent intent = new Intent(MyOrdersActivity.this, OrderDetailActivity.class);
                    intent.putExtra("orderId", order.getId());
                    startActivity(intent);
                });

                recyclerOrders.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MyOrdersActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
