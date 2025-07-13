package fpt.edu.vn.skincareshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpt.edu.vn.skincareshop.R;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.SearchViewHolder> {

    public interface OnSearchClickListener {
        void onClick(String keyword);
    }

    private Context context;
    private List<String> keywords;
    private OnSearchClickListener listener;

    public SearchHistoryAdapter(Context context, List<String> keywords, OnSearchClickListener listener) {
        this.context = context;
        this.keywords = keywords;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_history, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        String keyword = keywords.get(position);
        holder.tvKeyword.setText(keyword);
        holder.itemView.setOnClickListener(v -> listener.onClick(keyword));
    }

    @Override
    public int getItemCount() {
        return keywords.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView tvKeyword;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKeyword = itemView.findViewById(R.id.tvSearchKeyword);
        }
    }
}
