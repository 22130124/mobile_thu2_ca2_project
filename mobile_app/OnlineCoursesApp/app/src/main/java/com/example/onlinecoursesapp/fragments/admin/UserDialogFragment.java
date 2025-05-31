package com.example.onlinecoursesapp.fragments.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.data.UserRepository;
import com.example.onlinecoursesapp.models.UserProgress;
import com.example.onlinecoursesapp.utils.UserCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

//kieu - dialog cho them va sua
public class UserDialogFragment extends DialogFragment {
    public interface UserDialogListener {
        void onUserSaved(UserProgress user, boolean isEdit);
    }

    private UserProgress userToEdit;
    private UserDialogListener listener;

    public UserDialogFragment(UserDialogListener listener, @Nullable UserProgress userToEdit) {
        this.listener = listener;
        this.userToEdit = userToEdit;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 90% màn hình
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_dialog, null);

        TextView tvHeader = view.findViewById(R.id.tvUserDialogHeader);
        TextInputEditText etName = view.findViewById(R.id.etUserName);
        TextInputEditText etEmail = view.findViewById(R.id.etUserEmail);
        TextInputEditText etPassword = view.findViewById(R.id.etUserPassword);
        TextInputEditText etRole = view.findViewById(R.id.etUserRole);
        MaterialButton btnSave = view.findViewById(R.id.btnSaveUser);

        // Đặt tiêu đề và nút theo chế độ Thêm/Sửa
        if (userToEdit != null) {
            tvHeader.setText("Sửa người dùng");
            btnSave.setText("Cập nhật");
            etName.setText(userToEdit.getName());
            etEmail.setText(userToEdit.getEmail());
            etEmail.setEnabled(false); // Không cho sửa email
            etRole.setText(userToEdit.getRole());
        } else {
            tvHeader.setText("Thêm người dùng");
            btnSave.setText("Thêm");
        }

        btnSave.setText(userToEdit == null ? "Thêm" : "Cập nhật");

        if (userToEdit != null) {
            etName.setText(userToEdit.getName());
            etEmail.setText(userToEdit.getEmail());
            etEmail.setEnabled(false); // Không cho sửa email
            etRole.setText(userToEdit.getRole());
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String role = etRole.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            UserProgress user = userToEdit != null ? userToEdit : new UserProgress();
            user.setName(name);
            user.setEmail(email);
            user.setRole(role);
            if (!TextUtils.isEmpty(password)) {
                user.setPassword(password);
            }

            UserRepository repo = UserRepository.getInstance(getContext());
            if (userToEdit == null) {
                // Thêm user mới
                repo.addUser(user, new UserCallback() {
                    @Override
                    public void onSuccess(UserProgress user) {
                        listener.onUserSaved(user, false);
                        dismiss();
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Sửa user
                repo.updateUser(user.getId(), user, new UserCallback() {
                    @Override
                    public void onSuccess(UserProgress user) {
                        listener.onUserSaved(user, true);
                        dismiss();
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        dialog.setTitle(userToEdit == null ? "Thêm người dùng" : "Sửa người dùng");
        return dialog;
    }
}