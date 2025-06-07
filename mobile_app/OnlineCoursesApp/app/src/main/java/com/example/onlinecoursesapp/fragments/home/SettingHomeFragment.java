package com.example.onlinecoursesapp.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.activities.LoginActivity;
import com.example.onlinecoursesapp.fragments.ChangePasswordFragment;

public class SettingHomeFragment extends Fragment {
    private LinearLayout layoutLogout;
    private LinearLayout layoutPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        layoutLogout = view.findViewById(R.id.layout_logout);
        layoutPassword = view.findViewById(R.id.layout_password);

        layoutPassword.setOnClickListener(v -> {
            Fragment newFragment = new ChangePasswordFragment();
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, newFragment)
                    .commit();
        });

        layoutLogout.setOnClickListener(v -> {
            // Xóa dữ liệu SharedPreferences
            SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();

            // Quay về LoginActivity
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack
            startActivity(intent);
        });
    }
}