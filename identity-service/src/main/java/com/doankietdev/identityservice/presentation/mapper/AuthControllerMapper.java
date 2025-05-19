package com.doankietdev.identityservice.presentation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doankietdev.identityservice.application.model.dto.LoginCommand;
import com.doankietdev.identityservice.application.model.dto.LoginResult;
import com.doankietdev.identityservice.application.model.dto.LogoutCommand;
import com.doankietdev.identityservice.application.model.dto.RegisterCommand;
import com.doankietdev.identityservice.application.model.dto.RegisterResult;
import com.doankietdev.identityservice.application.model.dto.VerifyAccountCommand;
import com.doankietdev.identityservice.application.model.dto.VerifyAccountResult;
import com.doankietdev.identityservice.infrastructure.model.auth.AuthUser;
import com.doankietdev.identityservice.presentation.model.dto.request.LoginRequest;
import com.doankietdev.identityservice.presentation.model.dto.request.RegisterRequest;
import com.doankietdev.identityservice.presentation.model.dto.request.VerifyAccountRequest;
import com.doankietdev.identityservice.presentation.model.dto.response.LoginResponse;
import com.doankietdev.identityservice.presentation.model.dto.response.RegisterResponse;
import com.doankietdev.identityservice.presentation.model.dto.response.VerifyAccountResponse;

@Mapper(componentModel = "spring")
public interface AuthControllerMapper {
  RegisterCommand toRegisterCommand(RegisterRequest request);
  RegisterResponse toRegisterResponse(RegisterResult result);
  VerifyAccountCommand toVerifyAccountCommand(VerifyAccountRequest request);
  VerifyAccountResponse toVerifyAccountResponse(VerifyAccountResult result);
  LoginCommand toLoginCommand(LoginRequest request);
  LoginResponse toLoginResponse(LoginResult result);

  @Mapping(target = "userId", source = "id")
  LogoutCommand toLogoutCommand(AuthUser authUser);
}
