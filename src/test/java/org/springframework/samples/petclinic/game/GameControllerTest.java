package org.springframework.samples.petclinic.game;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardRepository;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.card.GenericCard.Colour;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerRepository;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc; 

    @Autowired
    

    @MockBean
    private GameService gameServ;

    @MockBean
    private GamePlayerService gamePlayerServ;

    @MockBean
    private CardService cardServ;

    // @MockBean
    // private GameRepository gameRepo;
    
    // @MockBean
    // private CardRepository cardRepo;

    // @MockBean
    // private GamePlayerRepository gamePlayerRepo;

    @WithMockUser
    @Test
    public void testGameListing() throws Exception {
        mockMvc.perform(get("/games"))
            .andExpect(status().isOk())
            .andExpect(view().name("games/listing"))
            .andExpect(model().attributeExists("games"));
    }

    @BeforeEach
    void setup() {
        GenericCard heart = new GenericCard();
        heart.setId(0);
        heart.setColour(Colour.RED);
        heart.setType(Type.ORGAN);

        GenericCard stomach = new GenericCard();
        stomach.setId(1);
        stomach.setColour(Colour.GREEN);
        stomach.setType(Type.ORGAN);

        Card c_heart = new Card();
        c_heart.setId(0);
        c_heart.setBody(false);
        c_heart.setPlayed(false);
        c_heart.setCardVaccine(null);
        c_heart.setCardVirus(null);
        c_heart.setVaccines(new ArrayList<>());
        c_heart.setVirus(new ArrayList<>());
        c_heart.setType(heart);
        Optional<Card> c_heart1 = Optional.of(c_heart);

        Card c_heart2 = new Card();
        c_heart2.setId(1);
        c_heart2.setBody(true);
        c_heart2.setPlayed(false);
        c_heart2.setCardVaccine(null);
        c_heart2.setCardVirus(null);
        c_heart2.setVaccines(new ArrayList<>());
        c_heart2.setVirus(new ArrayList<>());
        c_heart2.setType(heart);

        
        Card c_stomach = new Card();
        c_stomach.setId(2);
        c_stomach.setBody(true);
        c_stomach.setPlayed(false);
        c_stomach.setCardVaccine(null);
        c_stomach.setCardVirus(null);
        c_stomach.setVaccines(new ArrayList<>());
        c_stomach.setVirus(new ArrayList<>());
        c_stomach.setType(stomach);

        Game g = new Game();
        g.setCards(new ArrayList<>());
        g.setClassification(new HashMap<>());
        g.setIsRunning(true);
        g.setId(0);
        g.setTurn(0);
        g.setRound(0);
        g.setInitialHour(LocalDateTime.now());


        GamePlayer gp1 = new GamePlayer();
        gp1.setId(0);
        gp1.setHost(true);
        gp1.setCards(List.of(c_heart));
        gp1.setWinner(false);
        Optional<GamePlayer> gp1_o = Optional.of(gp1);

        GamePlayer gp2 = new GamePlayer();
        gp2.setId(1);
        gp2.setHost(true);
        gp2.setCards(new ArrayList<>());
        gp2.setWinner(false);
        Optional<GamePlayer> gp2_o = Optional.of(gp2);

        g.setGamePlayer(List.of(gp1,gp2));

        when(gameServ.findGames(0)).thenReturn(g);
        when(cardServ.findCard(0)).thenReturn(c_heart1);
        when(gamePlayerServ.findById(0)).thenReturn(gp1_o);
        when(gamePlayerServ.findById(1)).thenReturn(gp2_o);

        

    }

    @Test
    public void testPlayOrgan() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardServ);
        assertEquals("/games/"+0+"/gamePlayer/"+1+"/decision", gc.playOrgan(0, 0, 0, 0));

    }


    
}
