package com.event.booking.mgmt.service.impl;

import com.event.booking.mgmt.dto.BookingCancelResponse;
import com.event.booking.mgmt.dto.BookingCreateResponse;
import com.event.booking.mgmt.dto.BookingResponse;
import com.event.booking.mgmt.dto.EventDetailResponse;
import com.event.booking.mgmt.enums.BookingStatus;
import com.event.booking.mgmt.exception.AccessDeniedException;
import com.event.booking.mgmt.exception.DuplicateResourceException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        bookingRepository.findByUserIdUserAndEventIdEvent(user.getIdUser(), event.getIdEvent())
                .ifPresent(a -> {
                    throw new DuplicateResourceException("User has already booked this event");
                });

        Booking booking = Booking.builder()
                            .bookingReference(generateBookingReference())
                            .user(user)
                            .event(event)
                            .numberTickets(numberTickets)
                            .build();

        bookingRepository.save(booking);

        return BookingCreateResponse.builder()
                .idBooking(booking.getIdBooking())
                .bookingReference(booking.getBookingReference())
                .userName(user.getName())
                .eventTitle(event.getTitle())
                .numberTickets(numberTickets)
                .totalPrice(BigDecimal.valueOf(numberTickets).multiply(event.getPrice()))
                .createDate(booking.getCreateDate())
                .build();
    }

    private String generateBookingReference() {
        return "BOOKED-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }

    @Override
    public List<BookingResponse> getBooking(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        List<Booking> bookings = bookingRepository.findByUser(user);
        if (bookings.isEmpty())
            throw new ResourceNotFoundException("Booking not found");

        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (Booking booking : bookings) {

            Event event = eventRepository.findById(booking.getEvent().getIdEvent())
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            if (!booking.getUser().getEmail().equals(email))
                throw new AccessDeniedException("You are not allowed to update this booking");

            int ticketBooked = bookingRepository.countTicketBookedByIdEvent(event.getIdEvent());

            EventDetailResponse eventDetail = EventDetailResponse.builder()
                    .idEvent(event.getIdEvent())
                    .title(event.getTitle())
                    .description(event.getDescription())
                    .location(event.getLocation())
                    .eventDate(event.getEventDate())
                    .availableSeats(event.getAvailableSeats() - ticketBooked)
                    .price(event.getPrice())
                    .createBy(event.getCreateBy().getName())
                    .createDate(event.getCreateDate())
                    .build();

            bookingResponses.add(BookingResponse.builder()
                    .idBooking(booking.getIdBooking())
                    .numberTickets(booking.getNumberTickets())
                    .bookingStatus(booking.getIsActive() ? BookingStatus.BOOKED.name() : BookingStatus.CANCELLED.name())
                    .createDate(booking.getCreateDate())
                    .eventDetail(eventDetail)
                    .build());
        }
        return bookingResponses;
    }

    @Override
    public BookingCancelResponse cancelBooking(Integer idBooking, String email) {
        Booking booking = bookingRepository.findById(idBooking)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(!booking.getUser().getEmail().equals(email))
            throw new AccessDeniedException("You are not allowed to update this booking");

        Event event = eventRepository.findById(booking.getEvent().getIdEvent())
                        .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventDate = event.getEventDate();

        if(now.isAfter(eventDate.minusHours(24)))
            throw new IllegalArgumentException("Booking can only be cancelled 24 hours before the event");

        event.setAvailableSeats(event.getAvailableSeats() + booking.getNumberTickets());

        booking.setIsActive(false);
        Booking bookingSave =  bookingRepository.save(booking);

        return BookingCancelResponse.builder()
                .idBooking(bookingSave.getIdBooking())
                .titleEvent(bookingSave.getEvent().getTitle())
                .numberTickets(booking.getNumberTickets())
                .bookingStatus(BookingStatus.CANCELLED.name())
                .build();
    }
}
