package com.echow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Username must not be null")
  @Column(unique = true, nullable = false)
  private String username;

  @NotNull(message = "Email must not be null")
  @Column(unique = true, nullable = false)
  private String email;

  @NotNull(message = "Password must not be null")
  @Column(nullable = false)
  private String password;

  private String fullName;
  private String bio;
  private String profilePictureUrl;

  @Builder.Default private Integer skillPoints = 100;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Set<Role> roles = new HashSet<>();

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
