package fpt.edu.vn.skincareshop.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.services.PaymentService;
import fpt.edu.vn.skincareshop.ui.home.MainActivity;
import fpt.edu.vn.skincareshop.utils.CartManager;

public class PaymentWebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_webview);

        webView = findViewById(R.id.webViewVnpay);
        webView.getSettings().setJavaScriptEnabled(true);

        String paymentUrl = getIntent().getStringExtra("paymentUrl");

        if (paymentUrl != null && !paymentUrl.isEmpty()) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    if (url.contains("/vnpay-return")) {
                        Log.d("WEBVIEW_DEBUG", "Page loaded: " + url);

                        // ✅ Delay 500ms để DOM chắc chắn đã render xong
                        new Handler().postDelayed(() -> {
                            view.evaluateJavascript(
                                    "(function() { return document.getElementById('orderId')?.innerText; })();",
                                    value -> {
                                        if (value != null && !value.equals("null")) {
                                            String orderId = value.replaceAll("\"", "");
                                            Log.d("WEBVIEW_DEBUG", "Found orderId: " + orderId);

                                            PaymentService.checkOrderStatus(PaymentWebViewActivity.this, orderId, new PaymentService.OrderStatusCallback() {
                                                @Override
                                                public void onResult(String status) {
                                                    if ("confirmed".equalsIgnoreCase(status)) {
                                                        CartManager.clearCart(PaymentWebViewActivity.this);
                                                        Toast.makeText(PaymentWebViewActivity.this, "Thanh toán thành công!", Toast.LENGTH_LONG).show();

                                                        Intent intent = new Intent(PaymentWebViewActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        intent.putExtra("openProfileTab", true);
                                                        startActivity(intent);
                                                    } else {
                                                        Toast.makeText(PaymentWebViewActivity.this, "Thanh toán chưa hoàn tất", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(PaymentWebViewActivity.this, "Không tìm thấy mã đơn hàng", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                            );
                        }, 000); // ⏱ Delay 500ms
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
