package com.event.booking.mgmt.service;

import com.event.booking.mgmt.dto.BookingCancelResponse;
import com.event.booking.mgmt.dto.BookingCreateResponse;
import com.event.booking.mgmt.dto.BookingResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface BookingService {
    public BookingCreateResponse create (Integer idEvent, Integer numberTickets, Authentication authentication);
    public List<BookingResponse> getBooking(String email);
    public BookingCancelResponse cancelBooking(Integer idBooking, String email);
}
