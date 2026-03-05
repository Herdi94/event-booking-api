package com.event.booking.mgmt.service;

import com.event.booking.mgmt.dto.BookingCreateResponse;
import org.springframework.security.core.Authentication;

public interface BookingService {
    public BookingCreateResponse create (Integer idEvent, Integer numberTickets, Authentication authentication);
}
