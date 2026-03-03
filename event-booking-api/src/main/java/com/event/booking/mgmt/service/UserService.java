package com.event.booking.mgmt.service;

import com.event.booking.mgmt.dto.*;

public interface UserService {
    public UserRegistrationResponse userRegistration(UserRegistrationRequest request) throws Exception;
    public UserLoginResponse userLogin(UserLoginRequest request) throws Exception;
    public UserProfile userProfile(String email) throws Exception;
}
