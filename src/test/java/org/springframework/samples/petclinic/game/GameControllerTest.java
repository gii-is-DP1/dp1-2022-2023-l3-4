package org.springframework.samples.petclinic.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.card.GenericCard.Colour;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.samples.petclinic.card.GenericCardRepository;
import org.springframework.samples.petclinic.card.GenericCardService;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    
    @MockBean
    private GameService gameServ;

    @MockBean
    private GamePlayerService gamePlayerServ;

    // @MockBean
    // private GameRepository gameRepo;
    
    // @MockBean
    // private CardRepository cardRepo;

    // @MockBean
    // private GamePlayerRepository gamePlayerRepo;

    @MockBean
    private CardService cardService;

    @MockBean
    private GenericCardService gCardService;

    @MockBean
    private GameRepository gameRepo;

    @MockBean
    private GenericCardRepository gCardRepo;

/*
    Game game;
    @BeforeEach
    public void setUp() throws Exception {
        Game game = new Game();
        game.setId(70);
        Optional<Game> theGame = Optional.of(game);
        when(gameRepo.findById(any(Integer.class))).thenReturn(theGame);
    }
*/

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

        GenericCard heart1 = new GenericCard();
        heart1.setId(1);
        heart1.setColour(Colour.RED);
        heart1.setType(Type.ORGAN);

        GenericCard heart2 = new GenericCard();
        heart2.setId(3);
        heart2.setColour(Colour.RED);
        heart2.setType(Type.ORGAN);

        GenericCard heart3 = new GenericCard();
        heart3.setId(4);
        heart3.setColour(Colour.RED);
        heart3.setType(Type.ORGAN);

        GenericCard stomach = new GenericCard();
        stomach.setId(2);
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
        c_heart2.setBody(false);
        c_heart2.setPlayed(false);
        c_heart2.setCardVaccine(null);
        c_heart2.setCardVirus(null);
        c_heart2.setVaccines(new ArrayList<>());
        c_heart2.setVirus(new ArrayList<>());
        c_heart2.setType(heart1);
        Optional<Card> c_heart22 = Optional.of(c_heart2);

        Card c_heart3 = new Card();
        c_heart3.setId(3);
        c_heart3.setBody(true);
        c_heart3.setPlayed(false);
        c_heart3.setCardVaccine(null);
        c_heart3.setCardVirus(null);
        c_heart3.setVaccines(new ArrayList<>());
        c_heart3.setVirus(new ArrayList<>());
        c_heart3.setType(heart2);
        Optional<Card> c_heart33 = Optional.of(c_heart3);

        Card c_heart4 = new Card();
        c_heart2.setId(4);
        c_heart2.setBody(false);
        c_heart2.setPlayed(false);
        c_heart2.setCardVaccine(null);
        c_heart2.setCardVirus(null);
        c_heart2.setVaccines(new ArrayList<>());
        c_heart2.setVirus(new ArrayList<>());
        c_heart2.setType(heart3);
        Optional<Card> c_heart44 = Optional.of(c_heart4);

        
        Card c_stomach = new Card();
        c_stomach.setId(2);
        c_stomach.setBody(true);
        c_stomach.setPlayed(false);
        c_stomach.setCardVaccine(null);
        c_stomach.setCardVirus(null);
        c_stomach.setVaccines(new ArrayList<>());
        c_stomach.setVirus(new ArrayList<>());
        c_stomach.setType(stomach);
        Optional<Card> c_stomach1 = Optional.of(c_stomach);

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

        GamePlayer gp3 = new GamePlayer();
        gp3.setId(2);
        gp3.setHost(true);
        gp3.setCards(List.of(c_heart2, c_stomach));
        gp3.setWinner(false);
        Optional<GamePlayer> gp3_o = Optional.of(gp3);

        GamePlayer gp4 = new GamePlayer();
        gp4.setId(3);
        gp4.setHost(true);
        gp4.setCards(List.of(c_heart3, c_heart4));
        gp4.setWinner(false);
        Optional<GamePlayer> gp4_o = Optional.of(gp4);

        g.setGamePlayer(List.of(gp1,gp2,gp3));

        when(gameServ.findGames(0)).thenReturn(g);
        when(cardService.findCard(0)).thenReturn(c_heart1);
        when(cardService.findCard(1)).thenReturn(c_heart22);
        when(cardService.findCard(1)).thenReturn(c_heart44);
        when(cardService.findCard(2)).thenReturn(c_stomach1);
        when(cardService.findCard(3)).thenReturn(c_heart33);
        when(gamePlayerServ.findById(0)).thenReturn(gp1_o);
        when(gamePlayerServ.findById(1)).thenReturn(gp2_o);
        when(gamePlayerServ.findById(2)).thenReturn(gp3_o);
        when(gamePlayerServ.findById(3)).thenReturn(gp4_o);
        when(cardService.getBodyFromAGamePlayer(0)).thenReturn(List.of(c_heart));
        when(cardService.getBodyFromAGamePlayer(1)).thenReturn(new ArrayList<>());
        when(cardService.getBodyFromAGamePlayer(2)).thenReturn(List.of(c_stomach));
        when(cardService.getBodyFromAGamePlayer(3)).thenReturn(List.of(c_heart3));
    }

    GameController gc = new GameController(gameServ, gamePlayerServ, cardService);

    //Jugar un órgano corazón sin tener ningún organo
    @Test
    public void testPlayOrganPositive1() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService);
        assertEquals("/games/"+0+"/gamePlayer/"+1+"/decision", gc.playOrgan(0, 0, 0, 0));

    }


    //Jugar un órgano corazón teniendo un cerebro
    @Test
    public void testPlayOrganPositive2() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService);
        assertEquals("/games/"+0+"/gamePlayer/"+1+"/decision", gc.playOrgan(0, 2, 2, 1));
    }

    //Jugar un órgano corazón a otro jugador
    @Test
    public void testPlayOrganPositive3() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService);
        assertEquals("/games/"+0+"/gamePlayer/"+1+"/decision", gc.playOrgan(0, 0, 1, 0));
    }

    //Jugar un órgano corazón teniendo un corazón en el cuerpo
    @Test
    public void testPlayOrganNegativo1() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService);
        assertEquals("/games/"+0+"/gamePlayer/"+3+"/decision", gc.playOrgan(0, 3, 3, 4));
    }

    //Jugar un órgano corazón a otro que ya tiene un corazón en el cuerpo
    @Test
    public void testPlayOrganNegativo2() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService);
        assertEquals("/games/"+0+"/gamePlayer/"+0+"/decision", gc.playOrgan(0, 0, 3, 0));
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

