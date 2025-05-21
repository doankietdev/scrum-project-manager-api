package com.doankietdev.identityservice.application.service.auth.impl;

import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doankietdev.identityservice.application.exception.AppException;
import com.doankietdev.identityservice.application.model.cache.LoginSessionCache;
import com.doankietdev.identityservice.application.model.cache.LoginSessionCached;
import com.doankietdev.identityservice.application.model.dto.KeyToken;
import com.doankietdev.identityservice.application.model.dto.LoginCommand;
import com.doankietdev.identityservice.application.model.dto.LoginResult;
import com.doankietdev.identityservice.application.model.dto.LogoutCommand;
import com.doankietdev.identityservice.application.model.dto.RegisterCommand;
import com.doankietdev.identityservice.application.model.dto.RegisterResult;
import com.doankietdev.identityservice.application.model.dto.TokenPayload;
import com.doankietdev.identityservice.application.model.dto.VerifyAccountCommand;
import com.doankietdev.identityservice.application.model.dto.VerifyAccountResult;
import com.doankietdev.identityservice.application.model.enums.AppCode;
import com.doankietdev.identityservice.application.service.auth.AuthService;
import com.doankietdev.identityservice.application.service.auth.cache.LoginSessionCacheService;
import com.doankietdev.identityservice.application.spi.KeyTokenService;
import com.doankietdev.identityservice.domain.model.dto.LoginSessionCreate;
import com.doankietdev.identityservice.domain.model.dto.OtpCreate;
import com.doankietdev.identityservice.domain.model.dto.UserCreate;
import com.doankietdev.identityservice.domain.model.dto.UserUpdate;
import com.doankietdev.identityservice.domain.model.entity.LoginSession;
import com.doankietdev.identityservice.domain.model.entity.Otp;
import com.doankietdev.identityservice.domain.model.entity.User;
import com.doankietdev.identityservice.domain.model.enums.IdentityType;
import com.doankietdev.identityservice.domain.model.enums.OtpType;
import com.doankietdev.identityservice.domain.model.enums.UserStatus;
import com.doankietdev.identityservice.domain.repository.LoginSessionRepository;
import com.doankietdev.identityservice.domain.repository.OtpRepository;
import com.doankietdev.identityservice.domain.repository.UserRepository;
import com.doankietdev.identityservice.infrastructure.config.AppProperties;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
  KeyTokenService keyTokenService;
  UserRepository userRepository;
  LoginSessionRepository loginSessionRepository;
  OtpRepository otpRepository;
  PasswordEncoder passwordEncoder;
  AppProperties appProperties;
  LoginSessionCacheService loginSessionCacheService;

  @Transactional
  @Override
  public RegisterResult register(RegisterCommand command) {
    User existingUser = userRepository.findByIdentifier(command.getIdentifier());
    if (Objects.nonNull(existingUser))
      throw AppException.from(AppCode.USER_ALREADY_EXISTS);

    User newUser = userRepository.save(UserCreate.builder()
        .identifier(command.getIdentifier())
        .password(passwordEncoder.encode(command.getPassword()))
        .identityType(IdentityType.EMAIL)
        .status(UserStatus.PENDING)
        .build());

    if (Objects.isNull(newUser))
      throw AppException.from(AppCode.SERVER_ERROR);

    Otp newOtp = otpRepository.save(
        OtpCreate.builder()
            .user(newUser)
            .code(createRandomOtp(appProperties.getAuth().getEmailVerificationOtpLength()))
            .type(OtpType.EMAIL_VERIFICATION)
            .expiresAt(Instant.now().plusSeconds(appProperties.getAuth().getEmailVerificationOtpExpirationTime()))
            .build());

    if (Objects.isNull(newOtp)) {
      throw AppException.from(AppCode.SERVER_ERROR).withLog("Failed to create OTP");
    }

    return RegisterResult.builder()
        .identifier(newUser.getIdentifier())
        .build();
  }

  @Transactional
  @Override
  public VerifyAccountResult verifyAccount(VerifyAccountCommand command) {
    User existsUser = userRepository.findByIdentifier(command.getIdentifier());
    if (Objects.isNull(existsUser))
      throw AppException.from(AppCode.ACCOUNT_NOTFOUND);
    if (!existsUser.isNotVerifiedAccount())
      throw AppException.from(AppCode.ACCOUNT_VERIFY_IMPOSSIBLE);
    Otp existsOtp = otpRepository.findByUserIdAndCodeAndType(existsUser.getId(), command.getOtp(),
        OtpType.EMAIL_VERIFICATION);
    if (Objects.isNull(existsOtp))
      throw AppException.from(AppCode.OTP_INVALID);
    if (!existsOtp.isActive())
      throw AppException.from(AppCode.OTP_INVALID);
    if (existsOtp.isExpired()) {
      throw AppException.from(AppCode.OTP_EXPIRED);
    }

    User updatedUser = userRepository.updateById(existsUser.getId(),
        UserUpdate.builder().status(UserStatus.ACTIVE).build());
    if (Objects.isNull(updatedUser)) {
      throw AppException
          .from(AppCode.SERVER_ERROR)
          .withLog(String.format(
              "Failed to update user status - User::%s - Update status::%s",
              existsUser,
              UserStatus.ACTIVE));
    }

    boolean isDeleteSuccess = otpRepository.deleteById(existsOtp.getId());
    if (!isDeleteSuccess) {
      throw AppException.from(AppCode.SERVER_ERROR)
          .withLog(String.format("Failed to delete OTP"));
    }

    return VerifyAccountResult.builder()
        .identifier(existsUser.getIdentifier())
        .build();
  }

  @Transactional
  @Override
  public LoginResult login(LoginCommand command, String clientIp, String userAgent) {
    User existsUser = userRepository.findByIdentifier(command.getIdentifier());

    if (Objects.isNull(existsUser)) {
      throw AppException.from(AppCode.CredentialIncorrect)
          .withLog(String.format("Login attempt failed: User not found - %s", command.getIdentifier()));
    }
    if (existsUser.isNotVerifiedAccount())
      throw AppException.from(AppCode.ACCOUNT_NOT_VERIFIED);
    if (!existsUser.isActive())
      throw AppException.from(AppCode.ACCOUNT_INACTIVE);

    boolean isPasswordMatch = passwordEncoder.matches(command.getPassword(), existsUser.getPassword());
    if (!isPasswordMatch) {
      throw AppException
          .from(AppCode.CredentialIncorrect)
          .withLog(String.format("Login attempt failed: Password not match "));
    }

    KeyToken keyToken = keyTokenService.createKeyToken(TokenPayload.builder()
        .userId(existsUser.getId())
        .jti(UUID.randomUUID().toString())
        .identifier(existsUser.getIdentifier())
        .build());

    LoginSession loginSession = loginSessionRepository.save(LoginSessionCreate.builder()
        .user(existsUser)
        .publicKey(keyToken.getPublicKey())
        .jti(keyToken.getJti())
        .ipAddress(clientIp)
        .userAgent(userAgent)
        .expiresAt(keyToken.getExpiresAt())
        .build());

    String userId = loginSession.getUser().getId();
    String jti = loginSession.getJti();

    loginSessionCacheService.put(userId, jti, LoginSessionCache.builder()
        .loginSession(LoginSessionCached.builder()
            .userId(userId)
            .jti(jti)
            .publicKey(loginSession.getPublicKey())
            .build())
        .build());

    return LoginResult.builder()
        .accessToken(keyToken.getAccessToken())
        .refreshToken(keyToken.getRefreshToken())
        .build();
  }

  @Transactional
  @Override
  public void logout(LogoutCommand command) {
    boolean isDeleteLoginSessionCache = loginSessionCacheService.deleteLoginSession(command.getUserId(),
        command.getJti());
    if (!isDeleteLoginSessionCache)
      throw AppException.from(AppCode.SERVER_ERROR);

    boolean isDeleteLoginSessionDB = loginSessionRepository.deletePermanentByUserIdAndJti(command.getUserId(),
        command.getJti());
    if (!isDeleteLoginSessionDB)
      throw AppException.from(AppCode.SERVER_ERROR);
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
