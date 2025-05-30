package com.onlinecourse.backend.service;

import com.onlinecourse.backend.dto.UserProgress;
import com.onlinecourse.backend.model.User;
import com.onlinecourse.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Map<String, String> verificationCodes = new HashMap<>();
    private final EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${file.upload-base-dir}")
    private String uploadBaseDir;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public UserProgress createUser(UserProgress userProgress) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(userProgress.getEmail()) != null) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        // Tạo mã xác minh
        String code = String.format("%06d", new Random().nextInt(999999));

        // Thử gửi email trước
        try {
            emailService.sendVerificationCode(userProgress.getEmail(), code);
        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi email xác minh. Vui lòng kiểm tra lại địa chỉ email.");
        }

        // Nếu gửi thành công → lưu user vào DB
        User user = new User();
        user.setName(userProgress.getName());
        user.setEmail(userProgress.getEmail());
        user.setRole(userProgress.getRole());
        user.setActive(false);

        String encodedPassword = passwordEncoder.encode(userProgress.getPassword());
        user.setPassword(encodedPassword);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Lưu code vào map
        verificationCodes.put(user.getEmail(), code);

        return convertToDTO(savedUser);
    }

    public UserProgress login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email không tồn tại");
        }

        boolean match = passwordEncoder.matches(rawPassword, user.getPassword());
        if (!match) {
            throw new RuntimeException("Mật khẩu không chính xác");
        }

        if (!user.isActive()) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        return convertToDTO(user);
    }

    @Transactional
    public void changePassword(int userId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        User user = userOpt.get();

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        // Mã hóa mật khẩu mới và lưu lại
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private UserProgress convertToDTO(User user) {
        return new UserProgress(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getImg()
        );
    }

    public void activateUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    public String getVerificationCode(String email) {
        return verificationCodes.get(email);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void generateAndSendVerificationCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        verificationCodes.put(email, code);
        emailService.sendVerificationCode(email, code);
    }

    @Transactional
    public String uploadUserImage(int userId, MultipartFile file) throws IOException {
        Optional<User> userOpt = userRepository.findById(userId);
        User user = userOpt.get();

        String userImgDir = uploadBaseDir + "/user_img";
        Path uploadPath = Paths.get(userImgDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (user.getImg() != null && !user.getImg().isEmpty()) {
            try {
                Path oldFilePath = Paths.get(uploadBaseDir, user.getImg());
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = "/user_img/" + fileName;
        user.setImg(fileUrl);
        userRepository.save(user);
        return fileUrl;
    }

    public User getUserProfile(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        User user = userOpt.get();
        return user;
    }

    @Transactional
    public User updateUserProfile(int userId, User updatedUser) {
        Optional<User> userOpt = userRepository.findById(userId);
        User user = userOpt.get();
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        return user;
    }
}