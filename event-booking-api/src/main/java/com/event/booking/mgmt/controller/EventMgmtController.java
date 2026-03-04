package com.event.booking.mgmt.controller;

import com.event.booking.mgmt.dto.EventCreateRequest;
import com.event.booking.mgmt.dto.EventCreateResponse;
import com.event.booking.mgmt.dto.EventDetailResponse;
import com.event.booking.mgmt.service.EventService;
import com.event.booking.mgmt.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
}
