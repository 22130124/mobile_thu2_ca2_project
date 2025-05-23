package com.onlinecourse.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã xác minh tài khoản");
        message.setText("Xin chào,\n\nMã xác minh của bạn là: " + code + "\nVui lòng nhập mã để kích hoạt.");
        message.setFrom("22130124@st.hcmuaf.edu.vn");

        mailSender.send(message);
    }
}

