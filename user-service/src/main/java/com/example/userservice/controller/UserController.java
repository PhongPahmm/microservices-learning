package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping
    @CacheEvict(value = "allUsers", allEntries = true)
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }


    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable ("id") Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User with id " + id + " not found"));
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

    }
    @GetMapping
    @Cacheable("allUsers")
    public List<User> getAllUsers() {
        System.out.println("Query all users from database");
        return userRepository.findAll();
    }
}
