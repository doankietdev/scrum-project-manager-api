package com.doankietdev.identityservice.application.service.auth;

import com.doankietdev.identityservice.application.model.dto.request.AccountVerifyRequest;
import com.doankietdev.identityservice.application.model.dto.request.LoginRequest;
import com.doankietdev.identityservice.application.model.dto.request.RegisterRequest;
import com.doankietdev.identityservice.application.model.dto.response.AccountVerifyResponse;
import com.doankietdev.identityservice.application.model.dto.response.LoginResponse;
import com.doankietdev.identityservice.application.model.dto.response.RegisterResponse;

public interface AuthService {
    public RegisterResponse register(RegisterRequest request);
    public AccountVerifyResponse verifyAccount(AccountVerifyRequest request);
    public LoginResponse login(LoginRequest request, String clientIp, String userAgent);
}
