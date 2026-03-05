package com.event.booking.mgmt.controller;

import com.event.booking.mgmt.dto.*;
import com.event.booking.mgmt.service.EventService;
import com.event.booking.mgmt.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("api/event")
@RestController
public class EventMgmtController {

    private final EventService eventService;

    public EventMgmtController(EventService eventService){
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid EventCreateRequest request, Authentication authentication){
            String email = authentication.getName();
            EventCreateResponse response = eventService.create(request, email);
            return ResponseUtil.buildSuccessResponse(response, "Successfully create event.");
    }

    @GetMapping
    public ResponseEntity<?> getUpcomingEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        Map<String, Object> responses = eventService.getUpcomingEvents(page, size);
        return ResponseUtil.buildSuccessResponse(responses, "Successfully retrieve upcoming events");
    }

    @GetMapping("/{idEvent}")
    public ResponseEntity<?> getEventDetail(@PathVariable Integer idEvent){
        EventDetailResponse response = eventService.getEventDetail(idEvent);
        return ResponseUtil.buildSuccessResponse(response, "Successfully retrieve event detail");
    }

    @PutMapping("/{idEvent}")
    public ResponseEntity<?> updateEvent(@PathVariable Integer idEvent,@RequestBody @Valid EventUpdateRequest request, Authentication authentication){
        String email = authentication.getName();
        EventUpdateResponse response = eventService.updateEvent(idEvent, request, email);
        return ResponseUtil.buildSuccessResponse(response, "Successfully update event.");
    }

    @DeleteMapping("/{idEvent}")
    public ResponseEntity<?> deleteEvent(@PathVariable Integer idEvent, Authentication authentication){
        String email = authentication.getName();
        eventService.deleteEvent(idEvent, email);
        return ResponseUtil.buildSuccessResponse(null, "Successfully delete event.");
    }

    @GetMapping("/search")
    public ResponseEntity<?> getEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy")LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy")LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){

        Map<String, Object> response = eventService.searchEvent(title, location, startDate, endDate, page, size);
        return ResponseUtil.buildSuccessResponse(response, "Successfully search event.");

    }

}
