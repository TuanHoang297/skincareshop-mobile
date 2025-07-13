package fpt.edu.vn.skincareshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.Product;

public class GroupedProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Object> displayList;
    private ProductAdapter.OnItemClickListener listener;

    public GroupedProductAdapter(Context context, Map<String, List<Product>> groupedData, ProductAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.displayList = new ArrayList<>();

        for (Map.Entry<String, List<Product>> entry : groupedData.entrySet()) {
            displayList.add(entry.getKey()); // Header
            displayList.addAll(entry.getValue()); // Product items
        }
    }

    @Override
    public int getItemViewType(int position) {
        return displayList.get(position) instanceof String ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.item_product_group, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_HEADER) {
            String category = (String) displayList.get(position);
            ((HeaderViewHolder) holder).tvGroupTitle.setText(category);
        } else {
            Product product = (Product) displayList.get(position);
            ((ProductViewHolder) holder).bind(product);
        }
    }

    // ViewHolder cho tiêu đề nhóm
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupTitle;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupTitle = itemView.findViewById(R.id.tvGroupTitle);
        }
    }

    // ViewHolder cho sản phẩm
    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgProduct;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }

        void bind(final Product product) {
            tvName.setText(product.getName());
            tvPrice.setText("₫" + product.getPrice());

            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imgProduct);

            itemView.setOnClickListener(v -> listener.onItemClick(product));
        }
    }
}
