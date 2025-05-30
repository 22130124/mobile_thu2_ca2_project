package com.example.onlinecoursesapp.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.ImageUploadCallback;
import com.example.onlinecoursesapp.utils.ProfileUpdateCallback;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

//Kieu - thong tin ca nhan trong tab setting của dashboard
public class InformationPersonalFragment extends Fragment {
    private final String baseUrl = "http://10.0.2.2:8080";
    private UserRepository userRepository;
    private ImageView ivProfilePicture;
    private Button btnUploadImage;
    private TextView tvUserNameDisplay;
    private TextView tvUserEmailDisplay;
    private LinearLayout layoutInfoDisplay;
    private LinearLayout layoutInfoEdit;
    private TextView tvFullNameDisplay;
    private TextView tvUserEmailDisplayInfo;
    private TextInputEditText etFullName;
    private TextInputEditText etUserEmail;
    private Button btnSaveProfile;
    private ImageView ivEditIcon;
    private boolean isEditing = false;
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_personal, container, false);

        userRepository = UserRepository.getInstance(requireContext());

        // Init views (Display mode)
        ivProfilePicture = view.findViewById(R.id.iv_profile_picture);
        tvUserNameDisplay = view.findViewById(R.id.tv_user_name_display);
        tvUserEmailDisplay = view.findViewById(R.id.tv_user_email_display);
        layoutInfoDisplay = view.findViewById(R.id.layout_info_display);
        tvFullNameDisplay = view.findViewById(R.id.tv_full_name_display);
        tvUserEmailDisplayInfo = view.findViewById(R.id.tv_user_email_display_info);

        // Init views (Edit mode)
        btnUploadImage = view.findViewById(R.id.btn_upload_image);
        layoutInfoEdit = view.findViewById(R.id.layout_info_edit);
        etFullName = view.findViewById(R.id.et_full_name);
        etUserEmail = view.findViewById(R.id.et_user_email);
        btnSaveProfile = view.findViewById(R.id.btn_save_profile);

        // Init other views
        ivEditIcon = view.findViewById(R.id.iv_edit_icon);

        // back navigation
        view.findViewById(R.id.tv_profile_header).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uploadImage(uri);
                    }
                }
        );

        btnUploadImage.setOnClickListener(v -> {
            openImageChooser();
        });

        btnSaveProfile.setOnClickListener(v -> {
            saveInformationPersonal();
        });

        ivEditIcon.setOnClickListener(v -> {
            toggleEditMode();
        });

        loadInformationPersonal();
        setEditMode(false);
        return view;
    }

    private void loadInformationPersonal() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPrefs", 0);
        String name = sharedPref.getString("userName", "Không rõ");
        String email = sharedPref.getString("userEmail", "Không có email");
        String img = sharedPref.getString("img", "");

        tvUserNameDisplay.setText(name);
        tvUserEmailDisplay.setText(email);
        tvFullNameDisplay.setText(name);
        tvUserEmailDisplayInfo.setText(email);

        // Set initial values in EditTexts
        etFullName.setText(name);
        etUserEmail.setText(email);

        if (!img.isEmpty()) {
            Glide.with(this)
                    .load(baseUrl + img)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .circleCrop()
                    .into(ivProfilePicture);
        } else {
            ivProfilePicture.setImageResource(R.drawable.placeholder_image);
        }
    }

    private void saveInformationPersonal() {
        String name = etFullName.getText().toString();
        String email = etUserEmail.getText().toString();
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
                tvUserNameDisplay.setText(user.getName());
                tvUserEmailDisplay.setText(user.getEmail());
                tvFullNameDisplay.setText(user.getName());
                tvUserEmailDisplayInfo.setText(user.getEmail());

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
                    Glide.with(requireContext())
                            .load(fullImageUrl)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .circleCrop()
                            .into(ivProfilePicture);
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