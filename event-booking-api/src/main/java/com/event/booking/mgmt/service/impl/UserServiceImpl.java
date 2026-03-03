package com.event.booking.mgmt.service.impl;

import com.event.booking.mgmt.dto.UserRegistrationRequest;
import com.event.booking.mgmt.dto.UserRegistrationResponse;
import com.event.booking.mgmt.model.User;
import com.event.booking.mgmt.repository.UserRepository;
import com.event.booking.mgmt.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserRegistrationResponse userRegistration(UserRegistrationRequest request) throws Exception {

        String name        = request.getName();
        String email       = request.getEmail();
        String password    = request.getPassword();
        String encryptPass = passwordEncoder.encode(password);

        User user = User.builder()
                    .name(name)
                    .email(email)
                    .password(encryptPass)
                .build();
        userRepository.save(user);
        return new UserRegistrationResponse(name, email);
    }
}
