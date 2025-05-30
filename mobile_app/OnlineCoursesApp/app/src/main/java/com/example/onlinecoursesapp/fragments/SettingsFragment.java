package com.example.onlinecoursesapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.onlinecoursesapp.R;

import java.util.Locale;

//Kieu - setting cho dashboard
public class SettingsFragment extends Fragment {
    private LinearLayout layoutProfile;
    private LinearLayout layoutLanguage;
    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_LANGUAGE = "language";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutProfile = view.findViewById(R.id.layout_profile);
        layoutLanguage = view.findViewById(R.id.layout_language);

        layoutProfile.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new InformationPersonalFragment())
                    .addToBackStack(null)
                    .commit();
        });

//        layoutLanguage.setOnClickListener(v -> {
//            showLanguageSelectionDialog();
//        });
    }

//    private void showLanguageSelectionDialog() {
//        final String[] languages = {"English", "Tiếng Việt"};
//        final String[] languageCodes = {"en", "vi"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Chọn ngôn ngữ");
//        builder.setItems(languages, (dialog, which) -> {
//            String selectedLanguageCode = languageCodes[which];
//            setLocale(selectedLanguageCode);
//            if (getActivity() != null) {
//                getActivity().recreate();
//            }
//        });
//        builder.show();
//    }
//
//    private void setLocale(String languageCode) {
//        Locale locale = new Locale(languageCode);
//        Locale.setDefault(locale);
//        android.content.res.Configuration config = new android.content.res.Configuration();
//        config.locale = locale;
//        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
//
//        // Lưu lựa chọn ngôn ngữ vào SharedPreferences
//        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(KEY_LANGUAGE, languageCode);
//        editor.apply();
//    }




}