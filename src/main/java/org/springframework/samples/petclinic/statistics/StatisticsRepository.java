package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, Integer> {
  public List<Statistics> findAll();
  @Query("SELECT s FROM Statistics s WHERE s.player = :player")
  public Statistics findByPlayer(Player player);
}
