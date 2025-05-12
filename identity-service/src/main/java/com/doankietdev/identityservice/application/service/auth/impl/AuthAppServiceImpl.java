package com.doankietdev.identityservice.application.service.auth.impl;

import java.time.Instant;
import java.util.Objects;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.model.dto.AuthKeyToken;
import com.doankietdev.identityservice.application.model.dto.AuthUser;
import com.doankietdev.identityservice.application.model.dto.request.AccountVerifyRequest;
import com.doankietdev.identityservice.application.model.dto.request.LoginRequest;
import com.doankietdev.identityservice.application.model.dto.request.RegisterRequest;
import com.doankietdev.identityservice.application.model.dto.response.AccountVerifyResponse;
import com.doankietdev.identityservice.application.model.dto.response.LoginResponse;
import com.doankietdev.identityservice.application.model.dto.response.RegisterResponse;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.application.service.auth.AuthAppService;
import com.doankietdev.identityservice.application.service.auth.AuthTokenService;
import com.doankietdev.identityservice.domain.model.dto.LoginSessionCreate;
import com.doankietdev.identityservice.domain.model.dto.OtpCreate;
import com.doankietdev.identityservice.domain.model.dto.UserCreate;
import com.doankietdev.identityservice.domain.model.dto.UserUpdate;
import com.doankietdev.identityservice.domain.model.entity.Otp;
import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.domain.model.enums.IdentityType;
import com.doankietdev.identityservice.domain.model.enums.OtpType;
import com.doankietdev.identityservice.domain.model.enums.UserStatus;
import com.doankietdev.identityservice.domain.repository.LoginSessionRepository;
import com.doankietdev.identityservice.domain.repository.OtpRepository;
import com.doankietdev.identityservice.domain.repository.UserRepository;
import com.doankietdev.identityservice.infrastructure.config.AuthProperties;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthAppServiceImpl implements AuthAppService {
  AuthTokenService authTokenService;
  UserRepository userRepository;
  LoginSessionRepository loginSessionRepository;
  OtpRepository otpRepository;
  PasswordEncoder passwordEncoder;
  AuthProperties authProperties;

  @Transactional
  @Override
  public RegisterResponse register(RegisterRequest request) {
    User existingUser = userRepository.findByIdentifier(request.getIdentifier());
    if (Objects.nonNull(existingUser)) {
      throw AppException.builder()
          .appCode(AppCode.USER_ALREADY_EXISTS)
          .build();
    }

    User newUser = userRepository.save(UserCreate.builder()
        .identifier(request.getIdentifier())
        .password(passwordEncoder.encode(request.getPassword()))
        .identityType(IdentityType.EMAIL)
        .status(UserStatus.PENDING)
        .build());

    if (Objects.isNull(newUser)) {
      throw AppException.builder()
          .appCode(AppCode.SERVER_ERROR)
          .logMessage("Failed to create user")
          .build();
    }

    Otp newOtp = otpRepository.save(
        OtpCreate.builder()
            .user(newUser)
            .code(createRandomOtp(authProperties.getEmailVerificationOtpLength()))
            .type(OtpType.EMAIL_VERIFICATION)
            .expiresAt(Instant.now().plusSeconds(authProperties.getEmailVerificationOtpTime()))
            .build());

    if (Objects.isNull(newOtp)) {
      throw AppException.builder()
          .appCode(AppCode.SERVER_ERROR)
          .logMessage("Failed to create OTP")
          .build();
    }

    return RegisterResponse.builder()
        .identifier(newUser.getIdentifier())
        .build();
  }

  @Transactional
  @Override
  public AccountVerifyResponse verifyAccount(AccountVerifyRequest request) {
    User existsUser = userRepository.findByIdentifier(request.getIdentifier());
    if (Objects.isNull(existsUser)) {
      throw AppException.builder()
          .appCode(AppCode.USER_NOT_FOUND)
          .build();
    }

    if (!existsUser.isNotVerifiedAccount()) {
      throw AppException.builder()
          .appCode(AppCode.ACCOUNT_VERIFY_IMPOSSIBLE)
          .build();
    }

    Otp existsOtp = otpRepository.findByUserIdAndCodeAndType(existsUser.getId(), request.getOtp(), OtpType.EMAIL_VERIFICATION);
    if (Objects.isNull(existsOtp)) {
      throw AppException.builder()
          .appCode(AppCode.OTP_INVALID)
          .build();
    }

    if (!existsOtp.isActive()) {
      throw AppException.builder()
          .appCode(AppCode.OTP_INVALID)
          .build();
    }

    if (existsOtp.isExpired()) {
      throw AppException.builder()
          .appCode(AppCode.OTP_EXPIRED)
          .build();
    }

    User updatedUser = userRepository.updateById(existsUser.getId(),
        UserUpdate.builder().status(UserStatus.ACTIVE).build());
    if (Objects.isNull(updatedUser)) {
      throw AppException.builder()
          .appCode(AppCode.SERVER_ERROR)
          .logMessage(String.format("Failed to update user status - User::%s - Update status::%s", existsUser,
              UserStatus.ACTIVE))
          .build();
    }

    boolean isDeleteSuccess = otpRepository.deleteById(existsOtp.getId());
    if (!isDeleteSuccess) {
      throw AppException.builder()
          .appCode(AppCode.SERVER_ERROR)
          .logMessage(String.format("Failed to delete OTP"))
          .build();
    }

    return AccountVerifyResponse.builder()
        .identifier(existsUser.getIdentifier())
        .build();
  }

  @Override
  public LoginResponse login(LoginRequest request, String clientIp, String userAgent) {
    User existsUser = userRepository.findByIdentifier(request.getIdentifier());

    if (Objects.isNull(existsUser)) {
      throw AppException.builder()
          .appCode(AppCode.CredentialIncorrect)
          .logMessage(String.format("Login attempt failed: User not found - %s", request.getIdentifier()))
          .build();
    }

    if (existsUser.isNotVerifiedAccount()) {
      throw AppException.builder()
          .appCode(AppCode.ACCOUNT_NOT_VERIFIED)
          .build();
    }

    if (!existsUser.isActive()) {
      throw AppException.builder()
          .appCode(AppCode.ACCOUNT_INACTIVE)
          .build();
    }

    boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), existsUser.getPassword());
    if (!isPasswordMatch) {
      throw AppException.builder()
          .appCode(AppCode.CredentialIncorrect)
          .logMessage(String.format("Login attempt failed: Password not match "))
          .build();
    }

    AuthKeyToken authKeyToken = authTokenService.createAuthKeyToken(AuthUser.builder()
        .userId(existsUser.getId())
        .build());

    loginSessionRepository.save(LoginSessionCreate.builder()
        .user(existsUser)
        .publicKey(authKeyToken.getPublicKey())
        .jti(authKeyToken.getJti())
        .ipAddress(clientIp)
        .userAgent(userAgent)
        .expiresAt(authKeyToken.getExpiresAt())
        .build());

    return LoginResponse.builder()
        .userId(existsUser.getId())
        .accessToken(authKeyToken.getAccessToken())
        .refreshToken(authKeyToken.getRefreshToken())
        .build();
  }

  private String createRandomOtp(int length) {
    Random random = new Random();
    StringBuilder oneTimePassword = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int randomNumber = random.nextInt(10);
      oneTimePassword.append(randomNumber);
    }
    return oneTimePassword.toString().trim();
  }
}
