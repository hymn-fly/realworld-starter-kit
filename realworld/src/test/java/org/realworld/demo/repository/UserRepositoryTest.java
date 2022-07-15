package org.realworld.demo.repository;

import org.junit.jupiter.api.Test;
import org.realworld.demo.domain.user.entity.User;
import org.realworld.demo.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void test() {
        User user = new User("test@gmail.com", "111", "test", "ggg", "aaaa/asdf");
        userRepository.save(user);
        System.out.println(userRepository.findAll());
    }
}