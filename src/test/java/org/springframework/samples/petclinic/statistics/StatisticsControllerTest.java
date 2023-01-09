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
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers = StatisticsController.class, 
  excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), 
  excludeAutoConfiguration = SecurityConfiguration.class)
public class StatisticsControllerTest {

  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private AuthenticationService authenticationService;

  @MockBean
  private GameService gameService;

  @MockBean
  private GamePlayerService gamePlayerService;

  @BeforeEach
  void setup() {
    Game g1 = new Game();
    Game g2 = new Game();
    Game g3 = new Game();

    Set<Game> sg1 = Set.of(g1, g2, g3);

    Game g4 = new Game();
    Game g5 = new Game();
    
    Set<Game> sg2 = Set.of(g4, g5);
    
    Game g6 = new Game();

    Set<Game> sg3 = Set.of(g6);

    GamePlayer gp1 = new GamePlayer();
    GamePlayer gp2 = new GamePlayer();
    GamePlayer gp3 = new GamePlayer();
    GamePlayer gp4 = new GamePlayer();
    GamePlayer gp5 = new GamePlayer();
    
    gp1.setGames(sg1);
    gp2.setGames(sg2);
    gp3.setGames(sg3);
    gp4.setGames(sg1);
    gp5.setGames(sg2);

  }
  
  //Ranking de jugadores
  @WithMockUser(value = "frabenrui1", password = "z3bas")
  @Test
  public void testPlayersRanking() throws Exception {
      mockMvc.perform(get("/ranking/global"))
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
      .andExpect(view().name("statistics/ranking"));
  }
}
