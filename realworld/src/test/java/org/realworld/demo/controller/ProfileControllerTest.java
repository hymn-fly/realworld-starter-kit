package org.realworld.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.jwt.Jwt;

class ProfileControllerTest extends RestDocsSupportTest {

  private final User followee = new User("followee@gmail.com", "1234", "followee", "I'm a followee",
      "https://aaabbb.jpg");

  @BeforeEach
  void setup() {
    userRepository.save(user);
    userRepository.save(followee);
  }

  @Test
  @DisplayName("[GET] /api/profiles/{username} w/o login")
  void test1() throws Exception {
    //Given

    // When
    // Then
    mvc.perform(get("/api/profiles/{username}", followee.getUsername()))
        .andExpect(jsonPath("$.profile.username").value(followee.getUsername()))
        .andExpect(jsonPath("$.profile.image").value(followee.getImage()))
        .andExpect(jsonPath("$.profile.bio").value(followee.getBio()))
        .andExpect(jsonPath("$.profile.following").value(false))
        .andDo(print());
  }

  @Test
  @DisplayName("[GET] /api/profiles/{username} with login")
  void test2() throws Exception {
    //Given
    String token = jwt.createToken(Jwt.Claims.from(user.getId(), user.getEmail()));

    // When
    // Then
    mvc.perform(get("/api/profiles/{username}", followee.getUsername())
            .header("Authorization", "Bearer " + token))
        .andExpect(jsonPath("$.profile.username").value(followee.getUsername()))
        .andExpect(jsonPath("$.profile.image").value(followee.getImage()))
        .andExpect(jsonPath("$.profile.bio").value(followee.getBio()))
        .andExpect(jsonPath("$.profile.following").value(false))
        .andDo(print());
  }


  @Test
  @DisplayName("[POST] /api/profiles/{username}/follow")
  void test3() throws Exception {
    //Given
    String token = jwt.createToken(Jwt.Claims.from(user.getId(), user.getEmail()));

    // When
    // Then
    mvc.perform(post("/api/profiles/{username}/follow", followee.getUsername())
            .header("Authorization", "Bearer " + token))
        .andExpect(jsonPath("$.profile.username").value(followee.getUsername()))
        .andExpect(jsonPath("$.profile.image").value(followee.getImage()))
        .andExpect(jsonPath("$.profile.bio").value(followee.getBio()))
        .andExpect(jsonPath("$.profile.following").value(true))
        .andDo(print());
  }

  @Test
  @DisplayName("[POST] /api/profiles/{username}/follow w/o login -> Access Denied")
  void test3_2() throws Exception {
    //Given

    // When
    // Then
    mvc.perform(post("/api/profiles/{username}/follow", followee.getUsername()))
        .andExpect(status().is(403))
        .andExpect(status().reason("Access Denied"))
        .andDo(print());

  }


}