package com.event.booking.mgmt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class EventUpdateRequest {

    @NotBlank(message = "Title must not be empty.")
    private String title;

    @NotBlank(message = "Description must not be empty.")
    private String description;

    @NotBlank(message = "Location must not be empty.")
    private String location;

    @NotNull(message = "Event date must not be empty.")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(type = "string")
    private LocalDateTime eventDate;

    @NotNull(message = "Available seats must not be empty.")
    @PositiveOrZero
    private Integer availableSeats;

    @NotNull(message = "Price must not be empty.")
    @PositiveOrZero
    private BigDecimal price;

}
