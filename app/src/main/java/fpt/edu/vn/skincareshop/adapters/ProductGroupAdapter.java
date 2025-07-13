package fpt.edu.vn.skincareshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;
import fpt.edu.vn.skincareshop.models.Product;

public class ProductGroupAdapter extends RecyclerView.Adapter<ProductGroupAdapter.GroupViewHolder> {

    private Context context;
    private String groupTitle;
    private List<Product> productList;
    private ProductAdapter.OnItemClickListener listener;

    public ProductGroupAdapter(Context context, String groupTitle, List<Product> productList, ProductAdapter.OnItemClickListener listener) {
        this.context = context;
        this.groupTitle = groupTitle;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_group_with_grid, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.tvGroupTitle.setText(groupTitle);

        ProductAdapter adapter = new ProductAdapter(context, productList, listener);
        holder.recyclerGroup.setLayoutManager(new GridLayoutManager(context, 2));
        holder.recyclerGroup.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 1; // mỗi adapter là 1 nhóm
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupTitle;
        RecyclerView recyclerGroup;

        GroupViewHolder(View itemView) {
            super(itemView);
            tvGroupTitle = itemView.findViewById(R.id.tvGroupTitle);
            recyclerGroup = itemView.findViewById(R.id.recyclerGroup);
        }
    }
}
