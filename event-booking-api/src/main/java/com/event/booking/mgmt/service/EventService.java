package com.event.booking.mgmt.service;

import com.event.booking.mgmt.dto.EventCreateRequest;
import com.event.booking.mgmt.dto.EventCreateResponse;
import com.event.booking.mgmt.dto.EventDetailResponse;

import java.util.Map;

public interface EventService {
    public EventCreateResponse create(EventCreateRequest request, String email);
    public Map<String, Object> getUpcomingEvents(int page, int size);
    public EventDetailResponse getEventDetail(Integer idEvent);

}
