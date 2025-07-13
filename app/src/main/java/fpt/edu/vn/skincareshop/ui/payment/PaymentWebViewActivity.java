package fpt.edu.vn.skincareshop.ui.payment;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.services.PaymentService;
import fpt.edu.vn.skincareshop.utils.CartManager;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class PaymentWebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_webview);

        webView = findViewById(R.id.webViewVnpay);
        webView.getSettings().setJavaScriptEnabled(true);

        String paymentUrl = getIntent().getStringExtra("paymentUrl");

        if (paymentUrl != null) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    if (url.contains("/vnpay-return")) {
                        String orderId = SharedPrefManager.getInstance(PaymentWebViewActivity.this).getLatestOrderId();

                        if (orderId != null) {
                            PaymentService.checkOrderStatus(PaymentWebViewActivity.this, orderId, new PaymentService.OrderStatusCallback() {
                                @Override
                                public void onResult(String status) {
                                    if ("PAID".equalsIgnoreCase(status)) {
                                        CartManager.clearCart(PaymentWebViewActivity.this);

                                        Toast.makeText(PaymentWebViewActivity.this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(PaymentWebViewActivity.this, "Thanh toán chưa hoàn tất hoặc thất bại", Toast.LENGTH_LONG).show();
                                    }
                                    finish();
                                }

                                @Override
                                public void onError(String message) {
                                    Toast.makeText(PaymentWebViewActivity.this, "Lỗi kiểm tra đơn hàng: " + message, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(PaymentWebViewActivity.this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }
            });

            webView.loadUrl(paymentUrl);
        } else {
            Toast.makeText(this, "Không có đường dẫn thanh toán", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
