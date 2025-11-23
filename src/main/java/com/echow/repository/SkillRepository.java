package com.echow.repository;

import com.echow.entity.Skill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

  Optional<Skill> findByName(String name);

  List<Skill> findByCategory(String category);

  @Query("SELECT s FROM Skill s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))")
  Page<Skill> searchByName(@Param("query") String query, Pageable pageable);

  @Query("SELECT s FROM Skill s WHERE LOWER(s.category) = LOWER(:category)")
  Page<Skill> findByCategoryIgnoreCase(@Param("category") String category, Pageable pageable);

  @Query("SELECT s FROM Skill s ORDER BY s.popularity DESC")
  Page<Skill> findPopularSkills(Pageable pageable);

  @Query(
      "SELECT s FROM Skill s WHERE s.id IN (SELECT sk.id FROM User u JOIN u.wantedSkills sk GROUP BY sk.id ORDER BY COUNT(u) DESC)")
  Page<Skill> findMostWantedSkills(Pageable pageable);
}
