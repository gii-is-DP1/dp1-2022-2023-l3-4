package org.springframework.samples.petclinic.achievements;

import java.util.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
  public List<Achievement> findAll();

  @Query("SELECT at FROM AchievementType at")
  public List<AchievementType> findAllTypes();

  // @Query("SELECT u.achievements FROM User u WHERE u.username = :username")
  // public List<Achievement> findByUsername(String username);
}
