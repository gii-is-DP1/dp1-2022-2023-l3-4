package org.springframework.samples.petclinic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.game.Game;
import org.springframework.samples.petclinic.card.CardService;
import org.springframework.samples.petclinic.card.GenericCard;
import org.springframework.samples.petclinic.card.GenericCard.Colour;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;


//     @WebMvcTest(controllers = GameController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, 
// classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GameServiceTest {

    @Autowired
    GameService gs;

    @Autowired
    CardService cs;

    //Elementos comunes a addOrgan
    GenericCard generic_heart =new GenericCard(1,Colour.RED, Type.ORGAN);
    GenericCard generic_stomach =new GenericCard(2,Colour.GREEN, Type.ORGAN);
    GamePlayer gp1 = new GamePlayer(0);
    GamePlayer gp2 = new GamePlayer(1);
    Card organ_heart1 = new Card(0, false, gp1, generic_heart);
    Card organ_heart2 = new Card(1, false, gp2, generic_heart);
    Card organ_stomach = new Card(1, true, gp2, generic_stomach);
    
    //Elementos comunes a playTransplant
    GenericCard generic_brain =new GenericCard(3,Colour.BLUE, Type.ORGAN);
    GenericCard generic_BVaccine =new GenericCard(2,Colour.BLUE, Type.VACCINE);
    GenericCard generic_BVaccine2 =new GenericCard(3,Colour.BLUE, Type.VACCINE);
    Card organ_brain = new Card(1, true, gp2, generic_brain);
    Card vax_brain = new Card(2, false, gp2, generic_BVaccine);
    Card vax_brain2 = new Card(3, false, gp2, generic_BVaccine2);

    //Elementos comunes a playInfection
    GenericCard generic_HVirus = new GenericCard(3,Colour.RED, Type.VIRUS);
    GenericCard generic_BVirus = new GenericCard(4,Colour.BLUE, Type.VIRUS);
    GenericCard generic_brain2 =new GenericCard(5,Colour.BLUE, Type.ORGAN);
    GenericCard generic_HVaccine =new GenericCard(6,Colour.RED, Type.VACCINE);
    GenericCard generic_HVaccine2 =new GenericCard(7,Colour.RED, Type.VACCINE);
    GenericCard generic_HVirus2 = new GenericCard(8,Colour.RED, Type.VIRUS);
    Card virus_heart = new Card(2, false, gp1, generic_HVirus);
    Card virus_heart2 = new Card(7, false, gp2, generic_HVirus2);
    Card virus_brain = new Card(3, false, gp1, generic_BVirus);
    Card organ_brain2 = new Card(4, true, gp1, generic_brain2);
    Card vax_heart = new Card(5, false, gp2, generic_HVaccine);
    Card vax_heart2 = new Card(6, false, gp2, generic_HVaccine2);

    Game g = new Game();
    

    List<Card> cards = new ArrayList<>();
    ModelMap m = new ModelMap();

    @BeforeEach
    void setup(){
        g.setCards(new ArrayList<>());
        g.setClassification(new HashMap<>());
        g.setIsRunning(true);
        g.setId(0);
        List<GamePlayer> gplayers = new ArrayList<>();
        gplayers.add(gp1);
        gplayers.add(gp2);
        g.setGamePlayer(gplayers);
        g.setInitialHour(LocalDateTime.now());
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
    //Jugar un corazón con un estómago ya en el cuerpo de otra persona que también tiene un corazón en la mano
    public void testAddOrganPositive3() {
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
        assertThrows(IllegalArgumentException.class , ()-> gs.changeCards(gp1,gp2,organ_heart1,organ_heart2));
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
        cs.changeGamePlayer(organ_brain, gp2, gp1);
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
        cs.changeGamePlayer(organ_brain, gp2, gp1);
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
        assertThrows(IllegalArgumentException.class , ()-> cs.changeGamePlayer(organ_heart2, gp2, gp1));
    }

    @Test
    //Jugar una carta de robo sobre un cerebro inmunizado.
    public void testThiefNegative2() {
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
        assertThrows(IllegalArgumentException.class , ()-> cs.changeGamePlayer(organ_brain, gp2, gp1));
    }

    @Test
    //Jugar una carta de contagio con un virus de corazón.
    public void testInfectionPositive1() {
        //Setup
        organ_heart1.setBody(true);
        List<Card> virus = new ArrayList<>();
        virus.add(virus_heart);
        organ_heart1.setVirus(virus);
        cards.add(virus_heart);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_heart2.setBody(true);
        organ_heart2.setGamePlayer(gp2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_heart2);
        gp2.setCards(cards2);
        //test
        gs.infection(gp1, gp2);
        assertEquals(true, gp2.getBody().get(0).getVirus().size()==1);
    }

    @Test
    //Jugar una carta de contagio con un virus de corazón y uno de cerebro.
    public void testInfectionPositive2() {
        //Setup
        organ_heart1.setBody(true);
        List<Card> virus = new ArrayList<>();
        List<Card> virus2 = new ArrayList<>();
        virus.add(virus_heart);
        virus2.add(virus_brain);
        organ_heart1.setVirus(virus);
        organ_brain2.setVirus(virus2);
        cards.add(organ_brain2);
        cards.add(virus_heart);
        cards.add(virus_brain);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_brain2.setGamePlayer(gp1);
        organ_heart2.setBody(true);
        organ_brain.setBody(true);
        organ_heart2.setGamePlayer(gp2);
        organ_brain.setGamePlayer(gp2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_heart2);
        cards2.add(organ_brain);
        gp2.setCards(cards2);
        //test
        gs.infection(gp1, gp2);
        assertEquals(true, gp2.getBody().get(0).getVirus().size()==1 && gp2.getBody().get(1).getVirus().size()==1);
    }

    @Test
    //Jugar una carta de contagio con un virus de corazón.
    public void testInfectionNegative1() {
        //Setup
        organ_heart1.setBody(true);
        List<Card> virus = new ArrayList<>();
        virus.add(virus_heart);
        organ_heart1.setVirus(virus);
        cards.add(virus_heart);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_heart2.setBody(true);
        organ_heart2.setGamePlayer(gp2);
        List<Card> vax = new ArrayList<>();
        vax.add(vax_heart);
        vax.add(vax_heart2);
        organ_heart2.setVaccines(vax);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_heart2);
        cards2.add(vax_heart);
        cards2.add(vax_heart2);
        gp2.setCards(cards2);
        //test
        assertThrows(IllegalArgumentException.class , ()-> gs.infection(gp1, gp2));
    }

    @Test
    //Jugar una carta de contagio con un virus de corazón.
    public void testInfectionNegative2() {
        //Setup
        organ_heart1.setBody(true);
        List<Card> virus = new ArrayList<>();
        virus.add(virus_heart);
        organ_heart1.setVirus(virus);
        cards.add(virus_heart);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_heart2.setBody(true);
        organ_heart2.setGamePlayer(gp2);
        List<Card> vax = new ArrayList<>();
        vax.add(vax_heart);
        organ_heart2.setVaccines(vax);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_heart2);
        cards2.add(vax_heart);
        gp2.setCards(cards2);
        //test
        assertThrows(IllegalArgumentException.class , ()-> gs.infection(gp1, gp2));
    }

    @Test
    //Jugar una carta de contagio con un virus de corazón.
    public void testInfectionNegative3() {
        //Setup
        organ_heart1.setBody(true);
        List<Card> virus = new ArrayList<>();
        virus.add(virus_heart);
        organ_heart1.setVirus(virus);
        cards.add(virus_heart);
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_heart2.setBody(true);
        organ_heart2.setGamePlayer(gp2);
        List<Card> virus2 = new ArrayList<>();
        virus2.add(virus_heart2);
        organ_heart2.setVirus(virus2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_heart2);
        cards2.add(virus_heart2);
        gp2.setCards(cards2);
        //test
        assertThrows(IllegalArgumentException.class , ()-> gs.infection(gp1, gp2));
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

}