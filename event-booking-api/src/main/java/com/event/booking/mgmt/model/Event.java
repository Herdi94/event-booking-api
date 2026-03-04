package com.event.booking.mgmt.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "TB_EVENT")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EVENT")
    private Integer idEvent;

    @NotBlank(message = "Title must not be empty.")
    @Column(name = "TITLE", nullable = false, length = 150)
    private String title;

    @NotBlank(message = "Description must not be empty.")
    @Column(name = "DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Location must not be empty.")
    @Column(name = "LOCATION", nullable = false, length = 200)
    private String location;

    @NotNull(message = "Event date must not be empty.")
    @Column(name = "EVENT_DATE", nullable = false)
    private LocalDateTime eventDate;

    @NotNull(message = "Available seats must not be empty.")
    @PositiveOrZero
    @Column(name = "AVAILABLE_SEATS", nullable = false)
    private Integer availableSeats;

    @NotNull(message = "Price must not be empty.")
    @PositiveOrZero
    @Column(name = "PRICE", nullable = false, precision = 15, scale = 0)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_BY", nullable = false)
    private User createBy;

    @Column(name = "CREATE_DATE", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPDATE_BY")
    private User updateBy;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist(){
        this.createDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updateDate = LocalDateTime.now();
    }
}
