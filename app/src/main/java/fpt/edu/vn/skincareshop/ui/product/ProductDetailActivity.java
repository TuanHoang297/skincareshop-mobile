package fpt.edu.vn.skincareshop.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.CartItem;
import fpt.edu.vn.skincareshop.models.Product;
import fpt.edu.vn.skincareshop.ui.home.MainActivity;
import fpt.edu.vn.skincareshop.utils.CartManager;
import fpt.edu.vn.skincareshop.utils.FavoriteManager;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct, btnBack, btnCart, btnFavorite;
    private TextView tvName, tvDescription, tvPrice;
    private TextView tvGender, tvVolume, tvRating, tvSkinTypes, tvManufactureDate, tvExpiryDate;
    private Button btnAddToCart, btnGoToCart;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ view
        imgProduct = findViewById(R.id.imgDetailProduct);
        btnBack = findViewById(R.id.btnBack);
        btnCart = findViewById(R.id.btnCart);
        btnFavorite = findViewById(R.id.btnFavorite);

        tvName = findViewById(R.id.tvDetailProductName);
        tvDescription = findViewById(R.id.tvDetailProductDescription);
        tvPrice = findViewById(R.id.tvDetailProductPrice);

        tvGender = findViewById(R.id.tvProductGender);
        tvVolume = findViewById(R.id.tvProductVolume);
        tvRating = findViewById(R.id.tvProductRating);
        tvSkinTypes = findViewById(R.id.tvProductSkinTypes);
        tvManufactureDate = findViewById(R.id.tvProductMfgDate);
        tvExpiryDate = findViewById(R.id.tvProductExpDate);

        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnGoToCart = findViewById(R.id.btnGoToCart);

        // Lấy sản phẩm từ Intent
        product = (Product) getIntent().getSerializableExtra("product");
        if (product == null) {
            Toast.makeText(this, "Không có thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin sản phẩm
        tvName.setText(product.getName());
        tvDescription.setText(product.getDescription());
        tvPrice.setText("₫" + product.getPrice());

        tvGender.setText("Giới tính: " + product.getGender());
        tvVolume.setText("Dung tích: " + product.getVolume());
        tvRating.setText("Đánh giá: " + product.getAverageRating() + " ★");

        List<String> skinTypes = product.getSuitableFor();
        if (skinTypes != null && !skinTypes.isEmpty()) {
            tvSkinTypes.setText("Loại da phù hợp: " + String.join(", ", skinTypes));
        } else {
            tvSkinTypes.setText("Loại da phù hợp: Không rõ");
        }

        tvManufactureDate.setText("Ngày sản xuất: 14/06/2025");
        tvExpiryDate.setText("Hạn sử dụng: 14/06/2027");

        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(imgProduct);

        // Hiển thị trạng thái nút favorite
        updateFavoriteIcon();

        btnFavorite.setOnClickListener(v -> {
            FavoriteManager.toggleFavorite(this, product.getId());
            updateFavoriteIcon();
            Toast.makeText(this,
                    FavoriteManager.isFavorite(this, product.getId()) ? "Đã thêm vào yêu thích" : "Đã xoá khỏi yêu thích",
                    Toast.LENGTH_SHORT).show();
        });

        // Ẩn nút "Đến giỏ hàng" nếu giỏ hàng đang rỗng
        if (CartManager.getCart(this).isEmpty()) {
            btnGoToCart.setVisibility(Button.GONE);
        } else {
            btnGoToCart.setVisibility(Button.VISIBLE);
        }

        // Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            List<CartItem> currentCart = CartManager.getCart(this);
            boolean alreadyInCart = false;

            for (CartItem item : currentCart) {
                if (item.getProduct().getId().equals(product.getId())) {
                    if (item.getQuantity() >= 50) {
                        Toast.makeText(this, "Bạn chỉ có thể mua tối đa 50 sản phẩm này!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    alreadyInCart = true;
                    break;
                }
            }

            CartManager.addToCart(this, product);
            Toast.makeText(this, alreadyInCart ? "Đã tăng số lượng sản phẩm" : "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            btnGoToCart.setVisibility(Button.VISIBLE);
        });

        // Đến giỏ hàng
        btnGoToCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
            intent.putExtra("openCartTab", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        // Giỏ hàng shortcut
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
            intent.putExtra("openCartTab", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void updateFavoriteIcon() {
        if (FavoriteManager.isFavorite(this, product.getId())) {
            btnFavorite.setImageResource(R.drawable.ic_heart_filled);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_heart_outline);
        }
    }
}
