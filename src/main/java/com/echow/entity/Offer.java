package com.echow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
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

  @NotNull(message = "Title must not be null")
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

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private OfferStatus status = OfferStatus.AVAILABLE;

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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "creator_id", nullable = false)
  private User creator;
}
