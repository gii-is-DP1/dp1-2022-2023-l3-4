package org.springframework.samples.petclinic.game;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCardRepository;
import org.springframework.samples.petclinic.card.GenericCardService;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc; 

    @MockBean
    private GameService gameService;

    @MockBean
    private GamePlayerService gamePlayerService;

    @MockBean
    private CardService cardService;

    @MockBean
    private GenericCardService gCardService;

    @Autowired
    private GameController gc;

    @MockBean
    private GameRepository gameRepo;

    @MockBean
    private GenericCardRepository gCardRepo;

    Game game;
    @BeforeEach
    public void setUp() throws Exception {
        Game game = new Game();
        game.setId(70);
        Optional<Game> theGame = Optional.of(game);
        gameService = new GameService(gameRepo);
        when(gameRepo.findById(any(Integer.class))).thenReturn(theGame);
    }

    @WithMockUser
    @Test
    public void testGameListing() throws Exception {
        mockMvc.perform(get("/games"))
            .andExpect(status().isOk())
            .andExpect(view().name("games/listing"))
            .andExpect(model().attributeExists("games"));
    }
    /* 
    @WithMockUser
    @Test
    public void testReset() throws Exception {
        gc.reset();
        assertTrue(gCardRepo.findAll().size()==68);
    }
    */
}