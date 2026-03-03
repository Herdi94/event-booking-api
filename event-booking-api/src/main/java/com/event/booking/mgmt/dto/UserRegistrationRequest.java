package com.event.booking.mgmt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    @NotBlank(message = "Name must not be empty.")
    private String name;

    @NotBlank(message = "Email must not be empty.")
    @Email(message = "Please enter a valid email format.")
    private String email;

    @NotBlank(message = "Password must not be empty.")
    private String password;
}
