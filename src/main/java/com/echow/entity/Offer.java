package com.echow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Title must not be empty")
  @Column(nullable = false)
  private String title;

  @Size(max = 1000, message = "Description must be less than 1000 characters")
  @Column(length = 1000)
  private String description;

  @NotNull(message = "Skill points cost must not be null")
  @Column(nullable = false)
  private Integer skillPointsCost;

  @NotNull(message = "Duration must not be null")
  @Column(nullable = false)
  private Integer durationMinutes;

  @Builder.Default private Integer maxParticipants = 1;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private OfferStatus status = OfferStatus.AVAILABLE;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    status = OfferStatus.AVAILABLE;
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "creator_id", nullable = false)
  private User creator;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "skill_id", nullable = false)
  private Skill skill;

  @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Request> requests = new HashSet<>();

  @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @Builder.Default
  private Set<Review> reviews = new HashSet<>();

  private boolean canBeRequested() {
    return status == OfferStatus.AVAILABLE
        && (maxParticipants == null || getAcceptedRequestsCount() < maxParticipants);
  }

  private long getAcceptedRequestsCount() {
    return requests.stream()
        .filter(request -> request.getStatus() == RequestStatus.ACCEPTED)
        .count();
  }

  public boolean isCreator(User user){
    return creator.getId().equals(user.getId());
  }
}
