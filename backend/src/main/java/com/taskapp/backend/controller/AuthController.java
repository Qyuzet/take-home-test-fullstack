package com.taskapp.backend.controller;

import com.taskapp.backend.dto.LoginRequest;
import com.taskapp.backend.dto.LoginResponse;
import com.taskapp.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    // Hardcoded credentials (as per requirement)
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin123";

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Validate credentials
        if (VALID_USERNAME.equals(loginRequest.getUsername()) && 
            VALID_PASSWORD.equals(loginRequest.getPassword())) {
            
            // Generate JWT token
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            
            return ResponseEntity.ok(new LoginResponse(token, loginRequest.getUsername()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }
    }
}

