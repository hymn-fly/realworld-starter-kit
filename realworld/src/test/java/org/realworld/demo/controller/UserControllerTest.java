package org.realworld.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class UserControllerTest extends BaseControllerTest {

  @Test
  void 로그인_성공() throws Exception {
    //Given
    유저_등록("Jacob", "jakejake", "example@jake.jake");
    ObjectNode node = objectMapper.createObjectNode();
    ObjectNode userNode = node.putObject("user");
    userNode.put("email", "example@jake.jake");
    userNode.put("password", "jakejake");

    // When
    // Then
    mvc.perform(post("/api/users/login")
            .content(node.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.user.email").value("example@jake.jake"))
        .andExpect(jsonPath("$.user.username").value("Jacob"))
        .andExpect(jsonPath("$.user.token").isNotEmpty())
        .andDo(print());
  }

  @Test
  void 비밀번호_불일치로인한_로그인_실패() throws Exception {
    //Given
    유저_등록("Jacob", "jakejake", "example@jake.jake");
    ObjectNode node = objectMapper.createObjectNode();
    ObjectNode userNode = node.putObject("user");
    userNode.put("email", "example@jake.jake");
    userNode.put("password", "wrongpassword");

    // When
    // Then
    mvc.perform(post("/api/users/login")
            .content(node.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
  }

  @Test
  void 유저_등록_성공() throws Exception {
    //Given
    ObjectNode node = objectMapper.createObjectNode();
    ObjectNode user = node.putObject("user");
    user.put("username", "testuser");
    user.put("email", "testuser@email.com");
    user.put("password", "1111");

    // When
    // Then
    mvc.perform(post("/api/users")
            .content(node.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.user.email").value("testuser@email.com"))
        .andExpect(jsonPath("$.user.username").value("testuser"))
        .andExpect(jsonPath("$.user.token").isEmpty())
        .andDo(print());
  }

  @Test
  void 현재_유저_조회_성공() throws Exception {
    //Given
    String username = "Jacob";
    String password = "jakejake";
    String email = "example@jake.jake";
    유저_등록(username, password, email);
    로그인(email, password);

    // When
    // Then
    mvc.perform(get("/api/user")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
        .andExpect(jsonPath("$.user.email").value(email))
        .andExpect(jsonPath("$.user.username").value(username))
        .andExpect(jsonPath("$.user.token").isNotEmpty())
        .andDo(print());
  }

  @Test
  void 유저_업데이트_성공() throws Exception {
    String originEmail = "example@jake.jake";
    String originUsername = "Jacob";

    유저_등록(originUsername, "jakejake", originEmail);
    로그인(originEmail, "jakejake");
    //Given
    ObjectNode node = objectMapper.createObjectNode();
    ObjectNode userNode = node.putObject("user");

    String updatedEmail = "update@yahoo.com";
    String updatedBio = "update~~";
    String updatedImage = "http://update-image";

    userNode.put("email", updatedEmail);
    userNode.put("bio", updatedBio);
    userNode.put("image", updatedImage);

    // When
    // Then
    mvc.perform(put("/api/user")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .content(node.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.user.email").value(updatedEmail))
        .andExpect(jsonPath("$.user.username").value(originUsername))
        .andExpect(jsonPath("$.user.bio").value(updatedBio))
        .andExpect(jsonPath("$.user.image").value(updatedImage))
        .andDo(print());
  }
}