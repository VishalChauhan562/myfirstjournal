package com.journal.myfirstjournal.service;

import com.journal.myfirstjournal.entity.User;
import com.journal.myfirstjournal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //    public Optional<User> getUserEntryById(ObjectId id) {
    //        return UserRepository.findById(id);
    //    }

    //    public User getUserEntryById(ObjectId id) {
    //        Optional<User> entry = UserRepository.findById(id);
    //        return entry.orElse(null);  // return null if not found, or you can throw an exception
    //    }



    public User getUserById(ObjectId id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }


    public User saveUser(User entry){
        return userRepository.save(entry);
    }

    public User updateUser(String username , User entry){
        User old = userRepository.findByUsername(username).orElse(null);
        if(old != null){
            old.setUsername(entry.getUsername() != null && !entry.getUsername().isEmpty() ? entry.getUsername() : old.getUsername());
            old.setPassword(entry.getPassword() != null && !entry.getPassword().isEmpty() ? entry.getPassword() : old.getPassword());
            userRepository.save(old);
        }
        return old;
    }

    public boolean deleteUser (ObjectId id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }
        return  false;
    }
    
    
}
