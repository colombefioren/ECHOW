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

  private String firstName;
  private String lastName;
  private String bio;
  private String profilePictureUrl;

  @Builder.Default private Integer skillPoints = 100;

  @Builder.Default private Double averageRating = 0.0;

  @Builder.Default private Integer completedSessions = 0;

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
    if (roles.isEmpty()) {
      roles.add(Role.STUDENT);
    }
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_skills",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
  @Builder.Default
  private Set<Skill> skills = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_wanted_skills",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
  @Builder.Default
  private Set<Skill> wantedSkills = new HashSet<>();

  @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @Builder.Default
  private Set<Offer> offers = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Request> requests = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Review> reviewsReceived = new HashSet<>();

  @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Review> reviewsWritten = new HashSet<>();

  public boolean canAfford(Integer points) {
    return this.skillPoints >= points;
  }

  public void deductPoints(Integer points) {
    if (!canAfford(points)) {
      throw new RuntimeException("Insufficient skill points");
    }
    this.skillPoints -= points;
  }

  public void addPoints(Integer points) {
    this.skillPoints += points;
  }

  public void updateRating() {
    if (reviewsReceived != null && !reviewsReceived.isEmpty()) {
      this.averageRating =
          reviewsReceived.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }
  }
}
