package com.legohub.controller;

import com.legohub.exception.UserAlreadyExistsException;
import com.legohub.model.User;
import com.legohub.service.UserService;
import com.legohub.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = userService.login(user.getUsername(), user.getPassword());

            String token = jwtUtil.generateToken(user.getUsername());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getUserByUsername(userDetails.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", currentUser);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.ok(Map.of("message", "User logged out successfully"));
    }
}