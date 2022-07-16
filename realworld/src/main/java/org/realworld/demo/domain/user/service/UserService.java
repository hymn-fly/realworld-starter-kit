package org.realworld.demo.domain.user.service;

import org.realworld.demo.domain.user.entity.User;

public interface UserService {

  User saveUser(User user);

  User updateUser(User originUser, String email, String username,
      String password, String image, String bio);

  User getUserByUsername(String username);

  User login(String email, String password);

}
