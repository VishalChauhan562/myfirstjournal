package com.journal.myfirstjournal.controller.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseUtils {

    public static Map<String, Object> createResponse(int statusCode, String message) {
        return createResponse(statusCode, message, null);
    }

    public static Map<String, Object> createResponse(int statusCode, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", statusCode);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }
}
