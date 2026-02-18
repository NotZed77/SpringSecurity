package com.notzed.springsecurity.service;

import com.notzed.springsecurity.dto.LoginDto;
import com.notzed.springsecurity.entity.SessionEntity;
import com.notzed.springsecurity.entity.User;
import com.notzed.springsecurity.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;

    private final SessionRepository sessionRepository;

    private final AuthenticationManager authenticationManager;

    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }

    public String loggingToken(Long userId){
            sessionRepository.deleteByUserId(userId);
            String newToken = UUID.randomUUID().toString(); // --> Generating NEW TOKEN
            SessionEntity session = new SessionEntity(userId, newToken);
            sessionRepository.save(session);
            return newToken;

    }
}
