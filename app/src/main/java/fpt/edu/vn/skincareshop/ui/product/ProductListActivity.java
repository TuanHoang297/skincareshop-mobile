package fpt.edu.vn.skincareshop.ui.product;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.ProductAdapter;
import fpt.edu.vn.skincareshop.models.Product;
import fpt.edu.vn.skincareshop.services.ProductService;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerProducts;
    private ProgressBar progressBar;
    private ProductAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerProducts = findViewById(R.id.recyclerProducts);
        progressBar = findViewById(R.id.progressProducts);

        recyclerProducts.setLayoutManager(new GridLayoutManager(this, 2));

        loadProducts();
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);

        ProductService.fetchAllProducts(this, new ProductService.ProductCallback() {
            @Override
            public void onSuccess(List<Product> productList) {
                progressBar.setVisibility(View.GONE);

                adapter = new ProductAdapter(ProductListActivity.this, productList, product -> {
                    Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                    intent.putExtra("product", product); // ✅ FIX: truyền kiểu Serializable
                    startActivity(intent);
                });

                recyclerProducts.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductListActivity.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
