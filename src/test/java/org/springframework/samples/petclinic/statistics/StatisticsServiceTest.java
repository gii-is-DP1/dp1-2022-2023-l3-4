package org.springframework.samples.petclinic.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerNotFoundException;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

  @Mock
  StatisticsRepository statisticsRepository;

  @Autowired
  protected StatisticsService statisticsService;
  
  private Game g1 = new Game();
  private Game g2 = new Game();
  private Game g3 = new Game();
  private Game g4 = new Game();

  private GamePlayer gp1 = new GamePlayer();
  private GamePlayer gp2 = new GamePlayer();
  private GamePlayer gp3 = new GamePlayer();
  private GamePlayer gp4 = new GamePlayer();
  private GamePlayer gp5 = new GamePlayer();
  private GamePlayer gp6 = new GamePlayer();
  private GamePlayer gp7 = new GamePlayer();
  private GamePlayer gp8 = new GamePlayer();

  private Player p1 = new Player();
  private Player p2 = new Player();

  @BeforeEach
  void setup() {
    statisticsService = new StatisticsService(statisticsRepository);

    gp1 = setGamePlayer(gp1, 1, true, p1);
    gp2 = setGamePlayer(gp2, 2, false, p2);
    gp3 = setGamePlayer(gp3, 3, true, p1);
    gp4 = setGamePlayer(gp4, 4, false, p2);
    gp5 = setGamePlayer(gp5, 5, true, p1);
    gp6 = setGamePlayer(gp6, 6, false, p2);
    gp7 = setGamePlayer(gp7, 7, true, p1);
    gp8 = setGamePlayer(gp8, 8, false, p2);

    g1.setGamePlayer(List.of(gp1,gp2));
    g2.setGamePlayer(List.of(gp3,gp4));
    g3.setGamePlayer(List.of(gp5,gp6));
    g4.setGamePlayer(List.of(gp7,gp8));

  }

  private Player createPlayer(String firstName, String lastName) {
    Player player = new Player();
    player.setFirstName(firstName);
    player.setLastName(lastName);
    return player;
  }

  private GamePlayer setGamePlayer(GamePlayer gp, Integer id, Boolean winner, Player player) {
    gp.setId(id);
    gp.setWinner(winner);
    gp.setPlayer(player);
    return gp;
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
    assertThrows(PlayerNotFoundException.class, () -> {statisticsService.saveStatisticsForNewPlayer(player);});
  }
  @Test
  public void testSaveUnsuccessfulStatisticsNumWonNumPlayed() {
    Player player = createPlayer("Lucia", "Perez");
    Statistics statistics = createStatistics(40, 5, 6, 30, player);
    assertThrows(WonPlayedGamesException.class, () -> {statisticsService.save(statistics);});
  }
  
  @Test
  public void testSaveStatisticsForNewPlayer() {
    Player player = createPlayer("Maria", "Marquez");
    Statistics statistics = createStatistics(40, 5, 2, 30, player);

    try {
      statisticsService.save(statistics);
      verify(statisticsRepository).save(statistics);
    } catch (Exception e) {
      fail("No exception should be thrown");
    }

  }

  @Test
  public void testRanking() {
    List<Statistics> topGamers = new ArrayList<>();
    topGamers.add(createStatistics(1, 0, 4, 0, p1));
    assertEquals(statisticsService.getRanking(), topGamers);
  }

}
