package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatisticsService {
  
  private StatisticsRepository statisticsRepository;

  @Autowired
  public StatisticsService(StatisticsRepository statisticsRepository) {
    this.statisticsRepository = statisticsRepository;
  }

  @Transactional(readOnly = true)
  public Statistics findPlayerStatistics(Player player) {
    return statisticsRepository.findByPlayer(player);
  }

  public void save(Statistics s) throws WonPlayedGamesException {
    if (s.getNumWonGames() > s.getNumPlayedGames()) {
      throw new WonPlayedGamesException();
    } else {
      statisticsRepository.save(s);
    }
  }

  @Transactional
  public void saveStatisticsForNewPlayer(Player player) {
    Statistics playerStatistics = new Statistics();
    playerStatistics.setPlayer(player);
    playerStatistics.setNumPlayedGames(0);
    playerStatistics.setNumWonGames(null);

    statisticsRepository.save(playerStatistics);
  }

  @Transactional
  public void sumPointsToPlayer(Integer points, Player player) {
    Statistics playerStats = findPlayerStatistics(player);
    Integer currentPoints = playerStats.getPoints();
    playerStats.setPoints(currentPoints + points);
    statisticsRepository.save(playerStats); 
  }

}
