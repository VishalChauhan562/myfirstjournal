package com.journal.myfirstjournal.repository;

import com.journal.myfirstjournal.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User ,ObjectId> {
    Optional<User> findByUsername(String username);
}