package com.event.booking.mgmt.service.impl;

import com.event.booking.mgmt.dto.EventCreateRequest;
import com.event.booking.mgmt.dto.EventCreateResponse;
import com.event.booking.mgmt.dto.EventDetailResponse;
import com.event.booking.mgmt.dto.EventUpcomingResponse;
import com.event.booking.mgmt.exception.ResourceNotFoundException;
import com.event.booking.mgmt.model.Event;
import com.event.booking.mgmt.model.User;
import com.event.booking.mgmt.repository.EventRepository;
import com.event.booking.mgmt.repository.UserRepository;
import com.event.booking.mgmt.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
}
