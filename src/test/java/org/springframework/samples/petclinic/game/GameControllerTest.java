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
import org.springframework.samples.petclinic.room.RoomService;
import org.springframework.samples.petclinic.util.AuthenticationService;
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

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private CardService cardService;

    @MockBean
    private GenericCardService gCardService;

    @MockBean
    private RoomService roomService;

    @MockBean
    private GameRepository gameRepo;

    @MockBean
    private GenericCardRepository gCardRepo;

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

        GenericCard heart_N1 = new GenericCard();
        heart_N1.setId(4);
        heart_N1.setColour(Colour.RED);
        heart_N1.setType(Type.ORGAN);

        GenericCard stomach = new GenericCard();
        stomach.setId(2);
        stomach.setColour(Colour.GREEN);
        stomach.setType(Type.ORGAN);

        GenericCard virusVP1 = new GenericCard();
        virusVP1.setId(5);
        virusVP1.setColour(Colour.YELLOW);
        virusVP1.setType(Type.VIRUS);

        GenericCard boneVP1 = new GenericCard();
        boneVP1.setId(6);
        boneVP1.setColour(Colour.YELLOW);
        boneVP1.setType(Type.ORGAN);

        GenericCard virusVP2 = new GenericCard();
        virusVP2.setId(8);
        virusVP2.setColour(Colour.YELLOW);
        virusVP2.setType(Type.VIRUS);

        GenericCard boneVP2 = new GenericCard();
        boneVP2.setId(7);
        boneVP2.setColour(Colour.YELLOW);
        boneVP2.setType(Type.ORGAN);

        GenericCard boneVP3 = new GenericCard();
        boneVP3.setId(9);
        boneVP3.setColour(Colour.YELLOW);
        boneVP3.setType(Type.ORGAN);

        GenericCard vaxVP3 = new GenericCard();
        vaxVP3.setId(10);
        vaxVP3.setColour(Colour.YELLOW);
        vaxVP3.setType(Type.VACCINE);

        GenericCard vaxVN21 = new GenericCard();
        vaxVN21.setId(11);
        vaxVN21.setColour(Colour.YELLOW);
        vaxVN21.setType(Type.VACCINE);

        GenericCard vaxVN22 = new GenericCard();
        vaxVN22.setId(12);
        vaxVN22.setColour(Colour.YELLOW);
        vaxVN22.setType(Type.VACCINE);

        GenericCard boneVN2 = new GenericCard();
        boneVN2.setId(13);
        boneVN2.setColour(Colour.YELLOW);
        boneVN2.setType(Type.ORGAN);

        GenericCard brainVXP1 = new GenericCard();
        brainVXP1.setId(14);
        brainVXP1.setColour(Colour.BLUE);
        brainVXP1.setType(Type.ORGAN);

        GenericCard vaxVXP1 = new GenericCard();
        vaxVXP1.setId(15);
        vaxVXP1.setColour(Colour.BLUE);
        vaxVXP1.setType(Type.VACCINE);

        GenericCard virusVXP2 = new GenericCard();
        virusVXP2.setId(16);
        virusVXP2.setColour(Colour.BLUE);
        virusVXP2.setType(Type.VIRUS);

        GenericCard brainVXP2 = new GenericCard();
        brainVXP2.setId(17);
        brainVXP2.setColour(Colour.BLUE);
        brainVXP2.setType(Type.ORGAN);

        GenericCard vaxVXN2 = new GenericCard();
        vaxVXN2.setId(18);
        vaxVXN2.setColour(Colour.BLUE);
        vaxVXN2.setType(Type.VACCINE);

        GenericCard vax2VXN2 = new GenericCard();
        vax2VXN2.setId(19);
        vax2VXN2.setColour(Colour.BLUE);
        vax2VXN2.setType(Type.VACCINE);

        GenericCard brainVXN2 = new GenericCard();
        brainVXN2.setId(20);
        brainVXN2.setColour(Colour.BLUE);
        brainVXN2.setType(Type.ORGAN);

        GenericCard transplant = new GenericCard();
        transplant.setId(21);
        transplant.setColour(Colour.RAINBOW);
        transplant.setType(Type.TRANSPLANT);

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

        Card c_heart_N1 = new Card();
        c_heart_N1.setId(4);
        c_heart_N1.setBody(false);
        c_heart_N1.setPlayed(false);
        c_heart_N1.setCardVaccine(null);
        c_heart_N1.setCardVirus(null);
        c_heart_N1.setVaccines(new ArrayList<>());
        c_heart_N1.setVirus(new ArrayList<>());
        c_heart_N1.setType(heart_N1);
        Optional<Card> c_heartN1 = Optional.of(c_heart_N1);

        
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

        Card c_virusVP1 = new Card();
        c_virusVP1.setId(5);
        c_virusVP1.setBody(false);
        c_virusVP1.setPlayed(false);
        c_virusVP1.setCardVaccine(null);
        c_virusVP1.setCardVirus(null);
        c_virusVP1.setVaccines(new ArrayList<>());
        c_virusVP1.setVirus(new ArrayList<>());
        c_virusVP1.setType(virusVP1);
        Optional<Card> c_virusVP1O = Optional.of(c_virusVP1);

        Card c_boneVP1 = new Card();
        c_boneVP1.setId(6);
        c_boneVP1.setBody(true);
        c_boneVP1.setPlayed(false);
        c_boneVP1.setCardVaccine(null);
        c_boneVP1.setCardVirus(null);
        c_boneVP1.setVaccines(new ArrayList<>());
        c_boneVP1.setVirus(new ArrayList<>());
        c_boneVP1.setType(boneVP1);
        Optional<Card> c_boneVP1O = Optional.of(c_boneVP1);

        Card c_virusVP2 = new Card();
        c_virusVP2.setId(8);
        c_virusVP2.setBody(false);
        c_virusVP2.setPlayed(false);
        c_virusVP2.setCardVaccine(null);
        c_virusVP2.setCardVirus(null);
        c_virusVP2.setVaccines(new ArrayList<>());
        c_virusVP2.setVirus(new ArrayList<>());
        c_virusVP2.setType(virusVP2);
        Optional<Card> c_virusVP2O = Optional.of(c_virusVP2);

        Card c_boneVP2 = new Card();
        c_boneVP2.setId(7);
        c_boneVP2.setBody(true);
        c_boneVP2.setPlayed(false);
        c_boneVP2.setCardVaccine(null);
        c_boneVP2.setCardVirus(null);
        c_boneVP2.setVaccines(new ArrayList<>());
        c_boneVP2.setVirus(List.of(c_virusVP2));
        c_boneVP2.setType(boneVP2);
        Optional<Card> c_boneVP2O = Optional.of(c_boneVP2);
        c_virusVP2.setCardVirus(c_boneVP2);

        Card c_vaxVP3 = new Card();
        c_vaxVP3.setId(9);
        c_vaxVP3.setBody(false);
        c_vaxVP3.setPlayed(false);
        c_vaxVP3.setCardVaccine(null);
        c_vaxVP3.setCardVirus(null);
        c_vaxVP3.setVaccines(new ArrayList<>());
        c_vaxVP3.setVirus(new ArrayList<>());
        c_vaxVP3.setType(vaxVP3);
        Optional<Card> c_vaxVP3O = Optional.of(c_vaxVP3);

        Card c_boneVP3 = new Card();
        c_boneVP3.setId(10);
        c_boneVP3.setBody(true);
        c_boneVP3.setPlayed(false);
        c_boneVP3.setCardVaccine(null);
        c_boneVP3.setCardVirus(null);
        c_boneVP3.setVaccines(List.of(c_vaxVP3));
        c_boneVP3.setVirus(new ArrayList<>());
        c_boneVP3.setType(boneVP3);
        Optional<Card> c_boneVP3O = Optional.of(c_boneVP3);
        c_vaxVP3.setCardVaccine(c_boneVP3);

        Card c_vaxVN21 = new Card();
        c_vaxVN21.setId(11);
        c_vaxVN21.setBody(false);
        c_vaxVN21.setPlayed(false);
        c_vaxVN21.setCardVaccine(null);
        c_vaxVN21.setCardVirus(null);
        c_vaxVN21.setVaccines(new ArrayList<>());
        c_vaxVN21.setVirus(new ArrayList<>());
        c_vaxVN21.setType(vaxVN21);
        Optional<Card> c_vaxVN21O = Optional.of(c_vaxVN21);

        Card c_vaxVN22 = new Card();
        c_vaxVN22.setId(12);
        c_vaxVN22.setBody(false);
        c_vaxVN22.setPlayed(false);
        c_vaxVN22.setCardVaccine(null);
        c_vaxVN22.setCardVirus(null);
        c_vaxVN22.setVaccines(new ArrayList<>());
        c_vaxVN22.setVirus(new ArrayList<>());
        c_vaxVN22.setType(vaxVN22);
        Optional<Card> c_vaxVN22O = Optional.of(c_vaxVN22);

        Card c_boneVN2 = new Card();
        c_boneVN2.setId(13);
        c_boneVN2.setBody(true);
        c_boneVN2.setPlayed(false);
        c_boneVN2.setCardVaccine(null);
        c_boneVN2.setCardVirus(null);
        c_boneVN2.setVaccines(List.of(c_vaxVN21,c_vaxVN22));
        c_boneVN2.setVirus(new ArrayList<>());
        c_boneVN2.setType(boneVN2);
        Optional<Card> c_boneVN2O = Optional.of(c_boneVN2);
        c_vaxVN21.setCardVaccine(c_boneVN2);
        c_vaxVN22.setCardVaccine(c_boneVN2);

        Card c_brainVXP1 = new Card();
        c_brainVXP1.setId(14);
        c_brainVXP1.setBody(true);
        c_brainVXP1.setPlayed(false);
        c_brainVXP1.setCardVaccine(null);
        c_brainVXP1.setCardVirus(null);
        c_brainVXP1.setVaccines(new ArrayList<>());
        c_brainVXP1.setVirus(new ArrayList<>());
        c_brainVXP1.setType(brainVXP1);
        Optional<Card> c_brainVXP1O = Optional.of(c_brainVXP1);

        Card c_vaxVXP1 = new Card();
        c_vaxVXP1.setId(15);
        c_vaxVXP1.setBody(false);
        c_vaxVXP1.setPlayed(false);
        c_vaxVXP1.setCardVaccine(null);
        c_vaxVXP1.setCardVirus(null);
        c_vaxVXP1.setVaccines(new ArrayList<>());
        c_vaxVXP1.setVirus(new ArrayList<>());
        c_vaxVXP1.setType(vaxVXP1);
        Optional<Card> c_vaxVXP1O = Optional.of(c_vaxVXP1);

        Card c_virusVXP2 = new Card();
        c_virusVXP2.setId(16);
        c_virusVXP2.setBody(false);
        c_virusVXP2.setPlayed(false);
        c_virusVXP2.setCardVaccine(null);
        c_virusVXP2.setCardVirus(null);
        c_virusVXP2.setVaccines(new ArrayList<>());
        c_virusVXP2.setVirus(new ArrayList<>());
        c_virusVXP2.setType(virusVXP2);
        Optional<Card> c_virusVXP2O = Optional.of(c_virusVXP2);

        Card c_brainVXP2 = new Card();
        c_brainVXP2.setId(17);
        c_brainVXP2.setBody(true);
        c_brainVXP2.setPlayed(false);
        c_brainVXP2.setCardVaccine(null);
        c_brainVXP2.setCardVirus(null);
        c_brainVXP2.setVaccines(new ArrayList<>());
        c_brainVXP2.setVirus(List.of(c_virusVXP2));
        c_brainVXP2.setType(brainVXP2);
        Optional<Card> c_brainVXP2O = Optional.of(c_brainVXP2);
        c_virusVXP2.setCardVirus(c_brainVXP2);

        Card c_vaxVXN2 = new Card();
        c_vaxVXN2.setId(18);
        c_vaxVXN2.setBody(false);
        c_vaxVXN2.setPlayed(false);
        c_vaxVXN2.setCardVaccine(null);
        c_vaxVXN2.setCardVirus(null);
        c_vaxVXN2.setVaccines(new ArrayList<>());
        c_vaxVXN2.setVirus(new ArrayList<>());
        c_vaxVXN2.setType(vaxVXN2);
        Optional<Card> c_vaxVXN2O = Optional.of(c_vaxVXN2);

        Card c_vax2VXN2 = new Card();
        c_vax2VXN2.setId(19);
        c_vax2VXN2.setBody(false);
        c_vax2VXN2.setPlayed(false);
        c_vax2VXN2.setCardVaccine(null);
        c_vax2VXN2.setCardVirus(null);
        c_vax2VXN2.setVaccines(new ArrayList<>());
        c_vax2VXN2.setVirus(new ArrayList<>());
        c_vax2VXN2.setType(vax2VXN2);
        Optional<Card> c_vax2VXN2O = Optional.of(c_vax2VXN2);

        Card c_brainVXN2 = new Card();
        c_brainVXN2.setId(20);
        c_brainVXN2.setBody(true);
        c_brainVXN2.setPlayed(false);
        c_brainVXN2.setCardVaccine(null);
        c_brainVXN2.setCardVirus(null);
        c_brainVXN2.setVaccines(List.of(c_vaxVXN2, c_vax2VXN2));
        c_brainVXN2.setVirus(new ArrayList<>());
        c_brainVXN2.setType(brainVXN2);
        Optional<Card> c_brainVXN2O = Optional.of(c_brainVXN2);
        c_vaxVXN2.setCardVaccine(c_brainVXN2);
        c_vax2VXN2.setCardVaccine(c_brainVXN2);
 
        Card c_transplant = new Card();
        c_transplant.setId(21);
        c_transplant.setBody(false);
        c_transplant.setPlayed(false);
        c_transplant.setCardVaccine(null);
        c_transplant.setCardVirus(null);
        c_transplant.setVaccines(new ArrayList<>());
        c_transplant.setVirus(new ArrayList<>());
        c_transplant.setType(transplant);
        Optional<Card> c_transplantO = Optional.of(c_transplant);
  

        Game g = new Game();
        g.setCards(new ArrayList<>());
        g.setClassification(new HashMap<>());
        g.setIsRunning(true);
        g.setId(0);
        g.setTurn(0);
        g.setRound(0);
        g.setInitialHour(LocalDateTime.now());

        Game g2 = new Game();
        g2.setCards(new ArrayList<>());
        g2.setClassification(new HashMap<>());
        g2.setIsRunning(true);
        g2.setId(1);
        g2.setTurn(0);
        g2.setRound(0);
        g2.setInitialHour(LocalDateTime.now());

        Game g3 = new Game();
        g3.setCards(new ArrayList<>());
        g3.setClassification(new HashMap<>());
        g3.setIsRunning(true);
        g3.setId(2);
        g3.setTurn(0);
        g3.setRound(0);
        g3.setInitialHour(LocalDateTime.now());

        Game g4 = new Game();
        g4.setCards(new ArrayList<>());
        g4.setClassification(new HashMap<>());
        g4.setIsRunning(true);
        g4.setId(3);
        g4.setTurn(0);
        g4.setRound(0);
        g4.setInitialHour(LocalDateTime.now());


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
        gp4.setCards(List.of(c_heart3, c_heart_N1));
        gp4.setWinner(false);
        Optional<GamePlayer> gp4_o = Optional.of(gp4);
        c_heart3.setGamePlayer(gp4);

        GamePlayer gp5 = new GamePlayer();
        gp5.setId(4);
        gp5.setHost(true);
        gp5.setCards(List.of(c_virusVP1));
        gp5.setWinner(false);
        Optional<GamePlayer> gp5_o = Optional.of(gp5);

        GamePlayer gp6 = new GamePlayer();
        gp6.setId(5);
        gp6.setHost(true);
        gp6.setCards(List.of(c_boneVP1));
        gp6.setWinner(false);
        Optional<GamePlayer> gp6_o = Optional.of(gp6);
        c_boneVP1.setGamePlayer(gp6);

        GamePlayer gp7 = new GamePlayer();
        gp7.setId(6);
        gp7.setHost(true);
        gp7.setCards(List.of(c_boneVP2));
        gp7.setWinner(false);
        Optional<GamePlayer> gp7_o = Optional.of(gp7);
        c_boneVP1.setGamePlayer(gp7);

        GamePlayer gp8 = new GamePlayer();
        gp8.setId(7);
        gp8.setHost(true);
        gp8.setCards(List.of(c_boneVP3, c_vaxVP3));
        gp8.setWinner(false);
        Optional<GamePlayer> gp8_o = Optional.of(gp8);
        c_boneVP3.setGamePlayer(gp8);
        c_vaxVP3.setGamePlayer(gp8);

        GamePlayer gp9 = new GamePlayer();
        gp9.setId(8);
        gp9.setHost(true);
        gp9.setCards(List.of(c_boneVN2, c_vaxVN21, c_vaxVN22));
        gp9.setWinner(false);
        Optional<GamePlayer> gp9_o = Optional.of(gp9);
        c_boneVN2.setGamePlayer(gp9);
        c_vaxVN21.setGamePlayer(gp9);
        c_vaxVN22.setGamePlayer(gp9);

        GamePlayer gp10 = new GamePlayer();
        gp10.setId(9);
        gp10.setHost(true);
        gp10.setCards(List.of(c_brainVXP1, c_vaxVXP1));
        gp10.setWinner(false);
        Optional<GamePlayer> gp10_o = Optional.of(gp10);

        GamePlayer gp11 = new GamePlayer();
        gp11.setId(10);
        gp11.setHost(true);
        gp11.setCards(List.of(c_brainVXP2, c_virusVXP2));
        gp11.setWinner(false);
        Optional<GamePlayer> gp11_o = Optional.of(gp11);
        c_brainVXP2.setGamePlayer(gp11);

        GamePlayer gp12 = new GamePlayer();
        gp12.setId(11);
        gp12.setHost(true);
        gp12.setCards(List.of(c_brainVXN2, c_vax2VXN2, c_vaxVXN2));
        gp12.setWinner(false);
        Optional<GamePlayer> gp12_o = Optional.of(gp12);

        GamePlayer gp13 = new GamePlayer();
        gp13.setId(12);
        gp13.setHost(true);
        gp13.setCards(List.of(c_transplant));
        gp13.setWinner(false);
        Optional<GamePlayer> gp13_o = Optional.of(gp13);
        c_transplant.setGamePlayer(gp13);
 
        g.setGamePlayer(List.of(gp1,gp2,gp3,gp4));
        g2.setGamePlayer(List.of(gp5,gp6,gp7,gp3,gp8,gp9));
        g3.setGamePlayer(List.of(gp10,gp1,gp11,gp12,gp13,gp4));
        g4.setGamePlayer(List.of(gp13,gp4,gp11, gp12));

        when(gameServ.findGames(0)).thenReturn(g);
        when(gameServ.findGames(1)).thenReturn(g2);
        when(gameServ.findGames(2)).thenReturn(g3);
        when(gameServ.findGames(3)).thenReturn(g4);
        when(cardService.findCard(0)).thenReturn(c_heart1);
        when(cardService.findCard(1)).thenReturn(c_heart22);
        when(cardService.findCard(4)).thenReturn(c_heartN1);
        when(cardService.findCard(2)).thenReturn(c_stomach1);
        when(cardService.findCard(3)).thenReturn(c_heart33);
        when(cardService.findCard(5)).thenReturn(c_virusVP1O);
        when(cardService.findCard(6)).thenReturn(c_boneVP1O);
        when(cardService.findCard(8)).thenReturn(c_virusVP2O);
        when(cardService.findCard(7)).thenReturn(c_boneVP2O);
        when(cardService.findCard(9)).thenReturn(c_vaxVP3O);
        when(cardService.findCard(10)).thenReturn(c_boneVP3O);
        when(cardService.findCard(11)).thenReturn(c_vaxVN21O);
        when(cardService.findCard(12)).thenReturn(c_vaxVN22O);
        when(cardService.findCard(13)).thenReturn(c_boneVN2O);
        when(cardService.findCard(14)).thenReturn(c_brainVXP1O);
        when(cardService.findCard(15)).thenReturn(c_vaxVXP1O);
        when(cardService.findCard(16)).thenReturn(c_virusVXP2O);
        when(cardService.findCard(17)).thenReturn(c_brainVXP2O);
        when(cardService.findCard(18)).thenReturn(c_vaxVXN2O);
        when(cardService.findCard(19)).thenReturn(c_vax2VXN2O);
        when(cardService.findCard(20)).thenReturn(c_brainVXN2O);
        when(cardService.findCard(21)).thenReturn(c_transplantO);
        when(gamePlayerServ.findById(0)).thenReturn(gp1_o);
        when(gamePlayerServ.findById(1)).thenReturn(gp2_o);
        when(gamePlayerServ.findById(2)).thenReturn(gp3_o);
        when(gamePlayerServ.findById(3)).thenReturn(gp4_o);
        when(gamePlayerServ.findById(4)).thenReturn(gp5_o);
        when(gamePlayerServ.findById(5)).thenReturn(gp6_o);
        when(gamePlayerServ.findById(6)).thenReturn(gp7_o);
        when(gamePlayerServ.findById(7)).thenReturn(gp8_o);
        when(gamePlayerServ.findById(8)).thenReturn(gp9_o);
        when(gamePlayerServ.findById(9)).thenReturn(gp10_o);
        when(gamePlayerServ.findById(10)).thenReturn(gp11_o);
        when(gamePlayerServ.findById(11)).thenReturn(gp12_o);
        when(gamePlayerServ.findById(12)).thenReturn(gp13_o);
        when(cardService.getBodyFromAGamePlayer(0)).thenReturn(new ArrayList<>());
        when(cardService.getBodyFromAGamePlayer(1)).thenReturn(new ArrayList<>());
        when(cardService.getBodyFromAGamePlayer(2)).thenReturn(List.of(c_stomach));
        when(cardService.getBodyFromAGamePlayer(3)).thenReturn(List.of(c_heart3));
        when(cardService.getBodyFromAGamePlayer(4)).thenReturn((new ArrayList<>()));
        when(cardService.getBodyFromAGamePlayer(5)).thenReturn(List.of(c_boneVP1));
    }

   

    //Jugar un órgano corazón sin tener ningún organo
    @Test
    public void testPlayOrganPositive1() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+0+"/gamePlayer/"+1+"/decision", gc.playOrgan(0, 0, 0, 0));

    }


    //Jugar un órgano corazón teniendo un estómago
    @Test
    public void testPlayOrganPositive2() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+0+"/gamePlayer/"+1+"/decision", gc.playOrgan(0, 2, 2, 1));
    }

    //Jugar un órgano corazón a otro jugador
    @Test
    public void testPlayOrganPositive3() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+0+"/gamePlayer/"+1+"/decision", gc.playOrgan(0, 0, 1, 0));
    }

    //Jugar un órgano corazón teniendo un corazón en el cuerpo
    @Test
    public void testPlayOrganNegative1() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+0+"/gamePlayer/"+3+"/decision", gc.playOrgan(0, 3, 3, 4));
    }

    //Jugar un órgano corazón a otro que ya tiene un corazón en el cuerpo
    @Test
    public void testPlayOrganNegative2() {
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+0+"/gamePlayer/"+0+"/decision", gc.playOrgan(0, 0, 3, 0));
    }

    @Test
    public void testPlayVirusPositive1(){
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+1+"/gamePlayer/"+5+"/decision", gc.playVirus(1, 4, 5, 6));
    }

    @Test
    public void testPlayVirusPositive2(){
        GameController gc =new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+1+"/gamePlayer/"+5+"/decision", gc.playVirus(1, 4, 5, 7));
    }

    @Test
    public void testPlayVirusPositive3(){
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+1+"/gamePlayer/"+5+"/decision", gc.playVirus(1, 4, 5, 10));
    }

    @Test
    public void testPlayVirusNegative1(){
        GameController gc =new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+1+"/gamePlayer/"+4+"/decision", gc.playVirus(1, 4, 5, 2));
    }

    @Test
    public void testPlayVirusNegative2(){
        GameController gc =new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+1+"/gamePlayer/"+4+"/decision", gc.playVirus(1, 4, 5, 13));
    }

    @Test
    public void testPlayVaccinePositive1(){
        GameController gc =new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+2+"/gamePlayer/"+0+"/decision", gc.playVaccine(2, 9, 15, 14));
    }

    @Test
    public void testPlayVaccinePositive2(){
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+2+"/gamePlayer/"+0+"/decision", gc.playVaccine(2, 9, 15, 17));
    }

    @Test
    public void testPlayVaccineNegative1(){
        GameController gc =new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+2+"/gamePlayer/"+9+"/decision", gc.playVaccine(2, 9, 15, 0));
    }

    @Test
    public void testPlayVaccineNegative2(){
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+2+"/gamePlayer/"+9+"/decision", gc.playVaccine(2, 9, 15, 20));
    }

    @Test
    public void testPlayTransplantPositive1(){
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+3+"/gamePlayer/"+3+"/decision", gc.playTransplant(3, 12, 3, 17));
    }

    @Test
    public void testPlayTransplantNegative(){
        GameController gc = new GameController(gameServ, gamePlayerServ, cardService, gCardService, roomService, authenticationService);
        assertEquals("/games/"+3+"/gamePlayer/"+12+"/decision", gc.playTransplant(3, 12, 3, 20));
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