package com.example.services;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private final String DEFAULT_PROFILE_PICTURE_URL = "http://bootdey.com/img/Content/avatar/avatar1.png"; // Replace with actual URL
    public void registerUser(User user) {
        user.setPassword(user.getPassword());

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Set<String> roles = new HashSet<>();
            roles.add("ROLE_USER");  // Add a default role
            user.setRoles(roles);
        }

        if (user.getProfilePicture() == null || user.getProfilePicture().isEmpty()) {
            user.setProfilePicture(DEFAULT_PROFILE_PICTURE_URL);
        }
        userRepository.save(user);
    }




    public void updateUser(User user) {
        System.out.println("Updating user: " + user.getUsername());
        System.out.println("New password: " + user.getPassword());
        userRepository.save(user);
    }

    public UserDetails authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        System.out.println("Authenticating user: " + username);
        if (!password.matches(user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return user;
    }


    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    public User findByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + name));
    }
}

