package org.springframework.samples.petclinic.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.Card;
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

    //Elementos comunes a addOrgan
    GenericCard generic_heart =new GenericCard(1,Colour.RED, Type.ORGAN);
    GenericCard generic_stomach =new GenericCard(2,Colour.GREEN, Type.ORGAN);
    GamePlayer gp1 = new GamePlayer(0);
    GamePlayer gp2 = new GamePlayer(1);
    Card organ_heart1 = new Card(0, false, gp1, generic_heart);
    Card organ_heart2 = new Card(1, false, gp2, generic_heart);
    Card organ_stomach = new Card(1, true, gp2, generic_stomach);
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

 

}