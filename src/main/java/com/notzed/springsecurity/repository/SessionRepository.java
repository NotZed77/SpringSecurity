package com.notzed.springsecurity.repository;

import com.notzed.springsecurity.entity.SessionEntity;
import com.notzed.springsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    List<SessionEntity> findByUser(User user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
