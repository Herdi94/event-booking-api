package com.event.booking.mgmt.service;

import com.event.booking.mgmt.dto.*;

import java.time.LocalDate;
import java.util.Map;

public interface EventService {
    public EventCreateResponse create(EventCreateRequest request, String email);
    public Map<String, Object> getUpcomingEvents(int page, int size);
    public EventDetailResponse getEventDetail(Integer idEvent);
    public EventUpdateResponse updateEvent(Integer idEvent, EventUpdateRequest request, String email);
    public void deleteEvent(Integer idEvent, String email);
    public Map<String, Object> searchEvent(String title, String location, LocalDate startDate, LocalDate endDate, int page, int size);

}
