package com.event.booking.mgmt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Integer idBooking;
    private Integer numberTickets;
    private String  bookingStatus;
    private EventDetailResponse eventDetail;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(type = "string")
    private LocalDateTime createDate;
}
