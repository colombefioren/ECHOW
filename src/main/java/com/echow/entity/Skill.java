package com.echow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull(message = "Name must not be null")
  @Column(unique = true, nullable = false)
  private String name;

  @Column(length = 1000)
  @Size(max = 1000, message = "Description must be less than 1000 characters")
  private String description;

  private String category;

  @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
  @Builder.Default
  private Set<User> users = new HashSet<>();

  @ManyToMany(mappedBy = "wantedSkills", fetch = FetchType.LAZY)
  @Builder.Default
  private Set<User> usersWanting = new HashSet<>();
}
