package com.example.onlinecoursesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.activities.course_progress.CourseProgressActivity;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.ImageUploadCallback;
import com.example.onlinecoursesapp.utils.ProfileUpdateCallback;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private final String baseUrl = "http://10.0.2.2:8080";
    private UserRepository userRepository;
    private ImageView imgView;
    private Button myCoursesButton, logoutButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userRepository = UserRepository.getInstance(this);

        imgView = findViewById(R.id.imgView);
        myCoursesButton = findViewById(R.id.myCoursesButton);
        logoutButton = findViewById(R.id.logoutButton);
        ivEditIcon = findViewById(R.id.iv_edit_icon);

        nameTextDisplay = findViewById(R.id.nameText_display);
        emailTextDisplay = findViewById(R.id.emailText_display);
        layoutInfoDisplay = findViewById(R.id.layout_info_display);

        btnUploadImage = findViewById(R.id.btn_upload_image);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        layoutInfoEdit = findViewById(R.id.layout_info_edit);
        btnSaveProfile = findViewById(R.id.btn_save_profile);

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
            Intent intent = new Intent(ProfileActivity.this, CourseProgressActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            sharedPref.edit().clear().apply();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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
    }

    private void loadUserProfileData() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPref.getString("userName", "Không rõ");
        String email = sharedPref.getString("userEmail", "Không có email");
        String img = sharedPref.getString("img", "");
        nameTextDisplay.setText(name);
        emailTextDisplay.setText(email);
        etName.setText(name);
        etEmail.setText(email);
        System.out.println("Hello");
        if (!img.isEmpty()) {
            Glide.with(this)
                    .load(baseUrl + img)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .circleCrop()
                    .into(imgView);

            System.out.println("Có img nha");
            System.out.println(baseUrl + img);
        } else {
            System.out.println("Không có img");
            imgView.setImageResource(R.drawable.placeholder_image);
        }
    }

    private void saveUserProfileData() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProgress updatedUser = new UserProgress();
        updatedUser.setId((int) userId);
        updatedUser.setName(name);
        updatedUser.setEmail(email);

        userRepository.updateUserProfileData(userId, updatedUser, new ProfileUpdateCallback() {
            @Override
            public void onSuccess(UserProgress user) {
                Toast.makeText(ProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileActivity.this, "Cập nhật thông tin thất bại: " + message, Toast.LENGTH_SHORT).show();
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
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPref.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
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
                    Glide.with(ProfileActivity.this)
                            .load(fullImageUrl)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .circleCrop()
                            .into(imgView);
                    Toast.makeText(ProfileActivity.this, "Tải ảnh lên thành công", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Lỗi phân tích phản hồi từ server: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(ProfileActivity.this, "Tải ảnh lên thất bại: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
