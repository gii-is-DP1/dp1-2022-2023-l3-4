package org.springframework.samples.petclinic.statistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = StatisticsController.class, 
  excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), 
  excludeAutoConfiguration = SecurityConfiguration.class)
public class StatisticsControllerTest {

  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private GameService gameService;

  @MockBean
  private GamePlayerService gamePlayerService;

  @BeforeEach
  void setup() {
    Game g1 = new Game();
    Game g2 = new Game();
    Game g3 = new Game();

    g1.setDuration(Duration.of(100, ChronoUnit.SECONDS));
    g2.setDuration(Duration.of(150, ChronoUnit.SECONDS));
    g3.setDuration(Duration.of(200, ChronoUnit.SECONDS));

    Set<Game> sg1 = Set.of(g1, g2, g3);

    GamePlayer gp1 = new GamePlayer();
    GamePlayer gp2 = new GamePlayer();
    GamePlayer gp3 = new GamePlayer();
    GamePlayer gp4 = new GamePlayer();
    GamePlayer gp5 = new GamePlayer();
    
    gp1.setGames(sg1);
    gp2.setGames(sg1);
    gp3.setGames(sg1);
    gp4.setGames(sg1);
    gp5.setGames(sg1);

    Player p1 = new Player();
    
    PlayerCount pc1 = new PlayerCountImpl(p1, 5);
    PlayerCount pc2 = new PlayerCountImpl(p1, 5);
    PlayerCount pc3 = new PlayerCountImpl(p1, 5);
    PlayerCount pc4 = new PlayerCountImpl(p1, 5);
    PlayerCount pc5 = new PlayerCountImpl(p1, 5);

    List<PlayerCount> listPC = new ArrayList<>();
    listPC.add(pc1);
    listPC.add(pc2);
    listPC.add(pc3);
    listPC.add(pc4);
    listPC.add(pc5);
    
    List<GamePlayer> listGP = List.of(gp1,gp2,gp3,gp4,gp5);

    when(gameService.getRanking()).thenReturn(listPC);
    when(gameService.listTerminateGames()).thenReturn(sg1);
    when(gameService.humanReadableDuration(Duration.of(100, ChronoUnit.SECONDS))).thenReturn("minimum duration");
    when(gameService.humanReadableDuration(Duration.of(150, ChronoUnit.SECONDS))).thenReturn("average duration");
    when(gameService.humanReadableDuration(Duration.of(200, ChronoUnit.SECONDS))).thenReturn("maximum duration");
    when(gameService.humanReadableDuration(Duration.of(450, ChronoUnit.SECONDS))).thenReturn("maximum duration");
    when(gamePlayerService.findAll()).thenReturn(listGP);



  }
  
  //Ranking de jugadores
  @WithMockUser(value = "frabenrui1", password = "z3bas")
  @Test
  public void testPlayersRanking() throws Exception {
      mockMvc.perform(get("/statistics/global"))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("rops"))
      .andExpect(model().attributeExists("top3"))
      .andExpect(model().attributeExists("maxDuration"))
      .andExpect(model().attributeExists("minDuration"))
      .andExpect(model().attributeExists("duration"))
      .andExpect(model().attributeExists("avgDuration")) 
      .andExpect(model().attributeExists("games"))
      .andExpect(model().attributeExists("maxGames"))
      .andExpect(model().attributeExists("minGames"))
      .andExpect(model().attributeExists("avgGames"))
      .andExpect(view().name("statistics/globalStatistics"));
  }
}
