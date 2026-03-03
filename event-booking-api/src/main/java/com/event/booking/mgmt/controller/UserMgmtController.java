package com.event.booking.mgmt.controller;

import com.event.booking.mgmt.dto.*;
import com.event.booking.mgmt.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RequestMapping("/api/user")
@RestController
public class UserMgmtController {

    private final UserService userService;

    public UserMgmtController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationRequest request){
            try{
                UserRegistrationResponse response = userService.userRegistration(request);
                return ResponseEntity.ok(response);
            }catch (Exception e){
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Unexpected Error : "+e.getMessage());
            }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request){
        try{
            UserLoginResponse response = userService.userLogin(request);
            return ResponseEntity.ok(response);
        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error : " + e.getMessage());
        }
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication){
        try {
            String email = authentication.getName();
            UserProfile userProfile = userService.userProfile(email);
            return ResponseEntity.ok(userProfile);
        }catch (IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid Request : " + e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected Error : " + e.getMessage());
        }
    }
}
