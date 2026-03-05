package com.event.booking.mgmt.service;

import com.event.booking.mgmt.dto.*;

public interface UserService {
    public UserRegistrationResponse userRegistration(UserRegistrationRequest request);
    public UserLoginResponse userLogin(UserLoginRequest request);
    public UserProfileResponse userProfile(String email);
}
