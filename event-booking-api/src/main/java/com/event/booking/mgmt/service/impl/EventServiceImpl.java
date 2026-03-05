package com.event.booking.mgmt.service.impl;

import com.event.booking.mgmt.dto.*;
import com.event.booking.mgmt.exception.AccessDeniedException;
import com.event.booking.mgmt.exception.ResourceNotFoundException;
import com.event.booking.mgmt.model.Event;
import com.event.booking.mgmt.model.User;
import com.event.booking.mgmt.repository.EventRepository;
import com.event.booking.mgmt.repository.UserRepository;
import com.event.booking.mgmt.service.EventService;
import com.event.booking.mgmt.specification.EventSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository){
        this.eventRepository = eventRepository;
        this.userRepository  = userRepository;
    }

    @Override
    public EventCreateResponse create(EventCreateRequest request, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found."));

        Event event = Event.builder()
                      .title(request.getTitle())
                      .description(request.getDescription())
                      .location(request.getLocation())
                      .eventDate(request.getEventDate())
                      .availableSeats(request.getAvailableSeats())
                      .price(request.getPrice())
                      .createBy(user)
                      .build();
        Event saveEvent = eventRepository.save(event);

        return EventCreateResponse.builder()
                .idEvent(saveEvent.getIdEvent())
                .title(saveEvent.getTitle())
                .description(saveEvent.getDescription())
                .location(saveEvent.getLocation())
                .eventDate(saveEvent.getEventDate())
                .availableSeats(saveEvent.getAvailableSeats())
                .price(saveEvent.getPrice())
                .createBy(saveEvent.getCreateBy().getName())
                .createDate(saveEvent.getCreateDate())
                .build();
    }

    @Override
    public Map<String, Object> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = eventRepository.findByEventDateAfter(LocalDateTime.now(), pageable);

        List<EventUpcomingResponse> content = eventsPage.stream()
                .map(e -> {
                  return EventUpcomingResponse.builder()
                            .idEvent(e.getIdEvent())
                            .title(e.getTitle())
                            .eventDate(e.getEventDate())
                            .availableSeats(e.getAvailableSeats())
                            .price(e.getPrice())
                            .build();
                }).toList();

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", eventsPage.getTotalPages());
        pagination.put("size", eventsPage.getSize());
        pagination.put("totalElements", eventsPage.getTotalElements());
        pagination.put("totalPages", eventsPage.getTotalPages());
        pagination.put("content", content);

        return pagination;
    }

    @Override
    public EventDetailResponse getEventDetail(Integer idEvent) {
       Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found."));

        //calculate ticket sold
        int ticketBooked = 10;

        return EventDetailResponse.builder()
                .idEvent(event.getIdEvent())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .eventDate(event.getEventDate())
                .availableSeats(event.getAvailableSeats())
                .currentAvailableSeats(ticketBooked)
                .price(event.getPrice())
                .createBy(event.getCreateBy().getName())
                .createDate(event.getCreateDate())
                .build();
    }

    @Override
    public EventUpdateResponse updateEvent(Integer idEvent, EventUpdateRequest request, String email) {
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        if(!event.getCreateBy().getEmail().equals(email))
            throw new AccessDeniedException("You are not allowed to update this event");

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setLocation(request.getLocation());
        event.setEventDate(request.getEventDate());
        event.setAvailableSeats(request.getAvailableSeats());
        event.setPrice(request.getPrice());
        event.setUpdateBy(user);
        eventRepository.flush();

        int ticketBooked = 10; //need relation to model booking

        return EventUpdateResponse.builder()
                .idEvent(event.getIdEvent())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .eventDate(event.getEventDate())
                .availableSeats(event.getAvailableSeats())
                .currentAvailableSeats(event.getAvailableSeats() - ticketBooked)
                .price(event.getPrice())
                .updateBy(event.getUpdateBy().getName())
                .updateDate(event.getUpdateDate())
                .build();
    }

    @Override
    public void deleteEvent(Integer idEvent, String email) {
        Event event = eventRepository.findByIdEventAndIsActiveTrue(idEvent)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if(!event.getCreateBy().getEmail().equals(email))
            throw new AccessDeniedException("You are not allowed to delete this event");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not found."));

        event.setIsActive(false);
        event.setUpdateBy(user);
        eventRepository.save(event);
    }

    @Override
    public Map<String, Object> searchEvent(String title, String location, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(
                page, size, Sort.by("eventDate").ascending()
        );

        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end   = endDate != null ? endDate.atTime(23, 59, 59) : null;

        Specification<Event> spec = EventSpecification
                .filterEvents(title, location, start, end);

        Page<Event> events = eventRepository.findAll(spec, pageable);

        int ticketBooked = 10; //bookingRepository.countByEventId(event.getId());

        List<EventSearchResponse> content = events.stream()
                .map(e -> {
                    return EventSearchResponse.builder()
                            .idEvent(e.getIdEvent())
                            .title(e.getTitle())
                            .description(e.getDescription())
                            .location(e.getLocation())
                            .eventDate(e.getEventDate())
                            .availableSeats(e.getAvailableSeats())
                            .currentAvailableSeats(e.getAvailableSeats() - ticketBooked)
                            .price(e.getPrice())
                            .build();
                }).toList();

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", events.getTotalPages());
        pagination.put("size", events.getSize());
        pagination.put("totalElements", events.getTotalElements());
        pagination.put("totalPages", events.getTotalPages());
        pagination.put("content", content);

        return pagination;
    }
}
