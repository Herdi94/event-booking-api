package com.event.booking.mgmt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateResponse {

    private Integer idBooking;
    private String bookingReference;
    private String userName;
    private String eventTitle;
    private Integer numberTickets;
    private BigDecimal totalPrice;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(type = "string")
    private LocalDateTime createDate;
}
