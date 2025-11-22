package com.echow.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private RequestStatus status = RequestStatus.PENDING;

  @Column(length = 500)
  private String message;

  private LocalDateTime requestedAt;
  private LocalDateTime respondedAt;
  private LocalDateTime scheduledAt;

  @PrePersist
  protected void onCreate() {
    requestedAt = LocalDateTime.now();
    status = RequestStatus.PENDING;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "offer_id", nullable = false)
  private Offer offer;

  public boolean canBeAccepted() {
    return status == RequestStatus.PENDING && offer.canBeRequested();
  }

  public boolean canBeCompleted() {
    return status == RequestStatus.ACCEPTED;
  }

  public void accept() {
    if (!canBeAccepted()) {
      throw new IllegalStateException("Request cannot be accepted");
    }
    this.status = RequestStatus.ACCEPTED;
    this.respondedAt = LocalDateTime.now();
  }

  public void complete() {
    if (!canBeCompleted()) {
      throw new IllegalStateException("Request cannot be completed");
    }
    this.status = RequestStatus.COMPLETED;

    this.user.deductPoints(offer.getSkillPointsCost());
    this.offer.getCreator().addPoints(offer.getSkillPointsCost());

    this.user.setCompletedSessions(this.user.getCompletedSessions() + 1);
    this.offer
        .getCreator()
        .setCompletedSessions(this.offer.getCreator().getCompletedSessions() + 1);
  }
}
