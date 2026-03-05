package com.event.booking.mgmt.service.impl;

import com.event.booking.mgmt.config.JwtTokenUtil;
import com.event.booking.mgmt.dto.*;
import com.event.booking.mgmt.exception.DuplicateResourceException;
import com.event.booking.mgmt.exception.ResourceNotFoundException;
import com.event.booking.mgmt.model.User;
import com.event.booking.mgmt.repository.UserRepository;
import com.event.booking.mgmt.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil){
        this.userRepository   = userRepository;
        this.passwordEncoder  = passwordEncoder;
        this.jwtTokenUtil     = jwtTokenUtil;
    }

    @Override
    public UserRegistrationResponse userRegistration(UserRegistrationRequest request){

        String name        = request.getName();
        String email       = request.getEmail();
        String password    = request.getPassword();
        String encryptPass = passwordEncoder.encode(password);

        if(userRepository.existsByEmail(email)){
            throw new DuplicateResourceException("Email already registered");
        }

        User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(encryptPass)
                .build();
        userRepository.save(user);
        return new UserRegistrationResponse(name, email);
    }

    @Override
    public UserLoginResponse userLogin(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Email not found."));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Password does not match.");

        String jwtToken = jwtTokenUtil.generateToken(user);
        return new UserLoginResponse(jwtToken);
    }

    @Override
    public UserProfileResponse userProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found."));

        return UserProfileResponse
                .builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
