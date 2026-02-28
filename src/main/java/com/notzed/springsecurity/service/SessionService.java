package com.notzed.springsecurity.service;

import com.notzed.springsecurity.entity.SessionEntity;
import com.notzed.springsecurity.entity.User;
import com.notzed.springsecurity.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final int SESSION_LIMIT = 2;


    public void generateNewSession(User user, String refreshToken){
        long activeSessions = sessionRepository.countByUser(user);
        if(activeSessions >= user.sessionLimitCounter()){
            SessionEntity leastRecentlyUsedSession = (SessionEntity) sessionRepository.findTopByUserOrderByLastUsedAtAsc(user)
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            sessionRepository.delete(leastRecentlyUsedSession);
        }

        SessionEntity newSession = SessionEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(newSession);
    }

    public void validateSession(String refreshToken){
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    public void deleteSession(String refreshToken){
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Session not found for refreshToken: "+refreshToken));
        sessionRepository.delete(session);

    }
}
