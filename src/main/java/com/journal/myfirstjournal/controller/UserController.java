package com.journal.myfirstjournal.controller;


import com.journal.myfirstjournal.controller.utils.ResponseUtils;
import com.journal.myfirstjournal.entity.JournalEntry;
import com.journal.myfirstjournal.entity.User;
import com.journal.myfirstjournal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<User> allEntries = userService.getAllUsers();
            if (allEntries == null || allEntries.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND) // HttpStatus.NOT_FOUND.value() this convert the status code to integer
                        .body(ResponseUtils.createResponse(HttpStatus.NOT_FOUND.value(), "No Users found."));
            }
            return ResponseEntity.ok(ResponseUtils.createResponse(HttpStatus.OK.value(), "Success", allEntries));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }


    @PostMapping
    public ResponseEntity<?> postEntry(@RequestBody User entry) {
        try {
            if (entry == null || entry.getUsername() == null || entry.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseUtils.createResponse(HttpStatus.BAD_REQUEST.value(), "Invalid request: both username and password is required."));
            }
            User savedEntry = userService.saveUser(entry);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtils.createResponse(HttpStatus.CREATED.value(), "User created successfully", savedEntry));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()));
        }
    }


}
