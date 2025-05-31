package com.example.onlinecoursesapp.fragments.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.activities.LoginActivity;
import com.example.onlinecoursesapp.activities.course_progress.CourseProgressActivity;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.ImageUploadCallback;
import com.example.onlinecoursesapp.utils.ProfileUpdateCallback;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private final String baseUrl = "http://10.0.2.2:8080";
    private UserRepository userRepository;
    private ImageView imgView;
    private Button myCoursesButton;
    private TextView nameTextDisplay;
    private TextView emailTextDisplay;
    private LinearLayout layoutInfoDisplay;
    private ImageView ivEditIcon;
    private Button btnUploadImage;
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private LinearLayout layoutInfoEdit;
    private Button btnSaveProfile;
    private boolean isEditing = false;

    private ActivityResultLauncher<String> pickImageLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userRepository = UserRepository.getInstance(requireContext());

        imgView = view.findViewById(R.id.imgView);
        myCoursesButton = view.findViewById(R.id.myCoursesButton);
        ivEditIcon = view.findViewById(R.id.iv_edit_icon);

        nameTextDisplay = view.findViewById(R.id.nameText_display);
        emailTextDisplay = view.findViewById(R.id.emailText_display);
        layoutInfoDisplay = view.findViewById(R.id.layout_info_display);

        btnUploadImage = view.findViewById(R.id.btn_upload_image);
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        layoutInfoEdit = view.findViewById(R.id.layout_info_edit);
        btnSaveProfile = view.findViewById(R.id.btn_save_profile);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uploadImage(uri);
                    }
                }
        );

        loadUserProfileData();
        setEditMode(false);

        myCoursesButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CourseProgressActivity.class);
            startActivity(intent);
        });

        ivEditIcon.setOnClickListener(v -> {
            toggleEditMode();
        });

        btnUploadImage.setOnClickListener(v -> {
            openImageChooser();
        });

        btnSaveProfile.setOnClickListener(v -> {
            saveUserProfileData();
        });

        return view;
    }

    private void loadUserProfileData() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", 0);
        String name = sharedPref.getString("userName", "Không rõ");
        String email = sharedPref.getString("userEmail", "Không có email");
        String img = sharedPref.getString("img", "");
        nameTextDisplay.setText(name);
        emailTextDisplay.setText(email);
        etName.setText(name);
        etEmail.setText(email);
        if (!img.isEmpty()) {
            Glide.with(this)
                    .load(baseUrl + img)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .circleCrop()
                    .into(imgView);
        } else {
            imgView.setImageResource(R.drawable.placeholder_image);
        }
    }

    private void saveUserProfileData() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", 0);
        int userId = sharedPref.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(getContext(), "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProgress updatedUser = new UserProgress();
        updatedUser.setId(userId);
        updatedUser.setName(name);
        updatedUser.setEmail(email);

        userRepository.updateUserProfileData(userId, updatedUser, new ProfileUpdateCallback() {
            @Override
            public void onSuccess(UserProgress user) {
                Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                nameTextDisplay.setText(user.getName());
                emailTextDisplay.setText(user.getEmail());

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("userName", user.getName());
                editor.putString("userEmail", user.getEmail());
                editor.apply();
                setEditMode(false);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Cập nhật thông tin thất bại: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleEditMode() {
        setEditMode(!isEditing);
    }

    private void setEditMode(boolean enabled) {
        isEditing = enabled;

        layoutInfoDisplay.setVisibility(enabled ? View.GONE : View.VISIBLE);
        layoutInfoEdit.setVisibility(enabled ? View.VISIBLE : View.GONE);

        btnUploadImage.setVisibility(enabled ? View.VISIBLE : View.GONE);
        btnSaveProfile.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }

    private void openImageChooser() {
        pickImageLauncher.launch("image/*");
    }

    private void uploadImage(Uri imageUri) {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", 0);
        int userId = sharedPref.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(getContext(), "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.uploadUserImage(userId, imageUri, new ImageUploadCallback() {
            @Override
            public void onSuccess(String responseBodyString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBodyString);
                    String imageUrl = jsonObject.getString("imageUrl");

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("img", imageUrl);
                    editor.apply();

                    String fullImageUrl = baseUrl + imageUrl;
                    Glide.with(ProfileFragment.this)
                            .load(fullImageUrl)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .circleCrop()
                            .into(imgView);
                    Toast.makeText(getContext(), "Tải ảnh lên thành công", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Lỗi phân tích phản hồi từ server: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Tải ảnh lên thất bại: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
