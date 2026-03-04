package com.event.booking.mgmt.controller;

import com.event.booking.mgmt.dto.*;
import com.event.booking.mgmt.service.UserService;
import com.event.booking.mgmt.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user")
@RestController
public class UserMgmtController {

    private final UserService userService;

    public UserMgmtController(UserService userService){
        this.userService  = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Valid UserRegistrationRequest request){
            UserRegistrationResponse response = userService.userRegistration(request);
            return ResponseUtil.buildSuccessResponse(response, "Successfully registration.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequest request){
            UserLoginResponse response = userService.userLogin(request);
            return ResponseUtil.buildSuccessResponse(response, "Successfully login.");
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication){
            String email = authentication.getName();
            UserProfileResponse response = userService.userProfile(email);
            return ResponseUtil.buildSuccessResponse(response, "Successfully get profile.");
    }
}
