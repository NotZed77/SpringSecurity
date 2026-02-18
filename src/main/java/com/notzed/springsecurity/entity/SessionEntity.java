package com.notzed.springsecurity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "session_entity")
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false, unique = true)
    private Long userId;

    @Column(name = "token",nullable = false, unique = true)
    private String token;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public SessionEntity(Long userId, String token){
        this.userId = userId;
        this.token = token;
        this.createdAt = LocalDateTime.now();
    }
}
