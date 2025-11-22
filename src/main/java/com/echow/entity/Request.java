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
}
