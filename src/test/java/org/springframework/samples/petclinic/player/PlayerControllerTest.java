package org.springframework.samples.petclinic.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.virus.configuration.SecurityConfiguration;
import org.springframework.samples.virus.player.Player;
import org.springframework.samples.virus.player.PlayerController;
import org.springframework.samples.virus.player.PlayerService;
import org.springframework.samples.virus.statistics.Statistics;
import org.springframework.samples.virus.statistics.StatisticsService;
import org.springframework.samples.virus.util.AuthenticationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(controllers=PlayerController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
    excludeAutoConfiguration = SecurityConfiguration.class)
public class PlayerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private StatisticsService statisticsService;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        Player mockPlayer = new Player();
        when(authenticationService.getPlayer()).thenReturn(mockPlayer);
        when(statisticsService.findPlayerStatistics(mockPlayer)).thenReturn(new Statistics());
    }
    
    @WithMockUser
    @Test
    public void testPlayerProfile() throws Exception {
        mockMvc.perform(get("/player/me"))
            .andExpect(status().isOk())
            .andExpect(view().name("player/playerProfile"))
            .andExpect(model().attributeExists("player"))
            .andExpect(model().attributeExists("statistics"));
    }

    @WithMockUser
    @Test
    public void testPlayerProfileEdit() throws Exception {
        mockMvc.perform(get("/player/me/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("player/createOrUpdateProfileForm"))
            .andExpect(model().attributeExists("player"));
    }

    @WithMockUser
    @Test
    public void testPlayerProfileEditPost() throws Exception {
        mockMvc.perform(post("/player/me/edit")
        .with(csrf())
        .param("firstName", "Pedro")
        .param("lastName", "Lopez")
        .param("status", "true")
        .param("description", "nueva descripcion."))
        .andExpect(status().isOk())
        .andExpect(view().name("player/playerProfile"))
        .andExpect(model().attributeExists("message"));
            
    }
}
