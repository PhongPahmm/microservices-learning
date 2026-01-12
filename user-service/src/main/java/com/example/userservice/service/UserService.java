package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
    public User ensureUserExistsFromToken(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // sub
        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    // Tạo mới user nếu chưa có
                    User user = new User();
                    user.setKeycloakId(keycloakId);
                    user.setEmail(jwt.getClaim("email"));
                    user.setName(jwt.getClaim("preferred_username"));
                    return userRepository.save(user);
                });
    }

}
