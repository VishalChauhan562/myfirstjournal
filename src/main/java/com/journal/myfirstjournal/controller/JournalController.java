package com.journal.myfirstjournal.controller;

import com.journal.myfirstjournal.controller.utils.ResponseUtils;
import com.journal.myfirstjournal.entity.JournalEntry;
import com.journal.myfirstjournal.entity.User;
import com.journal.myfirstjournal.exceptions.UserNotFoundException;
import com.journal.myfirstjournal.service.JournalEntryService;
import com.journal.myfirstjournal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/journal/{username}")
public class JournalController {

    @Autowired
    private JournalEntryService service;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable String username) {
        try {

            List<JournalEntry> allEntries = service.getAllJournalEntry(username);

            // Check if journal entries are empty
            if (allEntries == null || allEntries.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), "No journal entries found for this user."));
            }

            // Return journal entries
            return ResponseEntity.ok(ResponseUtils.createResponse(HttpStatus.OK.value(), "Success", allEntries));
        }catch (UserNotFoundException ex) {
            // Handle user not found error
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        } catch (Exception ex) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> postEntry(@RequestBody JournalEntry entry, @PathVariable String username) {
        try {

            if (entry == null || entry.getTitle() == null || entry.getTitle().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: Title is required."));
            }

            JournalEntry savedEntry = service.saveJournalEntry(entry,username);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtils.createResponse(HttpStatus.CREATED.value(), "Journal entry created successfully", savedEntry));
        } catch (UserNotFoundException ex) {
            // Handle user not found error
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }





    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable ObjectId id, @PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            // Check if user exists
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), "User not found."));
            }

            JournalEntry entry = user.getJournalEntries().stream()
                    .filter(journalEntry -> journalEntry.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (entry == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), "Journal entry not found with ID: " + id));
            }
            return ResponseEntity.ok(ResponseUtils.createResponse(HttpStatus.OK.value(), "Success", entry));
        } catch (UserNotFoundException ex) {
            // Handle user not found error
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid ID format: " + id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> putEntry(@PathVariable ObjectId id, @RequestBody JournalEntry entry, @PathVariable String username) {
        try {

            if (entry == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: Entry cannot be null."));
            }

            JournalEntry updatedEntry = service.updateJournalEntry(id, entry, username);

            if (updatedEntry == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), "Journal entry not found with ID: " + id));
            }

            return ResponseEntity.ok(ResponseUtils.createResponse(HttpStatus.OK.value(), "Journal entry updated successfully", updatedEntry));
        } catch (UserNotFoundException ex) {
            // Handle user not found error
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid ID format: " + id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id,  @PathVariable String username) {
        try {
            boolean isDeleted = service.deleteJournalEntry(id, username);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // No body for successful deletion
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), "No journal entry found with ID: " + id));
            }
        } catch (UserNotFoundException ex) {
            // Handle user not found error
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid ID format: " + id));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }

}