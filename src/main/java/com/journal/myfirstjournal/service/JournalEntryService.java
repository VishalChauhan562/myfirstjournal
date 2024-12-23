package com.journal.myfirstjournal.service;

import com.journal.myfirstjournal.entity.JournalEntry;
import com.journal.myfirstjournal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service  // we can also use @Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository JournalEntryRepository;

    public List<JournalEntry> getAllJournalEntry(){
        return JournalEntryRepository.findAll();
    }

    //    public Optional<JournalEntry> getJournalEntryById(ObjectId id) {
    //        return JournalEntryRepository.findById(id);
    //    }

    //    public JournalEntry getJournalEntryById(ObjectId id) {
    //        Optional<JournalEntry> entry = JournalEntryRepository.findById(id);
    //        return entry.orElse(null);  // return null if not found, or you can throw an exception
    //    }



    public JournalEntry getJournalEntryById(ObjectId id) {
        return JournalEntryRepository.findById(id)
                .orElse(null);
    }


    public JournalEntry saveJournalEntry(JournalEntry entry){
        entry.setCreatedAt(LocalDateTime.now());
        return JournalEntryRepository.save(entry);
    }

    public JournalEntry updateJournalEntry(ObjectId id , JournalEntry entry){
        JournalEntry old = JournalEntryRepository.findById(id).orElse(null);
        if(old != null){
            old.setTitle(entry.getTitle() != null && !entry.getTitle().isEmpty() ? entry.getTitle() : old.getTitle());
            old.setContent(entry.getContent() != null && !entry.getContent().isEmpty() ? entry.getContent() : old.getContent());
            JournalEntryRepository.save(old);
        }
        return old;
    }

    public boolean deleteJournalEntry (ObjectId id){
        if(JournalEntryRepository.existsById(id)){
            JournalEntryRepository.deleteById(id);
            return true;
        }
        return  false;
    }

}
