package com.legohub.controller;

import com.legohub.dto.response.ErrorResponse;
import com.legohub.dto.response.LoginResponse;
import com.legohub.dto.response.SuccessResponse;
import com.legohub.dto.response.UserResponse;
import com.legohub.exception.UserAlreadyExistsException;
import com.legohub.model.User;
import com.legohub.service.UserService;
import com.legohub.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    public ResponseEntity<Object> login(@RequestBody User user) {
        try {
            Authentication authentication = userService.login(user.getUsername(), user.getPassword());
            String token = jwtUtil.generateToken(user.getUsername());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getUserByUsername(userDetails.getUsername());

            return ResponseEntity.ok(new LoginResponse(token, currentUser));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UserResponse("User registered successfully", newUser));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("User already exists"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Registration failed"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser() {
        try {
            User currentUser = userService.getCurrentAuthenticatedUser();
            if (currentUser != null) {
                return ResponseEntity.ok(currentUser);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("User not authenticated"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to retrieve user information"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        return ResponseEntity.ok(new SuccessResponse("Logged out successfully"));
    }
}