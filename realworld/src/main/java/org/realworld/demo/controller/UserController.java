package org.realworld.demo.controller;

import org.realworld.demo.controller.dto.UserDto.UserCreateRequest;
import org.realworld.demo.controller.dto.UserDto.UserLoginRequest;
import org.realworld.demo.controller.dto.UserDto.UserResponse;
import org.realworld.demo.controller.dto.UserDto.UserUpdateRequest;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.user.service.UserService;
import org.realworld.demo.jwt.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users/login")
  public UserResponse login(@RequestBody UserLoginRequest loginRequest) {
    return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
  }

  @PostMapping("/users")
  public UserResponse registerUser(@RequestBody UserCreateRequest request) {
    User user = userService.saveUser(request.toUser());

    return UserResponse.from(user, "");
  }

  @GetMapping("/user")
  public UserResponse getUser() {
    JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();

    return UserResponse.from((User) authentication.getPrincipal(), "");
  }

  @PutMapping("/user")
  public UserResponse updateUser(@RequestBody UserUpdateRequest request) {
    JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
        .getAuthentication();

    User user = userService.updateUser(
        (User) authentication.getPrincipal(),
        request.getEmail(),
        request.getUsername(),
        request.getPassword(),
        request.getImage(),
        request.getBio()
    );

    return UserResponse.from(user, "");
  }
}
