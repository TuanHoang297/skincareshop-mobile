package fpt.edu.vn.skincareshop.ui.payment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.CartItem;
import fpt.edu.vn.skincareshop.models.OrderItem;
import fpt.edu.vn.skincareshop.models.OrderRequest;
import fpt.edu.vn.skincareshop.models.PaymentRequest;
import fpt.edu.vn.skincareshop.models.PaymentResponse;
import fpt.edu.vn.skincareshop.models.SimplifiedCartItem;
import fpt.edu.vn.skincareshop.services.OrderService;
import fpt.edu.vn.skincareshop.services.PaymentService;
import fpt.edu.vn.skincareshop.utils.CartManager;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class PaymentActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton rbCOD, rbVnpay;
    private Button btnSubmit;
    private EditText edtName, edtPhone, edtAddress;

    private List<CartItem> cartItems;
    private double totalAmount;

    private ProgressDialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        radioGroup = findViewById(R.id.radioGroupMethod);
        rbCOD = findViewById(R.id.rbCOD);
        rbVnpay = findViewById(R.id.rbVnpay);
        btnSubmit = findViewById(R.id.btnSubmitPayment);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang xử lý...");
        dialog.setCancelable(false);

        cartItems = (List<CartItem>) getIntent().getSerializableExtra("cartItems");
        totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);

        btnSubmit.setOnClickListener(v -> {
            if (!validateUserInput()) return;

            if (rbCOD.isChecked()) {
                handleCOD();
            } else if (rbVnpay.isChecked()) {
                handleVnpay();
            } else {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateUserInput() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin giao hàng", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void handleCOD() {
        dialog.show();

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct() != null) {
                Log.d("ORDER_DEBUG", "Item - ProductId: " + cartItem.getProduct().getId()
                        + ", Quantity: " + cartItem.getQuantity()
                        + ", Price: " + cartItem.getProduct().getPrice());

                orderItems.add(new OrderItem(
                        cartItem.getProduct(), // ✅ truyền nguyên Product
                        cartItem.getQuantity(),
                        cartItem.getProduct().getPrice()
                ));

            }
        }

        String userId = SharedPrefManager.getInstance(this).getUserId();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(userId);
        orderRequest.setPhoneNumber(phone);
        orderRequest.setAddress(address);
        orderRequest.setItems(orderItems);
        orderRequest.setTotalAmount(totalAmount);

        Log.d("ORDER_DEBUG", "Creating order with userId: " + userId);
        Log.d("ORDER_DEBUG", "Phone: " + phone + ", Address: " + address + ", Total: " + totalAmount);
        Log.d("ORDER_DEBUG", "Total items: " + orderItems.size());

        // In ra JSON payload gửi đi
        Log.d("ORDER_JSON", new Gson().toJson(orderRequest));

        OrderService.createOrder(this, orderRequest, new OrderService.OrderActionCallback() {
            @Override
            public void onSuccess() {
                dialog.dismiss();
                CartManager.clearCart(PaymentActivity.this);
                Toast.makeText(PaymentActivity.this, "Đặt hàng thành công (COD)", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                dialog.dismiss();
                Log.e("ORDER_DEBUG", "Create order failed: " + errorMessage);
                Toast.makeText(PaymentActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleVnpay() {
        dialog.show();

        PaymentRequest request = new PaymentRequest();
        request.setUserId(SharedPrefManager.getInstance(this).getUserId());
        request.setAmount(totalAmount);
        request.setPaymentMethod("VNPAY");

        List<SimplifiedCartItem> simpleItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct() != null) {
                simpleItems.add(new SimplifiedCartItem(
                        cartItem.getProduct().getId(),
                        cartItem.getQuantity(),
                        cartItem.getProduct().getPrice()
                ));
            }
        }
        request.setItems(simpleItems);

        PaymentService.createVnpayPayment(this, request, new PaymentService.PaymentCallback() {
            @Override
            public void onSuccess(PaymentResponse response) {
                dialog.dismiss();

                String url = response.getPaymentUrl();
                String orderId = response.getOrderId();

                if ((orderId == null || orderId.isEmpty()) && url != null && url.contains("vnp_TxnRef=")) {
                    int start = url.indexOf("vnp_TxnRef=") + 11;
                    int end = url.indexOf("&", start);
                    orderId = end == -1 ? url.substring(start) : url.substring(start, end);
                }

                if (orderId != null && !orderId.isEmpty()) {
                    SharedPrefManager.getInstance(PaymentActivity.this).saveLatestOrderId(orderId);
                }

                if (url != null && !url.isEmpty()) {
                    Intent intent = new Intent(PaymentActivity.this, PaymentWebViewActivity.class);
                    intent.putExtra("paymentUrl", url);
                    startActivity(intent);
                } else {
                    Toast.makeText(PaymentActivity.this, "Không nhận được dữ liệu thanh toán", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                dialog.dismiss();
                Log.e("VNPAY_DEBUG", "Create payment failed: " + errorMessage);
                Toast.makeText(PaymentActivity.this, "Lỗi VNPAY: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
