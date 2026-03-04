package com.event.booking.mgmt.service.impl;

import com.event.booking.mgmt.dto.EventCreateRequest;
import com.event.booking.mgmt.dto.EventCreateResponse;
import com.event.booking.mgmt.exception.ResourceNotFoundException;
import com.event.booking.mgmt.model.Event;
import com.event.booking.mgmt.model.User;
import com.event.booking.mgmt.repository.EventRepository;
import com.event.booking.mgmt.repository.UserRepository;
import com.event.booking.mgmt.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
