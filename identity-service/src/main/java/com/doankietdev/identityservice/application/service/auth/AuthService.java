package com.doankietdev.identityservice.application.service.auth;

import com.doankietdev.identityservice.application.model.dto.LoginCommand;
import com.doankietdev.identityservice.application.model.dto.LoginResult;
import com.doankietdev.identityservice.application.model.dto.RegisterCommand;
import com.doankietdev.identityservice.application.model.dto.RegisterResult;
import com.doankietdev.identityservice.application.model.dto.VerifyAccountCommand;
import com.doankietdev.identityservice.application.model.dto.VerifyAccountResult;

public interface AuthService {
    public RegisterResult register(RegisterCommand command);
    public VerifyAccountResult verifyAccount(VerifyAccountCommand command);
    public LoginResult login(LoginCommand command, String clientIp, String userAgent);
}
