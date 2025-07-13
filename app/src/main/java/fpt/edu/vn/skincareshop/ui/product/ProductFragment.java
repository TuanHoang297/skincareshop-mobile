package fpt.edu.vn.skincareshop.ui.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.adapters.ProductAdapter;
import fpt.edu.vn.skincareshop.adapters.ProductGroupAdapter;
import fpt.edu.vn.skincareshop.models.Product;
import fpt.edu.vn.skincareshop.services.ProductService;
import fpt.edu.vn.skincareshop.ui.components.SkincareChatPopup;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerProducts;
    private ProgressBar progressBar;
    private FloatingActionButton fabSkincareChat;
    private EditText edtSearch;
    private LinearLayout layoutFilter;

    private Context context;
    private List<Product> allProducts = new ArrayList<>();
    private List<String> groupOrder = new ArrayList<>();
    private ConcatAdapter concatAdapter;

    public ProductFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        context = getContext();
        recyclerProducts = view.findViewById(R.id.recyclerProducts);
        progressBar = view.findViewById(R.id.progressProducts);
        fabSkincareChat = view.findViewById(R.id.fabSkincareChat);
        edtSearch = view.findViewById(R.id.edtSearch);
        layoutFilter = view.findViewById(R.id.layoutFilter);

        fabSkincareChat.setOnClickListener(v -> {
            SkincareChatPopup popup = new SkincareChatPopup();
            popup.show(getParentFragmentManager(), "SkincareChatPopup");
        });

        recyclerProducts.setLayoutManager(new LinearLayoutManager(context));

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAndRenderProducts(s.toString());
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        loadProducts();
        return view;
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);

        ProductService.fetchAllProducts(context, new ProductService.ProductCallback() {
            @Override
            public void onSuccess(List<Product> productList) {
                progressBar.setVisibility(View.GONE);
                allProducts = productList;
                filterAndRenderProducts("");
                renderFilterButtons();
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterAndRenderProducts(String keyword) {
        Map<String, List<Product>> filtered = new LinkedHashMap<>();
        groupOrder.clear();

        String keywordNoAccent = removeAccents(keyword);

        for (Product product : allProducts) {
            String productName = product.getName();
            if (removeAccents(productName).contains(keywordNoAccent)) {
                String category = classifyProduct(productName);
                if (!filtered.containsKey(category)) {
                    filtered.put(category, new ArrayList<>());
                    groupOrder.add(category);
                }
                filtered.get(category).add(product);
            }
        }

        List<RecyclerView.Adapter<?>> adapters = new ArrayList<>();
        for (Map.Entry<String, List<Product>> entry : filtered.entrySet()) {
            adapters.add(new ProductGroupAdapter(context, entry.getKey(), entry.getValue(), product -> {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }));
        }

        concatAdapter = new ConcatAdapter(adapters);
        recyclerProducts.setAdapter(concatAdapter);
    }

    private void renderFilterButtons() {
        layoutFilter.removeAllViews();

        String[] categories = new String[]{
                "Tẩy trang", "Rửa mặt", "Toner", "Serum",
                "Tẩy tế bào chết", "Mặt nạ", "Dưỡng ẩm", "Chống nắng"
        };

        for (String cat : categories) {
            Button btn = new Button(context);
            btn.setText(cat);
            btn.setTextSize(12f);
            btn.setAllCaps(false);
            btn.setPadding(24, 8, 24, 8);
            btn.setBackgroundResource(R.drawable.btn_filter);

            btn.setOnClickListener(v -> scrollToCategory(cat));
            layoutFilter.addView(btn);
        }
    }

    private void scrollToCategory(String category) {
        int index = groupOrder.indexOf(category);
        if (index != -1 && recyclerProducts.getLayoutManager() instanceof LinearLayoutManager) {
            ((LinearLayoutManager) recyclerProducts.getLayoutManager())
                    .scrollToPositionWithOffset(index, 0);
        }
    }

    private String classifyProduct(String name) {
        name = name.toLowerCase();
        if (name.contains("tẩy trang")) return "Tẩy trang";
        if (name.contains("rửa mặt")) return "Rửa mặt";
        if (name.contains("nước sen") || name.contains("nước nghệ")) return "Toner";
        if (name.contains("tinh chất")) return "Serum";
        if (name.contains("làm sạch da chết mặt") || name.contains("tẩy tế bào chết")) return "Tẩy tế bào chết";
        if (name.contains("mặt nạ")) return "Mặt nạ";
        if (name.contains("sáp dưỡng") || name.contains("thạch nghệ")) return "Dưỡng ẩm";
        if (name.contains("chống nắng") || name.contains("sữa chống nắng")) return "Chống nắng";
        return "Khác";
    }

    private String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }
}
