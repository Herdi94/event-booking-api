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
public class EventUpcomingResponse {

    private Integer idEvent;
    private String title;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(type = "string")
    private LocalDateTime eventDate;

    private Integer availableSeats;
    private BigDecimal price;

}
