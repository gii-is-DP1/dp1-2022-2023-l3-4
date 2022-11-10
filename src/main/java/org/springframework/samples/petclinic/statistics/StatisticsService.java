package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {
  
  private StatisticsRepository statisticsRepository;

  @Autowired
  public StatisticsService(StatisticsRepository statisticsRepository) {
    this.statisticsRepository = statisticsRepository;
  }

  public List<Statistics> getAllStatistics() {
    return statisticsRepository.findAll();
  }

  public void save(Statistics s) throws WonPlayedGamesException {
    if (s.getNumWonGames() > s.getNumPlayedGames()) {
      throw new WonPlayedGamesException();
    } else {
      statisticsRepository.save(s);
    }
  }

}
