package com.doankietdev.userservice.controller.http;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
public class UserController {
  @GetMapping("/hi")
  public String hi() {
      return "Hello ";
  }
}
