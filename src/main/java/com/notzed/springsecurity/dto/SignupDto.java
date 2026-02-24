package com.notzed.springsecurity.dto;

import com.notzed.springsecurity.entity.enums.Permission;
import com.notzed.springsecurity.entity.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignupDto {
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    private Set<Permission> permissions;
}
