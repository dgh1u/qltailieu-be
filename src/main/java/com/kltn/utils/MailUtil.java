package com.kltn.utils;

import com.kltn.exception.EmailException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String email, String otp) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("VNUA Documents - Xác thực tài khoản");

            // Build HTML content với StringBuilder để tránh lỗi encoding
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;\">");
            htmlBuilder.append("<div style=\"text-align: center; margin-bottom: 30px; background-color: #1e3c72; padding: 20px; border-radius: 12px 12px 0 0;\">");
            htmlBuilder.append("<h1 style=\"color: white; margin: 0; font-size: 28px;\">VNUA Documents</h1>");
            htmlBuilder.append("<p style=\"color: #e8f4fd; margin: 8px 0 0 0; font-size: 16px;\">Hệ thống quản lý tài liệu Học viện Nông nghiệp Việt Nam</p>");
            htmlBuilder.append("</div>");

            htmlBuilder.append("<div style=\"background-color: #ffffff; border: 1px solid #e1e8ed; border-radius: 0 0 12px 12px; padding: 30px;\">");
            htmlBuilder.append("<div style=\"text-align: center; margin-bottom: 25px;\">");
            htmlBuilder.append("<div style=\"background-color: #f8f9fa; border-radius: 50px; width: 80px; height: 80px; margin: 0 auto 15px; display: flex; align-items: center; justify-content: center;\">");
            htmlBuilder.append("<span style=\"font-size: 35px;\">🔐</span>");
            htmlBuilder.append("</div>");
            htmlBuilder.append("<h2 style=\"color: #1e3c72; margin: 0; font-size: 24px;\">Xác thực tài khoản</h2>");
            htmlBuilder.append("<p style=\"color: #5a6c7d; margin: 10px 0; font-size: 16px;\">Để truy cập vào hệ thống quản lý tài liệu, vui lòng sử dụng mã OTP bên dưới</p>");
            htmlBuilder.append("</div>");

            htmlBuilder.append("<div style=\"text-align: center; margin: 25px 0;\">");
            htmlBuilder.append("<div style=\"background-color: #667eea; border-radius: 12px; padding: 20px; margin: 20px 0; display: inline-block;\">");
            htmlBuilder.append("<p style=\"color: white; margin: 0 0 8px 0; font-size: 14px; text-transform: uppercase; letter-spacing: 1px;\">Mã xác thực</p>");
            htmlBuilder.append("<span style=\"font-size: 32px; font-weight: bold; color: white; letter-spacing: 4px; font-family: monospace;\">").append(otp).append("</span>");
            htmlBuilder.append("</div>");
            htmlBuilder.append("</div>");

            htmlBuilder.append("<div style=\"background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 8px; padding: 15px; margin: 25px 0; text-align: center;\">");
            htmlBuilder.append("<p style=\"color: #856404; font-size: 14px; margin: 0;\">");
            htmlBuilder.append("<strong>Mã OTP có hiệu lực trong 2 phút</strong><br>");
            htmlBuilder.append("Vui lòng không chia sẻ mã này với bất kỳ ai");
            htmlBuilder.append("</p>");
            htmlBuilder.append("</div>");

            htmlBuilder.append("<div style=\"border-top: 1px solid #e1e8ed; padding-top: 20px; margin-top: 30px;\">");
            htmlBuilder.append("<h3 style=\"color: #1e3c72; font-size: 18px; margin: 0 0 15px 0;\">Các tính năng chính:</h3>");
            htmlBuilder.append("<ul style=\"color: #5a6c7d; line-height: 1.6; margin: 0; padding-left: 20px;\">");
            htmlBuilder.append("<li>Quản lý và lưu trữ tài liệu an toàn</li>");
            htmlBuilder.append("<li>Tìm kiếm tài liệu nhanh chóng, chính xác</li>");
            htmlBuilder.append("<li>Phân quyền truy cập chi tiết</li>");
            htmlBuilder.append("<li>Theo dõi lịch sử chỉnh sửa tài liệu</li>");
            htmlBuilder.append("</ul>");
            htmlBuilder.append("</div>");
            htmlBuilder.append("</div>");

            htmlBuilder.append("<div style=\"margin-top: 25px; text-align: center; color: #8492a6; font-size: 13px; line-height: 1.5;\">");
            htmlBuilder.append("<p style=\"margin: 5px 0;\">Nếu bạn không yêu cầu mã xác thực này, vui lòng bỏ qua email này.</p>");
            htmlBuilder.append("<p style=\"margin: 5px 0;\">Để được hỗ trợ, liên hệ: support@vnua.edu.vn</p>");
            htmlBuilder.append("<div style=\"border-top: 1px solid #e1e8ed; padding-top: 15px; margin-top: 15px;\">");
            htmlBuilder.append("<p style=\"margin: 0;\"><strong>© 2025 VNUA Documents System</strong></p>");
            htmlBuilder.append("<p style=\"margin: 5px 0 0 0;\">Học viện Nông nghiệp Việt Nam - Hệ thống Quản lý Tài liệu</p>");
            htmlBuilder.append("</div>");
            htmlBuilder.append("</div>");
            htmlBuilder.append("</div>");

            String htmlContent = htmlBuilder.toString();

            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new EmailException("Lỗi gửi email xác thực: " + e.getMessage());
        }
    }
}