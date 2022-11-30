package org.springframework.samples.virus.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.virus.player.Player;
import org.springframework.samples.virus.player.PlayerNotFoundException;
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

  @Transactional
  public void save(Statistics s) throws WonPlayedGamesException {
    if (s.getNumWonGames() > s.getNumPlayedGames()) {
      throw new WonPlayedGamesException();
    } else {
      statisticsRepository.save(s);
    }
  }

  @Transactional
  public void saveStatisticsForNewPlayer(Player player) throws PlayerNotFoundException {

    Statistics statistics = new Statistics();
    statistics.setNumPlayedGames(0);
    statistics.setNumWonGames(0);
    statistics.setPoints(0);
    if (player != null){
      statistics.setPlayer(player);
      statisticsRepository.save(statistics);
    } else {
      throw new PlayerNotFoundException();
    }

  }

  @Transactional
  public void sumPointsToPlayer(Integer points, Player player) {
    Statistics playerStats = findPlayerStatistics(player);
    Integer currentPoints = playerStats.getPoints();
    playerStats.setPoints(currentPoints + points);
    statisticsRepository.save(playerStats); 
  }

}
