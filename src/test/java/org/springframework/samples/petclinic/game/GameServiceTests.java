package org.springframework.samples.petclinic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardRepository;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.card.GenericCard.Colour;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.room.Room;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;


//     @WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
// classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GameServiceTests {

    @Autowired
    GameService gs;

    @Autowired
    CardService cs;

    @MockBean
    GameRepository gr;

    @MockBean
    GamePlayerService gps;

    
    @MockBean
    CardRepository cr;

    //Elementos comunes a addOrgan
    GenericCard generic_heart =new GenericCard(1,Colour.RED, Type.ORGAN);
    GenericCard generic_stomach =new GenericCard(2,Colour.GREEN, Type.ORGAN);
    GenericCard generic_rainbow = new GenericCard(3, Colour.RAINBOW, Type.ORGAN);
    

    GamePlayer gp1 = new GamePlayer(0);
    GamePlayer gp2 = new GamePlayer(1); 


    Card organ_heart1 = new Card(0, false, gp1, generic_heart);
    Card organ_heart2 = new Card(1, false, gp2, generic_heart);
    Card organ_rainbow = new Card(3, false, gp2, generic_rainbow);
    Card organ_stomach = new Card(2, true, gp2, generic_stomach);
    
    //Elementos comunes a playTransplant
    GenericCard generic_brain =new GenericCard(3,Colour.BLUE, Type.ORGAN);
    GenericCard generic_BVaccine =new GenericCard(2,Colour.BLUE, Type.VACCINE);
    GenericCard generic_BVaccine2 =new GenericCard(3,Colour.BLUE, Type.VACCINE);
    Card organ_brain = new Card(1, true, gp2, generic_brain);
    Card vax_brain = new Card(2, false, gp2, generic_BVaccine);
    Card vax_brain2 = new Card(3, false, gp2, generic_BVaccine2);

    //Elementos comunes a thief
    GenericCard generic_thief =new GenericCard(10,Colour.RAINBOW, Type.THIEF);
    Card thief =  new Card(9, false, gp2, generic_BVaccine2);

    //Elementos comunes a playInfection
    GenericCard generic_HVirus = new GenericCard(3,Colour.RED, Type.VIRUS);
    GenericCard generic_BVirus = new GenericCard(4,Colour.BLUE, Type.VIRUS);
    GenericCard generic_brain2 =new GenericCard(5,Colour.BLUE, Type.ORGAN);
    GenericCard generic_HVaccine =new GenericCard(6,Colour.RED, Type.VACCINE);
    GenericCard generic_HVaccine2 =new GenericCard(7,Colour.RED, Type.VACCINE);
    GenericCard generic_HVirus2 = new GenericCard(8,Colour.RED, Type.VIRUS);
    GenericCard generic_HVirus4 = new GenericCard(8,Colour.RAINBOW, Type.VIRUS);
    GenericCard generic_infection = new GenericCard(9,Colour.RAINBOW, Type.INFECTION);
    GamePlayer gp3 = new GamePlayer(2);
    GamePlayer gp4 = new GamePlayer(3);
    Card virus_heart = new Card(2, false, gp1, generic_HVirus);
    Card virus_heart2 = new Card(7, false, gp2, generic_HVirus2);
    Card virus_brain = new Card(3, false, gp1, generic_BVirus);
    Card virus_rainbow = new Card(10, false, gp3, generic_HVirus4);
    Card organ_brain2 = new Card(4, true, gp1, generic_brain2);
    Card vax_heart = new Card(5, false, gp2, generic_HVaccine);
    Card vax_heart2 = new Card(6, false, gp2, generic_HVaccine2);
    Card infection = new Card(8,false,gp3,generic_infection);

    //Elementos para testClasificate
    GamePlayer gp5 = new GamePlayer(4);
    GamePlayer gp6 = new GamePlayer(5);
    GamePlayer gp7 = new GamePlayer(6);
    GenericCard generic_bone = new GenericCard(12,Colour.YELLOW, Type.ORGAN);
    Card organ_bone = new Card(13,true,gp2,generic_bone);
    Card organ_heart3 = new Card(17, true, gp3, generic_heart);
    Card organ_stomach2 = new Card(16, true, gp3, generic_stomach);
    Card organ_bone2 = new Card(15,true,gp3,generic_bone);
    Card organ_heart4 = new Card(18, true, gp4, generic_heart);
    Card organ_stomach3 = new Card(19, true, gp4, generic_stomach);
    Card organ_bone3 = new Card(20,true,gp5,generic_bone);
    Card organ_stomach4 = new Card(19, true, gp5, generic_stomach);
    Card organ_heart5 = new Card(21, true, gp6, generic_heart);
    Card organ_stomach5 = new Card(19, true, gp7, generic_stomach);

    Game g = new Game();
    List<GamePlayer> gamePlayers = new ArrayList<>();
    List<Card> cards = new ArrayList<>();
    ModelMap m = new ModelMap();

    @BeforeEach
    void setup(){
        g.setCards(new ArrayList<>());
        g.setIsRunning(true);
        g.setId(0);
        List<GamePlayer> gplayers = new ArrayList<>();
        gplayers.add(gp1);
        gplayers.add(gp2);
        g.setGamePlayer(gplayers);
        g.setInitialHour(LocalDateTime.now());
        organ_heart1.setBody(false);
        cards.add(organ_heart1);
    }


    @Test
    //Jugar un corazón con el cuerpo vacío
    public void testAddOrganPositive1() {
        //setup
        gp1.setCards(cards);
        //test
        gs.addOrgan(organ_heart1, gp1, gp1, m);
        assertEquals(1, gp1.getBody().size());
        assertEquals(0, gp1.getHand().size());
    }
    @Test
    //Jugar un corazón con un estómago ya en el cuerpo
    public void testAddOrganPositive2() {
        //setup
        cards.add(organ_stomach);
        gp1.setCards(cards);
        //test
        gs.addOrgan(organ_heart1, gp1, gp1, m);
        assertEquals(2, gp1.getBody().size());
        assertEquals(0, gp1.getHand().size());
    }

    @Test
    //Jugar un organo arcoíris con un estómago ya en el cuerpo
    public void testAddOrganPositive3() {
        //setup
        cards.add(organ_stomach);
        cards.add(organ_rainbow);
        gp1.setCards(cards);
        //test
        gs.addOrgan(organ_rainbow, gp1, gp1, m);
        assertEquals(2, gp1.getBody().size());
        assertEquals(1, gp1.getHand().size());
    }

    @Test
    //Jugar un corazón con un estómago ya en el cuerpo de otra persona que también tiene un corazón en la mano
    public void testAddOrganPositive4() {
        //Setup
        gp1.setCards(cards);
        cards.clear();
        cards.add(organ_heart2);
        cards.add(organ_stomach);
        gp2.setCards(cards);
        //test
        gs.addOrgan(organ_heart1, gp1, gp2, m);
        assertEquals(2, gp2.getBody().size());
        assertEquals(1, gp2.getHand().size());
    }

    @Test
    //Jugar un corazón teniendo un corazón en el cuerpo
    public void testAddOrganNegative1() {
        //Setup
        organ_heart2.setBody(true);
        cards.add(organ_heart2);
        gp1.setCards(cards);
        //test
        assertThrows(IllegalArgumentException.class , ()-> gs.addOrgan(organ_heart1, gp1, gp1, m));
}
    @Test
    //Jugar un corazón a otro que ya tiene un corazón en el cuerpo
    public void testAddOrganNegative2() {
        //Setup
        gp1.setCards(cards);
        cards.clear();
        organ_heart2.setBody(true);
        cards.add(organ_heart2);
        gp2.setCards(cards);
        //test
        assertThrows(IllegalArgumentException.class , ()-> gs.addOrgan(organ_heart1, gp1, gp2, m));
    }

    @Test
    //Jugar una carta de trasplante sobre un cerebro.
    public void testTransplantPositive1() {
        //Setup
        organ_heart1.setBody(true);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_brain.setBody(true);
        organ_brain.setGamePlayer(gp2);
        gp2.getBody().remove(organ_heart2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_brain);
        gp2.setCards(cards2);
        //test
        gs.changeCards(gp1, gp2, organ_heart1, organ_brain);
        assertEquals(true, gp1.getBody().get(0).getType().getColour()==GenericCard.Colour.BLUE);
        assertEquals(true, gp2.getBody().get(0).getType().getColour()==GenericCard.Colour.RED);
    }

    @Test
    //Jugar una carta de trasplante sobre un cerebro vacunado.
    public void testTransplantPositive2() {
        //Setup
        organ_heart1.setBody(true);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_brain.setBody(true);
        List<Card> vax = new ArrayList<>();
        vax.add(vax_brain);
        organ_brain.setVaccines(vax);
        organ_brain.setGamePlayer(gp2);
        gp2.getBody().remove(organ_heart2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_brain);
        gp2.setCards(cards2);
        //test
        gs.changeCards(gp1, gp2, organ_heart1, organ_brain);
        assertEquals(true, gp1.getBody().get(0).getType().getColour()==GenericCard.Colour.BLUE);
        assertEquals(true, gp2.getBody().get(0).getType().getColour()==GenericCard.Colour.RED);
    }

    @Test
    //Jugar una carta de trasplante sobre un corazón teniendo ya uno.
    public void testTransplantNegative1() {
        //Setup
        organ_heart1.setBody(true);
        organ_brain.setBody(true);
        cards.add(organ_brain);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_brain.setGamePlayer(gp1);
        organ_heart2.setBody(true);
        organ_heart2.setGamePlayer(gp2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_heart2);
        gp2.setCards(cards2);
        //test 
        assertThrows(IllegalArgumentException.class , ()-> gs.changeCards(gp1,gp2,organ_brain,organ_heart2));
    }

    @Test
    //Jugar una carta de trasplante sobre un cerebro inmunizado.
    public void testTransplantNegative2() {
        //Setup
        organ_heart1.setBody(true);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_brain.setBody(true);
        List<Card> vax = new ArrayList<>();
        vax.add(vax_brain);
        vax.add(vax_brain2);
        organ_brain.setVaccines(vax);
        organ_brain.setGamePlayer(gp2);
        gp2.getBody().remove(organ_heart2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_brain);
        gp2.setCards(cards2);
        //test 
        assertThrows(IllegalArgumentException.class , ()-> gs.changeCards(gp1,gp2,organ_heart1,organ_brain));
    }

    @BeforeEach
    void setup2(){
        thief.setGamePlayer(gp1);
        gp1.getCards().add(thief);
    }


    @Test
    //Jugar una carta de robo sobre un cerebro.
    public void testThiefPositive1() {
        //Setup
        organ_heart1.setBody(true);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_brain.setBody(true);
        organ_brain.setGamePlayer(gp2);
        gp2.getBody().remove(organ_heart2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_brain);
        gp2.setCards(cards2);
        //test
        gs.thief(thief, gp1, gp2, organ_brain);
        assertEquals(true, gp1.getBody().get(1).getType().getColour()==GenericCard.Colour.BLUE);
    }

    @Test
    //Jugar una carta de robo sobre un cerebro vacunado.
    public void testThiefPositive2() {
        //Setup
        organ_heart1.setBody(true);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_brain.setBody(true);
        List<Card> vax = new ArrayList<>();
        vax.add(vax_brain);
        organ_brain.setVaccines(vax);
        organ_brain.setGamePlayer(gp2);
        gp2.getBody().remove(organ_heart2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_brain);
        gp2.setCards(cards2);
        //test
        gs.thief(thief, gp1,gp2, organ_brain);
        assertEquals(true, gp1.getBody().get(1).getType().getColour()==GenericCard.Colour.BLUE);
    }

    @Test
    //Jugar una carta de robo sobre un corazón teniendo ya uno.
    public void testThiefNegative1() {
        //Setup
        organ_heart1.setBody(true);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_heart2.setBody(true);
        organ_heart2.setGamePlayer(gp2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_heart2);
        gp2.setCards(cards2);
        //test 
        assertThrows(IllegalArgumentException.class , ()-> gs.thief(thief, gp1, gp2, organ_heart2));
    }

    @Test
    //Jugar una carta de robo sobre un cerebro inmunizado.
    public void testThiefNegative2() {
        //Setup
        organ_brain.setBody(true);
        List<Card> vax = new ArrayList<>();
        vax_brain.setGamePlayer(gp2);
        vax_brain2.setGamePlayer(gp2);
        vax.add(vax_brain);
        vax.add(vax_brain2);
        organ_brain.setVaccines(vax);
        organ_brain.setGamePlayer(gp2);
        gp2.getCards().add(organ_brain);
        //test 
        assertThrows(IllegalArgumentException.class , ()-> gs.thief(thief, gp1, gp2, organ_brain));
    }

    @BeforeEach
    void setup3(){
        gp3.getCards().add(infection);
        infection.setGamePlayer(gp3);
        setup3(virus_heart,organ_heart1,gp3);
        
    }
    
    public void setup3(Card virus_or_vaccine, Card organ, GamePlayer gp){
        organ.setBody(true);
        organ.setGamePlayer(gp3);
        virus_or_vaccine.setGamePlayer(gp);
        if(virus_or_vaccine.getType().getType()==Type.VIRUS){
            virus_or_vaccine.setCardVirus(organ);
            organ.getVirus().add(virus_or_vaccine);
        }else{
        virus_or_vaccine.setCardVaccine(organ);
        organ.getVaccines().add(virus_or_vaccine);
        }
        gp.getCards().add(organ);
        gp.getCards().add(virus_or_vaccine);
    } 

    @Test
    //Jugar una carta de contagio con un virus de corazón a otro corazón limpio.
    public void testInfectionPositive1() {
        //Setup
        organ_heart2.setBody(true);
        organ_heart2.setGamePlayer(gp4);
        gp4.getCards().add(organ_heart2);
        //test
        gs.infection(gp3, gp4);
        assertEquals(true, gp4.getBody().get(0).getVirus().size()==1);
    }

    @Test
    //Jugar una carta de contagio con un virus de corazón y uno de cerebro a un corazón y un cerebro limpio.
    public void testInfectionPositive2() {
        //Setup
        setup3(virus_brain,organ_brain,gp3);
        organ_brain2.setGamePlayer(gp4);
        organ_brain2.setBody(true);
        organ_heart2.setGamePlayer(gp4);
        organ_heart2.setBody(true);
        gp4.getCards().add(organ_brain2);
        gp4.getCards().add(organ_heart2);
        
        //test
        gs.infection(gp3, gp4);
        assertEquals(true, gp4.getBody().get(0).getVirus().size()==1 && gp4.getBody().get(1).getVirus().size()==1);
    }

    @Test
    //Jugar una carta de contagio con un corazón y un cerebro contagiados a un corazón y un órgano arcoíris
    public void testInfectionPositive3(){
        //Setup
        setup3(virus_brain,organ_brain,gp3);
        organ_rainbow.setGamePlayer(gp4);
        organ_rainbow.setBody(true);
        organ_heart2.setGamePlayer(gp4);
        organ_heart2.setBody(true);
        gp4.getCards().add(organ_rainbow);
        gp4.getCards().add(organ_heart2);

         //test
         gs.infection(gp3, gp4);
         assertEquals(true, gp4.getBody().get(0).getVirus().size()==1 && gp4.getBody().get(1).getVirus().size()==1);
    }

    @Test
    //Jugar una carta de contagio con un corazón con virus y un cerebro (con virus arcoíris) a un corazón y un cerebro
    public void testInfectionPositive4(){
        //Setup
        setup3(virus_rainbow,organ_brain,gp3);
        organ_brain2.setGamePlayer(gp4);
        organ_brain2.setBody(true);
        organ_heart2.setGamePlayer(gp4);
        organ_heart2.setBody(true);
        gp4.getCards().add(organ_brain2);
        gp4.getCards().add(organ_heart2);

         //test
         gs.infection(gp3, gp4);
         assertEquals(true, gp4.getBody().get(0).getVirus().size()==1 && gp4.getBody().get(1).getVirus().size()==1);
    }



    @Test
    //Jugar una carta de guante de látex.
    public void testGlovePositive() {
        //SetUp
        List<Card> hand1 = new ArrayList<>();
        hand1.add(organ_heart1);
        hand1.add(organ_heart2);
        hand1.add(vax_brain);
        gp1.setCards(hand1);
        cards.add(organ_brain);
        cards.add(vax_heart);
        gp2.setCards(cards);
        //test
        gs.glove(gp1, g);
        assertEquals(true, gp2.getHand().size()==0);
    }

    @Test
    //Jugar una carta de guante de látex.
    public void testErrorPositive() {
        //SetUp
        List<Card> body = new ArrayList<>();
        body.add(organ_heart2);
        body.add(organ_brain);
        organ_brain.setBody(true);
        organ_heart2.setBody(true);
        organ_heart1.setBody(true);
        gp1.setCards(body);
        gp2.setCards(cards);
        //test
        gs.medicalError(gp1, gp2);
        assertEquals(true, gp1.getBody().size()==1 && gp2.getBody().size()==2);
    }

    public void classificateCommonMethod(Card c, GamePlayer gp){
        c.setBody(true);
        c.setGamePlayer(gp);
        gp.getCards().add(c);
    }


    //Clasifica los jugadores de una partida en primer,segundo,tercer o cuarto puesto
    @Test
    public void testClasificate(){
        //setup
        gp1.getCards().clear();
        classificateCommonMethod(organ_brain,gp1);
        organ_heart1.getVirus().clear();
        classificateCommonMethod(organ_heart1,gp1);
        classificateCommonMethod(organ_rainbow,gp1);
        classificateCommonMethod(organ_stomach,gp1);
        classificateCommonMethod(organ_brain2,gp2);
        classificateCommonMethod(organ_heart2,gp2);
        classificateCommonMethod(organ_bone,gp2);
        gp3.getCards().clear();
        classificateCommonMethod(organ_heart3,gp3);
        classificateCommonMethod(organ_stomach2,gp3);
        classificateCommonMethod(organ_bone2,gp3);
        classificateCommonMethod(organ_heart4,gp4);
        classificateCommonMethod(organ_stomach3,gp4);
        classificateCommonMethod(organ_bone3,gp5);
        classificateCommonMethod(organ_stomach4,gp5);
        classificateCommonMethod(organ_heart5,gp6);
        classificateCommonMethod(organ_stomach5,gp7);

        List<GamePlayer> gamePlayers = new ArrayList<>();
        gamePlayers.add(gp1);
        gamePlayers.add(gp2);
        gamePlayers.add(gp3);
        gamePlayers.add(gp4);
        gamePlayers.add(gp5);
        gamePlayers.add(gp6);
        gamePlayers.add(gp7);

        Map<Integer, List<GamePlayer>> classification = new HashMap<>();
        classification.put(1, List.of(gp1));
        classification.put(2, List.of(gp2,gp3));
        classification.put(3, List.of(gp4,gp5));
        classification.put(4, List.of(gp6,gp7));
        //test
        assertEquals(classification,gs.clasificate(gamePlayers));
    }

    @Test
    public void testStartGameAndDealCards(){
        //setup
        when(gr.findGamePlayerByPlayer(0)).thenReturn(gp6);
        when(gr.findGamePlayerByPlayer(1)).thenReturn(gp7);
        when(gps.save(gp7)).thenReturn(gp7);
        when(gr.save(any())).thenReturn(g);
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        player1.setId(0);
        Player player2 = new Player();
        player2.setId(1);
        players.add(player1);
        players.add(player2);
        Room room = new Room();
        room.setPlayers(players);
        //test
        assertEquals(67, gs.startGame(room).getCards().size());
        assertEquals(2, gs.startGame(room).getGamePlayer().size());
    }

    @Test
    public void testDealCardsAndFillDeck(){
        //setup
        gamePlayers.add(gp1);
        organ_heart1.setPlayed(true);
        organ_heart1.setGamePlayer(null);
        organ_heart2.setPlayed(true);
        organ_heart2.setGamePlayer(null);
        g.getCards().clear();
        g.setGamePlayer(gamePlayers);
        g.getCards().add(organ_heart1);
        g.getCards().add(organ_heart2);

        when(gr.save(g)).thenReturn(g);
        when(cr.save(any())).thenReturn(organ_heart1);
        //test
        gs.dealCards(g);
        assertEquals(3, gp1.getHand().size());
        assertEquals(0, g.baraja().size());
    }

    @Test
    public void testChangeTurn1(){
        //setup
        gamePlayers.add(gp1);
        gamePlayers.add(gp2);
        g.setTurn(0);
        when(gr.save(g)).thenReturn(g);
        //test
        gs.changeTurn(g);
        assertEquals(1, g.getTurn());

    }

    private void changeTurnSetup(Game g, Card c){
        c.setGamePlayer(null);
        c.setBody(false);
        c.setPlayed(false);
        g.getCards().add(c);

    }

    @Test
    public void testChangeTurn2(){
        //setup
        gamePlayers.add(gp1);
        gamePlayers.add(gp2);
        g.setTurn(1);
        when(gr.save(g)).thenReturn(g);
        changeTurnSetup(g, organ_bone);
        changeTurnSetup(g, organ_bone2);
        changeTurnSetup(g, organ_bone3);
        changeTurnSetup(g, organ_brain);
        changeTurnSetup(g, organ_brain2);
        when(cr.save(any())).thenReturn(organ_heart1);
        //test
        g.setTurn(1);
        g.setRound(0);
        gs.changeTurn(g);
        assertEquals(0, g.getTurn());
        assertEquals(1, g.getRound());
        assertEquals(3, gp1.getHand().size());
        assertEquals(3, gp2.getHand().size());
    }

    @Test
    public void testDiscard(){
        //setup
        cards.clear();
        cards.add(gp1.getCards().get(0));
        when(cr.save(any())).thenReturn(vax_heart);
        gs.discard(cards, gp1);
        assertEquals(0, gp1.getHand().size());
    }

    // @Test 
    // public void testFinishGame(){
    //     //setup
    //     gp1.getCards().clear();
    //     classificateCommonMethod(organ_brain,gp1);
    //     organ_heart1.getVirus().clear();
    //     classificateCommonMethod(organ_heart1,gp1);
    //     classificateCommonMethod(organ_rainbow,gp1);
    //     classificateCommonMethod(organ_stomach,gp1);
    //     gamePlayers.add(gp1);
    //     g.setGamePlayer(gamePlayers);
    //     when(cr.save(any())).thenReturn(vax_heart);
    //     when(gr.save(g)).thenReturn(g);
    //     //test
    //     gs.finishGame(g);
    //     assertEquals(false, g.getIsRunning());

    // }


}