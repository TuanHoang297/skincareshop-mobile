package fpt.edu.vn.skincareshop.ui.review;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.ReviewAdapter;
import fpt.edu.vn.skincareshop.models.Review;
import fpt.edu.vn.skincareshop.services.RatingService;

public class ViewReviewActivity extends AppCompatActivity {

    private RecyclerView recyclerReviews;
    private ProgressBar progressBar;
    private ImageButton btnWriteReview;

    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review_view);

        recyclerReviews = findViewById(R.id.recyclerReviews);
        progressBar = findViewById(R.id.progressReviewList);
        btnWriteReview = findViewById(R.id.btnWriteReview);

        productId = getIntent().getStringExtra("productId");
        if (productId == null) {
            Toast.makeText(this, "Thiếu thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerReviews.setLayoutManager(new LinearLayoutManager(this));

        loadReviews();

        btnWriteReview.setOnClickListener(v -> {
            Intent intent = new Intent(this, WriteReviewActivity.class);
            intent.putExtra("productId", productId);
            startActivity(intent);
        });
    }

    private void loadReviews() {
        progressBar.setVisibility(View.VISIBLE);

        RatingService.getReviewsByProduct(this, productId, new RatingService.ReviewListCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                progressBar.setVisibility(View.GONE);
                recyclerReviews.setAdapter(new ReviewAdapter(ViewReviewActivity.this, reviews));
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewReviewActivity.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
