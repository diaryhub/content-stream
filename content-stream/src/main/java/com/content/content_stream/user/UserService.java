package com.content.content_stream.user;

import com.content.content_stream.global.exception.CustomException;
import com.content.content_stream.global.exception.ErrorCode;
import com.content.content_stream.global.jwt.JwtProvider;
import com.content.content_stream.user.dto.LoginRequest;
import com.content.content_stream.user.dto.SignupRequest;
import com.content.content_stream.user.dto.TokenResponse;
import com.content.content_stream.user.dto.UserResponse;
import com.content.content_stream.user.token.RefreshToken;
import com.content.content_stream.user.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.createLocalUser(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname()
        );

        return UserResponse.from(userRepository.save(user));
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        // Refresh Token Redis 저장 (초 단위)
        refreshTokenRepository.save(new RefreshToken(
                user.getEmail(),
                refreshToken,
                refreshTokenExpiration / 1000
        ));

        return TokenResponse.of(accessToken, refreshToken);
    }

    public TokenResponse reissue(String refreshToken) {
        if (!jwtProvider.isValid(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtProvider.getEmail(refreshToken);

        RefreshToken stored = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.EXPIRED_TOKEN));

        if (!stored.getToken().equals(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole().name());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getEmail());

        refreshTokenRepository.save(new RefreshToken(
                email,
                newRefreshToken,
                refreshTokenExpiration / 1000
        ));

        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    public void logout(String email) {
        refreshTokenRepository.deleteById(email);
    }

    public UserResponse getMyInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }
}
