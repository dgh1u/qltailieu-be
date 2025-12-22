package com.kltn.controller;

import com.kltn.config.JwtConfig;
import com.kltn.dto.entity.UserDto;
import com.kltn.dto.request.*;
import com.kltn.dto.request.*;
import com.kltn.dto.response.BaseResponse;
import com.kltn.dto.response.LoginResponse;
import com.kltn.dto.response.RegisterResponse;
import com.kltn.service.AuthenticateService;
import com.kltn.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticateController {
    private final AuthenticateService authenticateService;
    private final UserService userService;
    private final JwtConfig jwtConfig;

    // API xử lý đăng nhập người dùng
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticateService.login(request);
        return BaseResponse.successData(response);
    }

    // API đăng ký tài khoản mới
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authenticateService.register(request);
        return BaseResponse.successData(response);
    }

    // API xác thực tài khoản bằng mã OTP
    @PutMapping("verify-account")
    public ResponseEntity<?> verifyAccount(@Valid @RequestBody VerifyAccountRequest request) {
        return BaseResponse.successData(authenticateService.verifyAccount(request));
    }

    // API tạo lại mã OTP mới
    @PutMapping("regenerate-otp")
    public ResponseEntity<?> regenerateOTP(@Valid @RequestBody RegenerateOtpRequest request) {
        return BaseResponse.successData(authenticateService.regenerateOTP(request));
    }

    // API xử lý quên mật khẩu và đặt lại mật khẩu
    @PutMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return BaseResponse.successData(authenticateService.forgotPassword(request));
    }

    // API cập nhật thông tin hồ sơ người dùng
    @PutMapping("profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return BaseResponse.successData(authenticateService.updateProfile(request));
    }

    // API lấy thông tin hồ sơ người dùng từ token
    @GetMapping("profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String email = jwtConfig.getUserIdFromJWT(jwt);
        UserDto userDto = userService.selectUserByEmail(email);
        return BaseResponse.successData(userDto);
    }
}
