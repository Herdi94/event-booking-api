package com.event.booking.mgmt.controller;

import com.event.booking.mgmt.dto.UserRegistrationRequest;
import com.event.booking.mgmt.dto.UserRegistrationResponse;
import com.event.booking.mgmt.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
