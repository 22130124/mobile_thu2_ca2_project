package com.example.onlinecoursesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;

import java.util.List;

public class PopularSearchAdapter extends RecyclerView.Adapter<PopularSearchAdapter.ViewHolder> {

    private List<String> popularSearches;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String searchTerm);
    }

    public PopularSearchAdapter(List<String> popularSearches) {
        this.popularSearches = popularSearches;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String searchTerm = popularSearches.get(position);
        holder.tvSearchTerm.setText(searchTerm);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(searchTerm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popularSearches != null ? popularSearches.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSearchTerm;
        ImageView ivArrow;

        ViewHolder(View itemView) {
            super(itemView);
            tvSearchTerm = itemView.findViewById(R.id.tvSearchTerm);
            ivArrow = itemView.findViewById(R.id.ivArrow);
        }
    }
}