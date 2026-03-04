package com.event.booking.mgmt.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequest {

    @NotBlank(message = "Title must not be empty.")
    private String title;

    @NotBlank(message = "Description must not be empty.")
    private String description;

    @NotBlank(message = "Location must not be empty.")
    private String location;

    @NotNull(message = "Event date must not be empty.")
    private LocalDateTime eventDate;

    @NotNull(message = "Available seats must not be empty.")
    @PositiveOrZero
    private Integer availableSeats;

    @NotNull(message = "Price must not be empty.")
    @PositiveOrZero
    private BigDecimal price;

}
