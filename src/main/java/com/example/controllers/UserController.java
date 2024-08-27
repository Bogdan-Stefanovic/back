package com.example.controllers;

import com.example.model.User;
import com.example.request.LoginRequest;
import com.example.response.JwtResponse;
import com.example.security.JwtService;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDetails user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
            String token = jwtService.generateToken(user);
            return ResponseEntity.ok(new JwtResponse(token)); // Return the JWT token
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<User> getMe(Principal principal) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        return ResponseEntity.ok(user);
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser, Principal principal) {
        try {
            // Get the current authenticated user
            User currentUser = (User) userService.loadUserByUsername(principal.getName());

            // Store the old username for comparison
            String oldUsername = currentUser.getUsername();

            // Update user details conditionally
            if (updatedUser.getLastname() != null) {
                currentUser.setLastname(updatedUser.getLastname());
            }
            if (updatedUser.getEmail() != null) {
                currentUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getAddress() != null) {
                currentUser.setAddress(updatedUser.getAddress());
            }
            if (updatedUser.getPhone() != null) {
                currentUser.setPhone(updatedUser.getPhone());
            }
            if (updatedUser.getProfilePicture() != null) {
                currentUser.setProfilePicture(updatedUser.getProfilePicture());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                String newpassword = updatedUser.getPassword();
                currentUser.setPassword(newpassword);
            }
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(oldUsername)) {
                currentUser.setUsername(updatedUser.getUsername());
            }

            // Save the updated user
            userService.updateUser(currentUser);

            // Re-authenticate the user with the new username if it has changed
            if (!oldUsername.equals(currentUser.getUsername())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        currentUser, currentUser.getPassword(), currentUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generate a new JWT token
                String newToken = jwtService.generateToken(currentUser);

                // Send the new token back to the client
                return ResponseEntity.ok(Map.of("message", "User updated successfully", "token", newToken));
            }

            return ResponseEntity.ok(Map.of("message", "User updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }








}

