package fpt.edu.vn.skincareshop.ui.review;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.Review;
import fpt.edu.vn.skincareshop.services.RatingService;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class WriteReviewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText edtComment;
    private Button btnSubmitReview;

    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_review_write);

        ratingBar = findViewById(R.id.ratingBarWrite);
        edtComment = findViewById(R.id.edtReviewComment);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);

        productId = getIntent().getStringExtra("productId");

        btnSubmitReview.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = edtComment.getText().toString().trim();

            if (rating == 0 || comment.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Review review = new Review();
            review.setProductId(productId);
            review.setUserId(SharedPrefManager.getInstance(this).getUserId());
            review.setUserName(SharedPrefManager.getInstance(this).getUsername());
            review.setRating(rating);
            review.setComment(comment);

            RatingService.createReview(this, review, new RatingService.ReviewCreateCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(WriteReviewActivity.this, "Đã gửi đánh giá", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(WriteReviewActivity.this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
