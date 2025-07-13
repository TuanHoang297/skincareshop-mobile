package fpt.edu.vn.skincareshop.ui.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.ProductAdapter;
import fpt.edu.vn.skincareshop.models.AiResponse;
import fpt.edu.vn.skincareshop.models.Product;
import fpt.edu.vn.skincareshop.services.SkintypeService;

import android.content.Intent;

import fpt.edu.vn.skincareshop.ui.product.ProductDetailActivity;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class ActivitySkincareAdvice extends AppCompatActivity {

    private EditText etSkinType;
    private Button btnGetAdvice;
    private ProgressBar progressBar;
    private TextView tvAdvice;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skincare_advice);

        etSkinType = findViewById(R.id.etSkinTypeInput);
        btnGetAdvice = findViewById(R.id.btnGetAdvice);
        progressBar = findViewById(R.id.progressBarAdvice);
        tvAdvice = findViewById(R.id.tvAiAdvice);
        recyclerView = findViewById(R.id.recyclerAiProducts);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnGetAdvice.setOnClickListener(v -> {
            String skinInput = etSkinType.getText().toString().trim();
            if (skinInput.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập loại da", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Lấy userId từ SharedPreferences
            String userId = SharedPrefManager.getInstance(this).getUserId();
            if (userId == null || userId.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            tvAdvice.setText("");
            recyclerView.setAdapter(null);

            SkintypeService service = new SkintypeService();
            service.getAdvice(this, skinInput, userId, new SkintypeService.SkincareCallback() {
                @Override
                public void onSuccess(AiResponse.AiData data) {
                    progressBar.setVisibility(View.GONE);
                    tvAdvice.setText(data.getReply());

                    products = data.getProducts();
                    productAdapter = new ProductAdapter(ActivitySkincareAdvice.this, products, product -> {
                        Intent intent = new Intent(ActivitySkincareAdvice.this, ProductDetailActivity.class);
                        intent.putExtra("product", product);
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(productAdapter);
                }

                @Override
                public void onError(String message) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ActivitySkincareAdvice.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}
