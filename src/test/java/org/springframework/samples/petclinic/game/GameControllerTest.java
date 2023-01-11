package org.springframework.samples.petclinic.game;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.room.RoomService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.samples.petclinic.card.GenericCardRepository;
import org.springframework.samples.petclinic.card.GenericCardService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc; 
    
    @MockBean
    private GameService gameServ;

    @MockBean
    private GamePlayerService gamePlayerServ;

    @MockBean
    private CardService cardService;

    @MockBean
    private GenericCardService gCardService;

    @MockBean
    private RoomService roomService;

    @MockBean
    private GenericCardRepository gCardRepo;

    @MockBean
    private AuthenticationService authService;

    @WithMockUser
    @Test
    public void testListRunningGames() throws Exception {
        mockMvc.perform(get("/runningGames"))
            .andExpect(status().isOk())
            .andExpect(view().name("games/runningGameListing"))
            .andExpect(model().attributeExists("games"));

    }

    @WithMockUser
    @Test
    public void testListTerminateGames() throws Exception {
        mockMvc.perform(get("/terminateGames"))
            .andExpect(status().isOk())
            .andExpect(view().name("games/terminateGameListing"))
            .andExpect(model().attributeExists("games"));

    }


}