package com.echow.repository;

import com.echow.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
  List<User> findByRole(@Param("role") String role);

  @Query("SELECT u FROM User u ORDER BY u.skillPoints DESC")
  Page<User> findTopBySkillPoints(Pageable pageable);

  @Query("SELECT u FROM User u JOIN u.skills s WHERE s.id = :skillId ")
  List<User> findBySkillId(@Param("skillId") Long skillId);

  @Query("SELECT u FROM User u JOIN u.wantedSkills ws WHERE ws.id = :skillId")
  List<User> findByWantedSkillId(@Param("skillId") Long skillId);

  @Query("SELECT u FROM User u WHERE u.averageRating >= :minRating ORDER BY u.averageRating DESC")
  Page<User> findByMinRating(@Param("minRating") Double minRating, Pageable pageable);
}
