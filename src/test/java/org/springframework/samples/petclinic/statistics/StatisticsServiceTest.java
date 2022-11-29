package org.springframework.samples.petclinic.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerNotFoundException;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

  @Mock
  StatisticsRepository statisticsRepository;

  private static final Integer ID_PLAYER = 1;

  private Player createPlayer(String firstName, String lastName) {
    Player player = new Player();
    player.setFirstName(firstName);
    player.setLastName(lastName);
    return player;
  }

  private Statistics createStatistics(int id, int num_played_games, int num_won_games, int points, Player player){
    Statistics statistics = new Statistics();
    statistics.setId(id);
    statistics.setNumPlayedGames(num_played_games);
    statistics.setNumWonGames(num_won_games);
    statistics.setPoints(points);
    statistics.setPlayer(player);
    return statistics;
  }

  @Test
  public void testSaveSuccesfulStatistics() {
    Player player = new Player();
    player.setId(2);
    
    Statistics statistics = createStatistics(20, 5, 2, 50, player);
    StatisticsService statisticsService = new StatisticsService(statisticsRepository);

    try {
      statisticsService.save(statistics);
      verify(statisticsRepository).save(statistics);
    } catch (Exception e) {
      fail("No exception should be thrown");
    }
    
  }
  
  @Test
  public void testSaveUnsuccessfulStatisticsPlayerNull() {
    Player player = null;
    StatisticsService statisticsService = new StatisticsService(statisticsRepository);
    
    assertThrows(PlayerNotFoundException.class, () -> {statisticsService.saveStatisticsForNewPlayer(player);});

  }
  @Test
  public void testSaveUnsuccessfulStatisticsNumWonNumPlayed() {
    Player player = createPlayer("Lucia", "Perez");
    Statistics statistics = createStatistics(40, 5, 6, 30, player);
    StatisticsService statisticsService = new StatisticsService(statisticsRepository);
    
    assertThrows(WonPlayedGamesException.class, () -> {statisticsService.save(statistics);});
    
  }
  
  @Test
  public void testSaveStatisticsForNewPlayer() {
    Player player = createPlayer("Maria", "Marquez");
    Statistics statistics = createStatistics(40, 5, 2, 30, player);
    StatisticsService statisticsService = new StatisticsService(statisticsRepository);

    try {
      statisticsService.save(statistics);
      verify(statisticsRepository).save(statistics);
    } catch (Exception e) {
      fail("No exception should be thrown");
    }

  }

}
