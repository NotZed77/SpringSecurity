package com.notzed.springsecurity.dto;

import lombok.Data;

@Data
public class SignupDto {
    private String email;
    private String password;
    private String name;
}
