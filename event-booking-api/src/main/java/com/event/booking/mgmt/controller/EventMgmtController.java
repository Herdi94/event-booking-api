package com.event.booking.mgmt.controller;

import com.event.booking.mgmt.dto.EventCreateRequest;
import com.event.booking.mgmt.dto.EventCreateResponse;
import com.event.booking.mgmt.service.EventService;
import com.event.booking.mgmt.util.ResponseUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("api/event")
@RestController
public class EventMgmtController {

    private final EventService eventService;

    public EventMgmtController(EventService eventService){
        this.eventService = eventService;
    }

    public ResponseEntity<?> create(@RequestBody EventCreateRequest request, Authentication authentication){
            String email = authentication.getName();
            EventCreateResponse response = eventService.create(request, email);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtil.buildSuccessResponse(response, "Successfully create event."));
    }
}
