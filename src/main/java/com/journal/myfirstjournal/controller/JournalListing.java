package com.journal.myfirstjournal.controller;

import com.journal.myfirstjournal.entity.JournalEntry;
import com.journal.myfirstjournal.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalListing {

    @Autowired
    private JournalEntryService service;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<JournalEntry> allEntries = service.getAllJournalEntry();
            if (allEntries == null || allEntries.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(HttpStatus.NOT_FOUND.value(), "No journal entries found."));
            }
            return ResponseEntity.ok(createResponse(HttpStatus.OK.value(), "Success", allEntries));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable ObjectId id) {
        try {
            JournalEntry entry = service.getJournalEntryById(id);
            if (entry == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(HttpStatus.NOT_FOUND.value(), "Journal entry not found with ID: " + id));
            }
            return ResponseEntity.ok(createResponse(HttpStatus.OK.value(), "Success", entry));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid ID format: " + id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> postEntry(@RequestBody JournalEntry entry) {
        try {
            if (entry == null || entry.getTitle() == null || entry.getTitle().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: Title is required."));
            }
            JournalEntry savedEntry = service.saveJournalEntry(entry);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createResponse(HttpStatus.CREATED.value(), "Journal entry created successfully", savedEntry));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> putEntry(@PathVariable ObjectId id, @RequestBody JournalEntry entry) {
        try {
            if (entry == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: Entry cannot be null."));
            }
            JournalEntry updatedEntry = service.updateJournalEntry(id, entry);
            if (updatedEntry == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(HttpStatus.NOT_FOUND.value(), "No journal entry found with ID: " + id));
            }
            return ResponseEntity.ok(createResponse(HttpStatus.OK.value(), "Journal entry updated successfully", updatedEntry));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid ID format: " + id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        try {
            boolean isDeleted = service.deleteJournalEntry(id);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // No body for successful deletion
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createResponse(HttpStatus.NOT_FOUND.value(), "No journal entry found with ID: " + id));
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid ID format: " + id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    /**
     * Utility method to create a structured JSON response via method overloading
     */
    private Map<String, Object> createResponse(int statusCode, String message) {
        return createResponse(statusCode, message, null);
    }

    private Map<String, Object> createResponse(int statusCode, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", statusCode);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }


}