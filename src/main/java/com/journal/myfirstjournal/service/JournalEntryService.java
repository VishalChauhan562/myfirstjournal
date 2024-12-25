package com.journal.myfirstjournal.service;

import com.journal.myfirstjournal.entity.JournalEntry;
import com.journal.myfirstjournal.entity.User;
import com.journal.myfirstjournal.exceptions.UserNotFoundException;
import com.journal.myfirstjournal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service  // we can also use @Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntry> getAllJournalEntry(String username){
        try {
            User user = userService.getUserByUsername(username);
            if(user == null){
                throw new UserNotFoundException("User not found: " + username);
            }
            return user.getJournalEntries();
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while getting the journal entries.", e);
        }
    }

    //    public Optional<JournalEntry> getJournalEntryById(ObjectId id) {
    //        return journalEntryRepository.findById(id);
    //    }

    //    public JournalEntry getJournalEntryById(ObjectId id) {
    //        Optional<JournalEntry> entry = journalEntryRepository.findById(id);
    //        return entry.orElse(null);  // return null if not found, or you can throw an exception
    //    }



    public JournalEntry getJournalEntryById(ObjectId id, String username) {
        try {
            User user = userService.getUserByUsername(username);
            if(user == null){
                throw new UserNotFoundException("User not found: " + username);
            };
            return user.getJournalEntries()
                    .stream()
                    .filter(x -> x.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        } catch (UserNotFoundException e) {
            throw e; // Re-throw the specific exception
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while getting the journal entry.", e);
        }
    }


    @Transactional
    public JournalEntry saveJournalEntry(JournalEntry entry, String username){
        try {
            entry.setCreatedAt(LocalDateTime.now());
            User user = userService.getUserByUsername(username);
            if(user == null){
                throw new UserNotFoundException("User not found: " + username);
            };
            JournalEntry newEntry = journalEntryRepository.save(entry);
            user.getJournalEntries().add(newEntry);
            userService.saveUser(user);
            return newEntry;
        } catch (UserNotFoundException e) {
            throw e; // Re-throw the specific exception
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the journal entry.", e);
        }
    }

    public JournalEntry updateJournalEntry(ObjectId id , JournalEntry entry, String username){

        try {
            User user = userService.getUserByUsername(username);
            if(user == null){
                throw new UserNotFoundException("User not found: " + username);
            };

            boolean isJournalEntryExist = user.getJournalEntries().stream().anyMatch(x-> x.getId().equals(id));
            if(isJournalEntryExist){
                JournalEntry old = journalEntryRepository.findById(id).orElse(null);
                if(old != null){
                    old.setTitle(entry.getTitle() != null && !entry.getTitle().isEmpty() ? entry.getTitle() : old.getTitle());
                    old.setContent(entry.getContent() != null && !entry.getContent().isEmpty() ? entry.getContent() : old.getContent());
                    journalEntryRepository.save(old);
                }
                return old;
            };
            return  null;
        } catch (UserNotFoundException e) {
            throw e; // Re-throw the specific exception
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the journal entry.", e);
        }
    }

    public boolean deleteJournalEntry (ObjectId id, String username){
        try {
            User user = userService.getUserByUsername(username);
            if(user == null){
                throw new UserNotFoundException("User not found: " + username);
            };

            boolean isJournalEntryExist = user.getJournalEntries().stream().anyMatch(x-> x.getId().equals(id));
            if(isJournalEntryExist){
                journalEntryRepository.deleteById(id);
                return true;
            };
            return  false;
        } catch (UserNotFoundException e) {
            throw e; // Re-throw the specific exception
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting the journal entry.", e);
        }

    }

}
