package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

   private final UserService userService;
   private final UserRepository userRepository;

    @PostMapping
    @CacheEvict(value = "allUsers", allEntries = true)
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/keycloak/{sub}")
    public UserDTO getUserByKeycloakId(@PathVariable String sub, @AuthenticationPrincipal Jwt jwt) {
        //tự tạo user từ token
        User user = userRepository.findByKeycloakId(sub)
            .orElseGet(() -> userService.ensureUserExistsFromToken(jwt));

        return new UserDTO(user.getId(), user.getName(), user.getEmail());
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
