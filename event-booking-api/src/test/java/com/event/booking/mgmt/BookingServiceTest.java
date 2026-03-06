package com.event.booking.mgmt;

import com.event.booking.mgmt.dto.BookingCreateResponse;
import com.event.booking.mgmt.exception.DuplicateResourceException;
import com.event.booking.mgmt.model.Booking;
import com.event.booking.mgmt.model.Event;
import com.event.booking.mgmt.model.User;
import com.event.booking.mgmt.repository.BookingRepository;
import com.event.booking.mgmt.repository.EventRepository;
import com.event.booking.mgmt.repository.UserRepository;
import com.event.booking.mgmt.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private User user;

    @Mock
    private Event event;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setup(){

        user = new User();
        user.setIdUser(1);
        user.setName("Herdi");
        user.setEmail("herdi@gmail.com");

        event = new Event();
        event.setIdEvent(1);
        event.setTitle("Learn basic oracle to expert");
        event.setAvailableSeats(50);
        event.setPrice(BigDecimal.valueOf(90000));
        event.setEventDate(LocalDateTime.now());

    }

    @Test
    void createBookingTest(){

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("herdi@gmail.com");
        when(userRepository.findByEmail("herdi@gmail.com")).thenReturn(Optional.of(user));
        when(eventRepository.findByIdEventAndIsActiveTrue(1)).thenReturn(Optional.of(event));
        when(bookingRepository.findByUserIdUserAndEventIdEvent(1,1)).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        BookingCreateResponse response = bookingService.create(1, 2, authentication);

        assertNotNull(response);
        assertEquals("Herdi", response.getUserName());
        assertEquals(2, response.getNumberTickets());
        assertEquals(BigDecimal.valueOf(180000), response.getTotalPrice());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void checkDuplicateBooking(){
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn("herdi@gmail.com");

            when(userRepository.findByEmail("herdi@gmail.com")).thenReturn(Optional.of(user));
            when(eventRepository.findByIdEventAndIsActiveTrue(1)).thenReturn(Optional.of(event));

            when(bookingRepository.findByUserIdUserAndEventIdEvent(1, 1)).thenReturn(Optional.of(new Booking()));

            assertThrows(DuplicateResourceException.class, () -> bookingService.create(1, 2, authentication));
    }

    @Test
    void checkAvailability(){
        event.setAvailableSeats(50);
        assertDoesNotThrow(() ->
        {
            int ticketRequested = 3;
            if (event.getAvailableSeats() < ticketRequested)
               throw new IllegalArgumentException("Not enough seat available");
        });
    }

}
