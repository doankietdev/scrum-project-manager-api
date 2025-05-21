package com.doankietdev.profileservice.shared.constants;

import org.springframework.http.HttpHeaders;

public class AppHttpHeaders extends HttpHeaders {
  public static final String USER_ID = "X-User-Id";
  public static final String PERMISSIONS = "X-Permissions";
}
