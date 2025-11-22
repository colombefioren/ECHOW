package com.echow.service.impl;

import com.echow.dto.JwtResponse;
import com.echow.dto.LoginRequest;
import com.echow.dto.SignUpRequest;
import com.echow.repository.UserRepository;
import com.echow.security.JwtTokenProvider;
import com.echow.security.UserPrincipal;
import com.echow.service.AuthService;
import jakarta.transaction.Transactional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtTokenProvider tokenProvider;

  @Override
  public JwtResponse login(LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    return new JwtResponse(
        jwt,
        "Bearer",
        userPrincipal.getId(),
        userPrincipal.getUsername(),
        userPrincipal.getEmail(),
        "",
        "",
        userPrincipal.getAuthorities().stream()
            .map(item -> item.getAuthority().replace("ROLE_", ""))
            .collect(Collectors.toSet()));
  }

  @Override
  public String register(SignUpRequest signUpRequest) {
    return "";
  }
}
