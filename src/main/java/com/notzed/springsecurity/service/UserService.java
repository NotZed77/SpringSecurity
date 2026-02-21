package com.notzed.springsecurity.service;

import com.notzed.springsecurity.dto.LoginDto;
import com.notzed.springsecurity.dto.SignupDto;
import com.notzed.springsecurity.dto.UserDto;
import com.notzed.springsecurity.entity.User;
import com.notzed.springsecurity.exception.ResourceNotFoundException;
import com.notzed.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() ->
                new BadCredentialsException("User with email " + username + "not found"));
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User with id " + userId + "not found"));
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserDto signUp(SignupDto signupDto) {
        Optional<User> user = userRepository.findByEmail(signupDto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("User with email already exists" + signupDto.getEmail());
        }

        User toBeCreatedUser = modelMapper.map(signupDto, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

        User savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDto.class);

    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }
}
