package org.springframework.samples.virus.statistics;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.virus.player.Player;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, Integer> {
  @Query("SELECT s FROM Statistics s WHERE s.player = :player")
  public Statistics findByPlayer(@Param("player") Player player);
}
