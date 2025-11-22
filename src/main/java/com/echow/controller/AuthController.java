package com.echow.controller;

import com.echow.dto.JwtResponse;
import com.echow.dto.LoginRequest;
import com.echow.dto.SignUpRequest;
import com.echow.service.AuthService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired private AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    try {
      String result = authService.register(signUpRequest);
      return ResponseEntity.ok(result);
    } catch (BadRequestException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      JwtResponse result = authService.login(loginRequest);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Invalid credentials");
    }
  }
}
