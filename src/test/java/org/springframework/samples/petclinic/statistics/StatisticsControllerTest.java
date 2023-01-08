package org.springframework.samples.petclinic.statistics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
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
  
  //Ranking de jugadores
  @WithMockUser
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
