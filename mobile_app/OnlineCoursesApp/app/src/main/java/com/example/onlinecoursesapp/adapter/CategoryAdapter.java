package com.example.onlinecoursesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final Context context;
    private List<Category> categoryList;
    private final OnCategoryActionListener listener;

    public interface OnCategoryActionListener {
        void onEdit(Category category);
        void onDelete(Category category);
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(Context context, OnCategoryActionListener listener) {
        this.context = context;
        this.listener = listener;
        this.categoryList = new ArrayList<>();
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvCategoryName.setText(category.getName());

        // Set click listener for the entire category item
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));

        // Set click listener for the "More" button
        holder.btnMore.setOnClickListener(v -> showPopupMenu(holder.btnMore, category));
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    private void showPopupMenu(View view, Category category) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_category_item, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menuEdit) {
                listener.onEdit(category);
                return true;
            } else if (id == R.id.menuDelete) {
                listener.onDelete(category);
                return true;
            } else {
                return false;
            }
        });

        popupMenu.show();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCategoryName;
        final ImageButton btnMore;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
