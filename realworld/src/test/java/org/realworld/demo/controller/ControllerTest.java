package org.realworld.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.realworld.demo.domain.User;
import org.realworld.demo.jwt.JwtUtil;
import org.realworld.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    final String email = "example@jake.jake";

    final String password = "jakejake";

    final String username = "Jacob";


}
