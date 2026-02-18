package com.notzed.springsecurity.controller;

import com.notzed.springsecurity.dto.LoginDto;
import com.notzed.springsecurity.dto.SignupDto;
import com.notzed.springsecurity.dto.UserDto;
import com.notzed.springsecurity.entity.SessionEntity;
import com.notzed.springsecurity.service.AuthService;
import com.notzed.springsecurity.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupDto signupDto){
        UserDto userDto = userService.signUp(signupDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
                                        HttpServletResponse response){
        String token = authService.login(loginDto);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(token);
    }



    @PostMapping("/session")
    public ResponseEntity<?> session(@RequestBody SessionEntity session, HttpServletRequest request, HttpServletResponse response){
        String token = authService.loggingToken(session.getUserId());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello, World!";
    }

    @GetMapping("/error")
    public String error(){
        throw new RuntimeException("This is a test exception");
    }

}
