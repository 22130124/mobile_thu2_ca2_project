package com.example.onlinecoursesapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.CategoryAdapter;
import com.example.onlinecoursesapp.data.CategoryRepository;
import com.example.onlinecoursesapp.fragments.admin.CoursesManagementFragment;
import com.example.onlinecoursesapp.models.Category;
import com.example.onlinecoursesapp.utils.CategoryCallback;
import com.example.onlinecoursesapp.utils.CategoryListCallback;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryAdapter.OnCategoryActionListener {

    private ImageButton btnAddCategory;
    private TextInputEditText edtSearchCategory;
    private RecyclerView rvCategories;
    private CategoryAdapter adapter;

    private CategoryRepository categoryRepository;

    private List<Category> categoryList = new ArrayList<>();
    private List<Category> filteredList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        btnAddCategory = view.findViewById(R.id.btnAddCategory);
        edtSearchCategory = view.findViewById(R.id.edtSearchCategory);
        rvCategories = view.findViewById(R.id.rvCategories);

        categoryRepository = CategoryRepository.getInstance(requireContext());

        adapter = new CategoryAdapter(requireContext(), this);
        rvCategories.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCategories.setAdapter(adapter);

        btnAddCategory.setOnClickListener(v -> {
            showAddCategoryDialog();
        });

        edtSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategoryList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });

        loadCategoriesFromApi();

        return view;
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm thể loại");

        final EditText input = new EditText(getContext());
        input.setHint("Nhập tên thể loại");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String categoryName = input.getText().toString().trim();

            if (!categoryName.isEmpty()) {
                categoryRepository.createCategory(new Category(categoryName), new CategoryCallback() {
                    @Override
                    public void onSuccess(Category category) {
                        categoryList.add(category);
                        adapter.notifyItemInserted(categoryList.size() - 1);
                        Toast.makeText(getContext(), "Đã thêm thể loại thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        // Thông báo lỗi
                        Toast.makeText(getContext(), "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập tên thể loại", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Gọi API lấy danh sách category
    private void loadCategoriesFromApi() {
        categoryRepository.getAllCategories(new CategoryListCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                categoryList.clear();
                categoryList.addAll(categories);

                filteredList.clear();
                filteredList.addAll(categories);

                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> adapter.setCategoryList(filteredList));
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Lấy danh sách thất bại: " + message, Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void filterCategoryList(String keyword) {
        if (keyword.isEmpty()) {
            filteredList.clear();
            filteredList.addAll(categoryList);
        } else {
            List<Category> tempList = new ArrayList<>();
            for (Category c : categoryList) {
                if (c.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    tempList.add(c);
                }
            }
            filteredList.clear();
            filteredList.addAll(tempList);
        }
        adapter.notifyDataSetChanged();
    }

    public void onEdit(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chỉnh sửa thể loại");

        final EditText input = new EditText(getContext());
        input.setHint("Nhập tên thể loại");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(category.getName()); // Hiển thị tên hiện tại
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newCategoryName = input.getText().toString().trim();

            if (!newCategoryName.isEmpty()) {
                Category updatedCategory = new Category(category.getId(),newCategoryName);

                categoryRepository.updateCategory(category.getId(), updatedCategory, new CategoryCallback() {
                    @Override
                    public void onSuccess(Category updated) {
                        int index = categoryList.indexOf(category);
                        if (index != -1) {
                            categoryList.set(index, updated);
                            adapter.notifyItemChanged(index);
                        }
                        Toast.makeText(getContext(), "Đã cập nhật thể loại thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(), "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập tên thể loại", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    @Override
    public void onDelete(Category category) {
        // Gọi API xóa category
        categoryRepository.deleteCategory(category.getId(), new CategoryCallback() {
            @Override
            public void onSuccess(Category data) {
                if (getActivity() == null) return;

                // Xóa khỏi danh sách local
                categoryList.remove(category);

                // Cập nhật lại danh sách hiển thị theo từ khóa tìm kiếm hiện tại
                filterCategoryList(edtSearchCategory.getText() != null ? edtSearchCategory.getText().toString() : "");

                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onFailure(String message) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Xóa thất bại: " + message, Toast.LENGTH_LONG).show()
                );
            }
        });
    }
    @Override
    public void onCategoryClick(Category category) {
        // Mở CoursesFragment và truyền ID danh mục
        CoursesManagementFragment coursesFragment = CoursesManagementFragment.newInstance(category.getId());
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, coursesFragment)
                .addToBackStack(null)
                .commit();
    }
}
