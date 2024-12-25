package com.journal.myfirstjournal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
//@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true)  // For uniqueness
    private String username;

    private String password;

    @DBRef  // FOR REF
    private List<JournalEntry> journalEntries = new ArrayList<>();

}
