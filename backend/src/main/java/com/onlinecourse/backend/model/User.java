package com.onlinecourse.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private boolean isActive;
    private String img;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Enum Role cho quyền người dùng
    public enum Role {
        USER,
        ADMIN
    }
}

