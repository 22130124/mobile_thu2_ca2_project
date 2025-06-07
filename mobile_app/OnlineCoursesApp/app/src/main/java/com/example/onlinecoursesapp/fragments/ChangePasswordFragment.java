package com.example.onlinecoursesapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.fragments.admin.SettingsFragment;
import com.example.onlinecoursesapp.utils.ChangePasswordCallback;

public class ChangePasswordFragment extends Fragment {
    private EditText oldPassword, newPassword, confirmPassword;
    private Button btnChangePassword;
    private UserRepository userRepository;
    private int userId = 123;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        oldPassword = view.findViewById(R.id.oldPassword);
        newPassword = view.findViewById(R.id.newPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        userRepository = UserRepository.getInstance(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        btnChangePassword.setOnClickListener(v -> {
            String oldPass = oldPassword.getText().toString().trim();
            String newPass = newPassword.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();
            String role = prefs.getString("userRole", "user");

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(getContext(), "Mật khẩu mới và xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            userRepository.changePassword(userId, oldPass, newPass, new ChangePasswordCallback() {
                @Override
                public void onSuccess(String message) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                        String role = prefs.getString("userRole", "user");

                        Fragment newFragment;
                        if ("admin".equalsIgnoreCase(role)) {
                            newFragment = new com.example.onlinecoursesapp.fragments.admin.SettingsFragment();
                        } else {
                            newFragment = new com.example.onlinecoursesapp.fragments.home.SettingHomeFragment();
                        }

                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, newFragment)
                                .commit();
                    });
                }

                @Override
                public void onFailure(String error) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }
}