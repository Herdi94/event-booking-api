package com.event.booking.mgmt.service.impl;

import com.event.booking.mgmt.dto.BookingCreateResponse;
import com.event.booking.mgmt.dto.BookingResponse;
import com.event.booking.mgmt.dto.EventDetailResponse;
import com.event.booking.mgmt.exception.AccessDeniedException;
import com.event.booking.mgmt.exception.ResourceNotFoundException;
import com.event.booking.mgmt.model.Booking;
import com.event.booking.mgmt.model.Event;
import com.event.booking.mgmt.model.User;
import com.event.booking.mgmt.repository.BookingRepository;
import com.event.booking.mgmt.repository.EventRepository;
import com.event.booking.mgmt.repository.UserRepository;
import com.event.booking.mgmt.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(UserRepository userRepository,
                              EventRepository eventRepository,
                              BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingCreateResponse create(Integer idEvent, Integer numberTickets, Authentication authentication) {

        if(authentication == null || !authentication.isAuthenticated())
            throw new AccessDeniedException("User must be authenticated.");

        String email = authentication.getName();
        User user    = userRepository.findByEmail(email)
                    .orElseThrow(()->new ResourceNotFoundException("Email not found"));

        Event event  = eventRepository.findByIdEventAndIsActiveTrue(idEvent)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if(event.getAvailableSeats() < numberTickets)
            throw new IllegalArgumentException("Not enough seats available");

        Booking booking = Booking.builder()
                            .user(user)
                            .event(event)
                            .numberTickets(numberTickets)
                            .build();

        bookingRepository.save(booking);

        return BookingCreateResponse.builder()
                .idBooking(booking.getIdBooking())
                .userName(user.getName())
                .eventTitle(event.getTitle())
                .numberTickets(numberTickets)
                .createDate(booking.getCreateDate())
                .build();
    }

    @Override
    public BookingResponse getBooking(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        Booking booking = bookingRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        Event event = eventRepository.findById(booking.getEvent().getIdEvent())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if(!event.getCreateBy().getEmail().equals(email))
            throw new AccessDeniedException("You are not allowed to update this event");

        int ticketBooked = bookingRepository.countTicketBookedByIdEvent(event.getIdEvent());

        EventDetailResponse eventDetail = EventDetailResponse.builder()
                .idEvent(event.getIdEvent())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .eventDate(event.getEventDate())
                .availableSeats(event.getAvailableSeats())
                .currentAvailableSeats(event.getAvailableSeats() - ticketBooked)
                .price(event.getPrice())
                .createBy(event.getCreateBy().getName())
                .createDate(event.getCreateDate())
                .build();

        return BookingResponse.builder()
                .idBooking(booking.getIdBooking())
                .numberTickets(booking.getNumberTickets())
                .bookingStatus("BOOKED")
                .createDate(booking.getCreateDate())
                .eventDetail(eventDetail)
                .build();
    }
}
