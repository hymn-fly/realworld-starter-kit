package org.realworld.demo.domain.user.service;

import java.util.Optional;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public User updateUser(User originUser, String email, String username, String password,
      String image, String bio) {
    User updatedUser = originUser.update(email, password, username, bio, image);
    return userRepository.save(updatedUser);
  }

  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow();
  }

  public User getById(Long id) {
    return userRepository.findById(id).orElseThrow();
  }


  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email).orElseThrow();
  }

  public User login(String email, String password) {
    Optional<User> maybeUser = userRepository.findByEmail(email);
    User user = maybeUser.orElseThrow();
    if (!user.getPassword().equals(password)) {
      throw new IllegalArgumentException();
    }
    return user;
  }
}
