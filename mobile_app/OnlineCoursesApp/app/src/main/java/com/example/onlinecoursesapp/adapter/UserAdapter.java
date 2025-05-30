package com.example.onlinecoursesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.models.UserProgress;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserProgress> users;
    private final UserAdapterListener listener;

    public interface UserAdapterListener {
        void onEditUser(UserProgress user);
        void onToggleStatus(UserProgress user);
    }

    public UserAdapter(List<UserProgress> users, UserAdapterListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserProgress user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void updateUsers(List<UserProgress> newUsers) {
        this.users = newUsers;
        notifyDataSetChanged();
    }

    public void updateUser(UserProgress updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                notifyItemChanged(i);
                break;
            }
        }
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView emailTextView;
        private final TextView roleTextView;
        private final SwitchMaterial statusSwitch;
        private final ImageButton editButton;
        private final ShapeableImageView userAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.userNameTextView);
            emailTextView = itemView.findViewById(R.id.userEmailTextView);
            roleTextView = itemView.findViewById(R.id.userRoleTextView);
            statusSwitch = itemView.findViewById(R.id.userStatusSwitch);
            editButton = itemView.findViewById(R.id.editUserButton);
            userAvatar = itemView.findViewById(R.id.userAvatar);
        }

        public void bind(UserProgress user) {
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            roleTextView.setText(user.getRole());
            statusSwitch.setChecked(user.isActive());

            if (user.getImg() != null && !user.getImg().isEmpty()) {
                String imgUrl = user.getImg();
                if (!imgUrl.startsWith("http")) {
                    imgUrl = "http://10.0.2.2:8080" + imgUrl;
                }
                Glide.with(itemView.getContext())
                        .load(imgUrl)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .circleCrop()
                        .into(userAvatar);
            } else {
                userAvatar.setImageResource(R.drawable.placeholder_image);
            }

            // Set up click listeners
            editButton.setOnClickListener(v -> listener.onEditUser(user));
            statusSwitch.setOnClickListener(v -> listener.onToggleStatus(user));
        }
    }
}