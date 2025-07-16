package fpt.edu.vn.skincareshop.ui.order;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.OrderItemAdapter;
import fpt.edu.vn.skincareshop.models.Order;
import fpt.edu.vn.skincareshop.models.OrderItem;
import fpt.edu.vn.skincareshop.services.OrderService;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvOrderDate, tvTotal, tvStatus, tvPayment;
    private RecyclerView recyclerItems;
    private ProgressBar progressBar;
    private Button btnCancel;

    private String orderId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvOrderId = findViewById(R.id.tvDetailOrderId);
        tvOrderDate = findViewById(R.id.tvDetailOrderDate);
        tvTotal = findViewById(R.id.tvDetailOrderTotal);
        tvStatus = findViewById(R.id.tvDetailOrderStatus);
        tvPayment = findViewById(R.id.tvDetailPaymentMethod);
        recyclerItems = findViewById(R.id.recyclerOrderItems);
        progressBar = findViewById(R.id.progressOrderDetail);
        btnCancel = findViewById(R.id.btnCancelOrder);

        recyclerItems.setLayoutManager(new LinearLayoutManager(this));

        orderId = getIntent().getStringExtra("orderId");

        loadOrderDetail();
    }

    private void loadOrderDetail() {
        progressBar.setVisibility(View.VISIBLE);

        OrderService.getOrderDetail(this, orderId, new OrderService.OrderDetailCallback() {
            @Override
            public void onSuccess(Order order) {
                progressBar.setVisibility(View.GONE);

                List<OrderItem> items = order.getItems();

                tvOrderId.setText("Mã đơn: " + order.getId());
                tvOrderDate.setText("Ngày: " + order.getCreatedAt());
                tvTotal.setText("Tổng tiền: ₫" + String.format("%,.0f", order.getTotal()));
                tvStatus.setText("Trạng thái: " + order.getStatus());
                tvPayment.setText("Thanh toán: " + order.getPaymentMethod());

                recyclerItems.setAdapter(new OrderItemAdapter(OrderDetailActivity.this, items));


            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(OrderDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
