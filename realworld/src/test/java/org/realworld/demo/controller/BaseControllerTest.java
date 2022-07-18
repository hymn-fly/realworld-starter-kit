package org.realworld.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.transaction.Transactional;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.user.repository.UserRepository;
import org.realworld.demo.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureRestDocs
@Transactional
public abstract class BaseControllerTest {

  protected MockMvc mvc;

  @Autowired
  UserRepository userRepository;

  @Autowired
  Jwt jwt;

  protected String token;

  protected final ObjectMapper objectMapper = new ObjectMapper();

  final User user = new User("example@jake.jake", "jakejake", "Jacob", "", "");

  protected void 유저_등록(String username, String password, String email) throws Exception {
    ObjectNode node = objectMapper.createObjectNode();
    ObjectNode user = node.putObject("user");
    user.put("username", username);
    user.put("email", email);
    user.put("password", password);

    mvc.perform(post("/api/users")
            .content(node.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  protected void 로그인(String email, String password)
      throws Exception {
    ObjectNode node = objectMapper.createObjectNode();
    ObjectNode userNode = node.putObject("user");
    userNode.put("email", email);
    userNode.put("password", password);

    // When
    // Then
    MvcResult mvcResult = mvc.perform(post("/api/users/login")
            .content(node.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    String responseBody = mvcResult.getResponse().getContentAsString();
    JsonNode jsonNode = objectMapper.readTree(responseBody);
    token = jsonNode.get("user").get("token").asText();
  }
}
