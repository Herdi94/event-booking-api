package com.event.booking.mgmt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {
    private String jwtToken;
}
