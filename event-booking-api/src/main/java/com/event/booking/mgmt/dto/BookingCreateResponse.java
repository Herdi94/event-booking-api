package com.event.booking.mgmt.dto;

import com.event.booking.mgmt.model.Event;
import com.event.booking.mgmt.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateResponse {

    private Integer idBooking;
    private String userName;
    private String eventTitle;
    private Integer numberTickets;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(type = "string")
    private LocalDateTime createDate;
}
