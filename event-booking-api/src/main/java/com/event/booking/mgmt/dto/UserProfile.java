package com.event.booking.mgmt.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private String name;
    private String email;
}
