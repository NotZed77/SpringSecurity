package com.notzed.springsecurity.controller;

import com.notzed.springsecurity.dto.LoginDto;
import com.notzed.springsecurity.dto.LoginResponseDto;
import com.notzed.springsecurity.dto.SignupDto;
import com.notzed.springsecurity.dto.UserDto;
import com.notzed.springsecurity.service.AuthService;
import com.notzed.springsecurity.service.SessionService;
import com.notzed.springsecurity.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    private final SessionService sessionService;

    @Value("${deploy.env}")
    private String deployEnv;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupDto signupDto){
        UserDto userDto = userService.signUp(signupDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
                                        HttpServletResponse response){
        DeleteRecentRefreshToken(request, response);

        LoginResponseDto loginResponseDto = authService.login(loginDto);

        Cookie cookie = new Cookie("refreshToken", loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies == null) throw new AuthenticationServiceException("Refresh token not found inside the cookie");

        String refreshToken =  Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException
                        ("Refresh token not found inside the Cookies"));
        DeleteRecentRefreshToken(request, response);
        LoginResponseDto loginResponseDto = authService.refreshToken(refreshToken);

        Cookie expiredCookie = new Cookie("refreshToken",loginResponseDto.getRefreshToken());
        expiredCookie.setMaxAge(0);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        response.addCookie(expiredCookie);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            throw  new AuthenticationServiceException("Refresh token not found inside the cookie");
        }
        String refreshToken =  Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException
                        ("Refresh token not found inside the Cookies"));
        authService.logout(refreshToken);

        Cookie expiredCookie = new Cookie("refreshToken",null);
        expiredCookie.setMaxAge(0);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        response.addCookie(expiredCookie);
        return ResponseEntity.noContent().build();
    }

    public void DeleteRecentRefreshToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;
        Optional<String> refreshToken =  Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue);

        if(refreshToken.isEmpty()) return;
        authService.logout(refreshToken.get());

        Cookie expiredCookie = new Cookie("refreshToken",null);
        expiredCookie.setMaxAge(0);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        response.addCookie(expiredCookie);
    }

//    @PostMapping("/session")
//    public ResponseEntity<?> session(@RequestBody SessionEntity session, HttpServletRequest request, HttpServletResponse response){
//        String token = authService.loggingToken(session.getUserId());
//        return ResponseEntity.ok(token);
//    }



}
