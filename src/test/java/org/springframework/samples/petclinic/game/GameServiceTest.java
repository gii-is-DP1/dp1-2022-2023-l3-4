package org.springframework.samples.petclinic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.Card;
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

    List<Card> cards = new ArrayList<>();
    ModelMap m = new ModelMap();

    @BeforeEach
    void setup(){
        cards.add(organ_heart1);
    }


    @Test
    //Jugar un corazón con el cuerpo vacío
    public void testaddOrganPositive1() {
        //setup
        gp1.setCards(cards);
        //test
        gs.addOrgan(organ_heart1, gp1, gp1, m);
        assertEquals(1, gp1.getBody().size());
        assertEquals(0, gp1.getHand().size());
    }
    @Test
    //Jugar un corazón con un estómago ya en el cuerpo
    public void testaddOrganPositive2() {
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
    public void testaddOrganPositive3() {
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
    public void testaddOrganNegative1() {
        //Setup
        organ_heart2.setBody(true);
        cards.add(organ_heart2);
        gp1.setCards(cards);
        //test
        assertThrows(IllegalArgumentException.class , ()-> gs.addOrgan(organ_heart1, gp1, gp1, m));
}
    @Test
    //Jugar un corazón a otro que ya tiene un corazón en el cuerpo
    public void testaddOrganNegative2() {
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
        gp1.setCards(cards);
        organ_heart1.setGamePlayer(gp1);
        organ_heart2.setBody(true);
        organ_heart2.setGamePlayer(gp2);
        List<Card> cards2 = new ArrayList<>();
        cards2.add(organ_heart2);
        gp2.setCards(cards2);
        //test 
        assertThrows(IllegalArgumentException.class , ()-> gs.changeCards(gp1,gp2,organ_heart1,organ_heart2));
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

}