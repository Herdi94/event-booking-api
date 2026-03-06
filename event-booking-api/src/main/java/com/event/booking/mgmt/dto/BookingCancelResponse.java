package com.event.booking.mgmt.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCancelResponse {
    private Integer idBooking;
    private String  titleEvent;
    private Integer numberTickets;
    private String  bookingStatus;
}
