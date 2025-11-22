package com.echow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 1000)
  @NotNull
  private String comment;

  @Column(nullable = false)
  @NotNull
  private Integer rating;

  private String type;

  private LocalDateTime createdAt;

  // User being reviewed
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offer_id", nullable = false)
  private Offer offer;

  // User writing the review
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reviewer_id", nullable = false)
  private User reviewer;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    if (offer.getCreator().equals(reviewer)) {
      type = "FOR_STUDENT";
    } else {
      type = "FOR_MENTOR";
    }
  }

  public boolean isValidRating() {
    return rating >= 1 && rating <= 5;
  }

  public boolean canUserReview(User user, Request request) {
    return request.getStatus() == RequestStatus.COMPLETED
        && (request.getUser().equals(user) || request.getOffer().getCreator().equals(user));
  }
}
