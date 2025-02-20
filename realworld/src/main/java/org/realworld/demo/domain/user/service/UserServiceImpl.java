package org.realworld.demo.domain.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.realworld.demo.controller.dto.UserDto.UserResponse;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.user.repository.UserRepository;
import org.realworld.demo.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final Jwt jwt;

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public User updateUser(Long userId, String email, String username,
      String password, String image, String bio) {
    User user = userRepository.findById(userId).orElseThrow(IllegalStateException::new);
    user.update(email, password, username, bio, image);
    return userRepository.save(user);
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow();
  }

  public User getById(Long id) {
    return userRepository.findById(id).orElseThrow();
  }

  public UserResponse login(String email, String password) {
    Optional<User> maybeUser = userRepository.findByEmail(email);
    User user = maybeUser.orElseThrow(() -> new IllegalArgumentException("이메일이나 비밀번호가 잘못되었습니다"));
    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException("이메일이나 비밀번호가 잘못되었습니다");
    }

    String jwt = this.jwt.createToken(Jwt.Claims.from(user.getId(), email));

    return UserResponse.from(user, jwt);
  }
}
