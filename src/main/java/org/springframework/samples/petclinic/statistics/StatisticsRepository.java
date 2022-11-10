package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, Integer> {
  public List<Statistics> findAll();
  public List<Statistics> findByPlayer(Player player);
}
