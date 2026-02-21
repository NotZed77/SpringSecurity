package com.notzed.springsecurity;

import com.notzed.springsecurity.entity.User;
import com.notzed.springsecurity.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringsecurityApplicationTests {

    @Autowired
    private JwtService jwtService;

    @Test
    void contextLoads() {
//
//        User user = new User(4L, "alistar@gmail.com","1234", "Alistar");
//
//        String token = jwtService.generateToken(user);
//
//        System.out.println(token);
//
//        Long id = jwtService.getUserIdFromToken(token);
//
//        System.out.println(id);
    }


}
