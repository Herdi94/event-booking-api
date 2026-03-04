package com.event.booking.mgmt.util;

import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ResponseUtil {
    public static ResponseEntity<Map<String, Object>> buildErrorResponse(int status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("data", null);
        return ResponseEntity.status(status).body(body);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponse(Object data, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", 200);
        body.put("error", null);
        body.put("message", message);
        body.put("data", data);
        return ResponseEntity.ok(body);
    }
}
