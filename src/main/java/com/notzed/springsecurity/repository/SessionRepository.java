package com.notzed.springsecurity.repository;

import com.notzed.springsecurity.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    Optional<SessionEntity> findByUserId(Long userId);

    Optional<SessionEntity> findByToken(String token);

    void deleteByUserId(Long userId);
}
