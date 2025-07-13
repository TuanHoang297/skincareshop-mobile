package fpt.edu.vn.skincareshop.ui.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.ProductAdapter;
import fpt.edu.vn.skincareshop.models.AiResponse;
import fpt.edu.vn.skincareshop.services.SkintypeService;
import fpt.edu.vn.skincareshop.ui.product.ProductDetailActivity;
import fpt.edu.vn.skincareshop.utils.SharedPrefManager;

public class SkincareChatPopup extends BottomSheetDialogFragment {

    private static final String TAG = "SkincareDebug";

    private EditText etSkinType;
    private Button btnGetAdvice;
    private ProgressBar progressBar;
    private TextView tvAdvice;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    public SkincareChatPopup() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_skincare_chat_popup, container, false);

        etSkinType = view.findViewById(R.id.etSkinTypeInput);
        btnGetAdvice = view.findViewById(R.id.btnGetAdvice);
        progressBar = view.findViewById(R.id.progressBarAdvice);
        tvAdvice = view.findViewById(R.id.tvAiAdvice);
        recyclerView = view.findViewById(R.id.recyclerAiProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnGetAdvice.setOnClickListener(v -> {
            String userInput = etSkinType.getText().toString().trim();
            if (userInput.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập loại da", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = SharedPrefManager.getInstance(requireContext()).getUserId();
            if (userId == null) {
                Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            tvAdvice.setText("");
            recyclerView.setAdapter(null);

            SkintypeService service = new SkintypeService();
            service.getAdvice(getContext(), userInput, userId, new SkintypeService.SkincareCallback() {
                @Override
                public void onSuccess(AiResponse.AiData data) {
                    progressBar.setVisibility(View.GONE);
                    tvAdvice.setText(data.getReply());

                    // ✅ Lưu skinType chuẩn hóa nếu nhận diện được
                    String normalized = normalizeSkinType(userInput);
                    if (normalized != null) {
                        SharedPrefManager.getInstance(requireContext()).saveSkinType(normalized);
                        Log.d("SkincareDebug", "✅ Saved normalized skinType: " + normalized);
                    } else {
                        Log.d("SkincareDebug", "⚠️ Không thể chuẩn hóa skinType từ input: " + userInput);
                    }

                    productAdapter = new ProductAdapter(getContext(), data.getProducts(), product -> {
                        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                        intent.putExtra("product", product);
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(productAdapter);
                }

                @Override
                public void onError(String message) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });


        return view;
    }

    private String normalizeSkinType(String input) {
        input = input.toLowerCase();

        if (input.contains("dầu") || input.contains("da dầu") || input.contains("dau") || input.contains("da dau")) {
            return "oily";
        } else if (input.contains("khô") || input.contains("da khô") || input.contains("kho") || input.contains("da kho")) {
            return "dry";
        } else if (input.contains("thường") || input.contains("da thường") || input.contains("thuong") || input.contains("da thuong")) {
            return "normal";
        } else if (input.contains("hỗn hợp") || input.contains("da hỗn hợp") || input.contains("hon hop") || input.contains("da hon hop")) {
            return "combination";
        }

        return null;
    }
}
