package org.springframework.samples.petclinic.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.GenericCard.Colour;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CardServiceTest {
    @Autowired
    CardService cs;
    
    @Test
    @Transactional
    public void testFindCards(){
        List<Card> cards = cs.findCards();
        assertNotNull(cards.size()==1, "There should be cards in the database.");
    }

    @Test
    @Transactional
    public void testFindPlayed(){
        List<Card> cards = cs.findPlayed();
        assertEquals(cards.size(), 0, "There should be x played cards in the database.");
    }


       //Elementos comunes a playVirus
       GenericCard generic_bone =new GenericCard(1,Colour.YELLOW, Type.ORGAN);
       GenericCard generic_virus_bone =new GenericCard(2,Colour.YELLOW, Type.VIRUS);
       GamePlayer gp1 = new GamePlayer(0);
       GamePlayer gp2 = new GamePlayer(1);
       Card organ_bone = new Card(0, true, gp1, generic_bone);
       Card virus_bone = new Card(1, false, gp2, generic_virus_bone);
       List<Card> cards = new ArrayList<>();
       //Card organ_stomach = new Card(1, true, gp2, generic_stomach);
       ModelMap m = new ModelMap();
   
       @BeforeEach
       void setup2(){
        virus_bone.setGamePlayer(gp1);
           cards.add(virus_bone);
           gp1.setCards(cards);
           cards.clear();
       }
   
       @Test
       //Jugar un virus de hueso sobre un hueso sano
        public void testPlayVirusPositive(){
        organ_bone.setGamePlayer(gp1);
           cards.add(organ_bone);
           gp2.setCards(cards);
           cs.infect(organ_bone, virus_bone);
           assertEquals(gp2.getBody().size(), 1, "There should be 1 virus in the bone.");
       }


}
