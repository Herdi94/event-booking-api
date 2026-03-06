package com.event.booking.mgmt;

import com.event.booking.mgmt.controller.UserMgmtController;
import com.event.booking.mgmt.dto.UserProfileResponse;
import com.event.booking.mgmt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private Authentication authentication;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserMgmtController userMgmtController;

    @Test
    void getProfile() {
        String email = "herdi@gmail.com";
        UserProfileResponse response = new UserProfileResponse();

        when(authentication.getName()).thenReturn(email);
        when((userService.userProfile(email))).thenReturn(response);

        ResponseEntity<?> result = userMgmtController.getProfile(authentication);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        log.info("Result: {}", result.getBody());

    }
}
