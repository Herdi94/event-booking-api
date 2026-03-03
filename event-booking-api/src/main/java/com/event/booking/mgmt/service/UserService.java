package com.event.booking.mgmt.service;

import com.event.booking.mgmt.dto.UserRegistrationRequest;
import com.event.booking.mgmt.dto.UserRegistrationResponse;

public interface UserService {
    public UserRegistrationResponse userRegistration(UserRegistrationRequest request) throws Exception;
}
