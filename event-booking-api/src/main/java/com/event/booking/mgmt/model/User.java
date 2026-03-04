package com.event.booking.mgmt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER")
    private Integer idUser;

    @NotBlank(message = "Name must not be empty.")
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email must not be empty.")
    @Email(message = "Please enter a valid email format.")
    @Column(name = "EMAIL", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Password must not be empty.")
    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @CreationTimestamp
    @Column(name = "CREATE_DATE", updatable = false)
    private LocalDateTime createDate;
}
