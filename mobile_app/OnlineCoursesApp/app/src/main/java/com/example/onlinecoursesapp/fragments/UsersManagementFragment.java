package com.example.onlinecoursesapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.UserAdapter;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.UserCallback;
import com.example.onlinecoursesapp.utils.UserListCallback;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class UsersManagementFragment extends Fragment {
    private UserRepository userRepository;
    private RecyclerView usersRecyclerView;
    private UserAdapter userAdapter;
    private TextInputEditText searchEditText;
    private ChipGroup filterChipGroup;
    private FloatingActionButton fabAddUser;
    private List<UserProgress> allUsers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_management, container, false);
        userRepository = UserRepository.getInstance(requireContext());
        initViews(view);
        setupRecyclerView();
        loadUsers();
        setupListeners();
        return view;
    }

    private void initViews(View view) {
        usersRecyclerView = view.findViewById(R.id.usersRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        filterChipGroup = view.findViewById(R.id.filterChipGroup);
        fabAddUser = view.findViewById(R.id.fabAddUser);
    }

    private void setupRecyclerView() {
        userAdapter = new UserAdapter(new ArrayList<>(), new UserAdapter.UserAdapterListener() {
            @Override
            public void onEditUser(UserProgress user) {
                UserDialogFragment dialog = new UserDialogFragment((updatedUser, isEdit) -> {
                    if (isEdit) {
                        userAdapter.updateUser(updatedUser);
                    }
                }, user);
                dialog.show(getParentFragmentManager(), "EditUserDialog");
            }
            @Override
            public void onToggleStatus(UserProgress user) {
                updateUserStatus(user);
            }
        });

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        usersRecyclerView.setAdapter(userAdapter);
    }

    private void setupListeners() {
        System.out.println("Vào listener");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    searchUsers(s.toString());
                } else {
                    loadUsers();
                }
            }
        });

        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip selectedChip = group.findViewById(checkedId);
            if (selectedChip != null) {
                if (selectedChip.getId() == R.id.chipAll) {
                    loadUsers();
                } else if (selectedChip.getId() == R.id.chipActive) {
                    System.out.println("Chọn các user hoạt động");
                    filterUsersByStatus(true);
                } else if (selectedChip.getId() == R.id.chipInactive) {
                    System.out.println("Chọn các user không hoat dong");
                    filterUsersByStatus(false);
                }
            }
        });

        // Add user button
        fabAddUser.setOnClickListener(v -> {
            UserDialogFragment dialog = new UserDialogFragment((user, isEdit) -> {
                if (!isEdit) {
                    allUsers.add(user);
                    userAdapter.updateUsers(allUsers);
                }
            }, null);
            dialog.show(getParentFragmentManager(), "AddUserDialog");
        });
    }

    private void loadUsers() {
        userRepository.getAllUsers(new UserListCallback() {
            @Override
            public void onSuccess(List<UserProgress> users) {
                allUsers = users;
                userAdapter.updateUsers(users);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchUsers(String query) {
        userRepository.searchUsers(query, new UserListCallback() {
            @Override
            public void onSuccess(List<UserProgress> users) {
                userAdapter.updateUsers(users);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUsersByStatus(boolean active) {
        userRepository.filterUsersByStatus(active, new UserListCallback() {
            @Override
            public void onSuccess(List<UserProgress> users) {
                System.out.println("Vào load danh sách user loc r nè");
                for(UserProgress i: users){
                    System.out.println(i.toString());
                }
                userAdapter.updateUsers(users);
                System.out.println("Update xong r nha");
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println("Thất bại lọc nha");
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserStatus(UserProgress user) {
        userRepository.updateUserStatus(user.getId(), !user.isActive(), new UserCallback() {
            @Override
            public void onSuccess(UserProgress updatedUser) {
                userAdapter.updateUser(updatedUser);
                Toast.makeText(requireContext(),
                        "update trạng thái thành công",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}