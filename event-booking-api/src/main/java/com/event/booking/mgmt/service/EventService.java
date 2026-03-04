package com.event.booking.mgmt.service;

import com.event.booking.mgmt.dto.EventCreateRequest;
import com.event.booking.mgmt.dto.EventCreateResponse;

public interface EventService {
    public EventCreateResponse create(EventCreateRequest request, String email);
}
