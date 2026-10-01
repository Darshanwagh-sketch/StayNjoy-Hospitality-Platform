package com.hospitality.platform.controller;

import com.hospitality.platform.dto.ApiResponse;
import com.hospitality.platform.dto.AuthResponse;
import com.hospitality.platform.dto.LoginRequest;
import com.hospitality.platform.dto.RegisterRequest;
import com.hospitality.platform.dto.UserDTO;
import com.hospitality.platform.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        UserDTO userDTO = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(userDTO);
    }
}
