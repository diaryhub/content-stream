package com.content.content_stream.user;

import com.content.content_stream.global.common.ApiResponse;
import com.content.content_stream.user.dto.LoginRequest;
import com.content.content_stream.user.dto.SignupRequest;
import com.content.content_stream.user.dto.TokenResponse;
import com.content.content_stream.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ApiResponse.ok("회원가입이 완료되었습니다.", userService.signup(request));
    }

    @PostMapping("/auth/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(userService.login(request));
    }

    @PostMapping("/auth/reissue")
    public ApiResponse<TokenResponse> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
        return ApiResponse.ok(userService.reissue(refreshToken));
    }

    @PostMapping("/auth/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal String email) {
        userService.logout(email);
        return ApiResponse.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/users/me")
    public ApiResponse<UserResponse> getMyInfo(@AuthenticationPrincipal String email) {
        return ApiResponse.ok(userService.getMyInfo(email));
    }
}
