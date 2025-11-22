package com.echow.service;

import com.echow.dto.JwtResponse;
import com.echow.dto.LoginRequest;
import com.echow.dto.SignUpRequest;

public interface AuthService {
  JwtResponse login(LoginRequest loginRequest);

  String register(SignUpRequest signUpRequest);
}
