package com.event.booking.mgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateResponse {

    private Integer idEvent;
    private String title;
    private String description;
    private String location;
    private LocalDateTime eventDate;
    private Integer availableSeats;
    private BigDecimal price;
    private String createBy;
    private LocalDateTime createDate;

}
