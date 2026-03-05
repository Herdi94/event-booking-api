package com.event.booking.mgmt.controller;

import com.event.booking.mgmt.dto.BookingCreateResponse;
import com.event.booking.mgmt.service.BookingService;
import com.event.booking.mgmt.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/booking")
@RestController
public class BookingMgmtController {

    private final BookingService bookingService;

    public BookingMgmtController(BookingService bookingService){
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create (
            @RequestParam Integer idEvent,
            @RequestParam Integer numberTickets,
            Authentication authentication){

        BookingCreateResponse response = bookingService.create(idEvent, numberTickets, authentication);
        return ResponseUtil.buildSuccessResponse(response, "Successfully create booking");
    }
}
