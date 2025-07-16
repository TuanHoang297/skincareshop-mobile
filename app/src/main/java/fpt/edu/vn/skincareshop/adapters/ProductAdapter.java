package fpt.edu.vn.skincareshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.Product;
import fpt.edu.vn.skincareshop.utils.FavoriteManager;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    private Context context;
    private List<Product> productList;
    private OnItemClickListener listener;

    public ProductAdapter(Context context, List<Product> productList, OnItemClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgProduct, imgFavorite;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
        }

        public void bind(final Product product) {
            tvName.setText(product.getName());
            tvPrice.setText("₫" + product.getPrice());

            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imgProduct);

            // Favorite logic
            boolean isFavorite = FavoriteManager.isFavorite(context, product.getId());
            imgFavorite.setImageResource(isFavorite ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);

            imgFavorite.setOnClickListener(v -> {
                boolean newState = !FavoriteManager.isFavorite(context, product.getId());
                FavoriteManager.setFavorite(context, product.getId(), newState);
                imgFavorite.setImageResource(newState ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            });

            itemView.setOnClickListener(v -> listener.onItemClick(product));
        }
    }
}
