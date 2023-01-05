package org.springframework.samples.petclinic.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
       GenericCard generic_brain =new GenericCard(1,Colour.BLUE, Type.ORGAN);
       GenericCard generic_virus_bone =new GenericCard(2,Colour.YELLOW, Type.VIRUS);
       GenericCard generic_vaccine_bone =new GenericCard(3,Colour.YELLOW, Type.VACCINE);
       GamePlayer gp1 = new GamePlayer(0);
       GamePlayer gp2 = new GamePlayer(1);
       Card organ_bone = new Card(0, true, gp2, generic_bone);
       Card organ_brain = new Card(0, true, gp2, generic_brain);
       Card virus_bone = new Card(1, false, gp1, generic_virus_bone);
       Card virus_bone2 = new Card(2, false, gp2, generic_virus_bone);
       Card vaccine_bone = new Card(3, false, gp2, generic_vaccine_bone);  
       Card vaccine_bone2 = new Card(4, false, gp2, generic_vaccine_bone); 
       Card vaccine_bone3 = new Card(5, false, gp2, generic_vaccine_bone);    
       List<Card> cards = new ArrayList<>();
       //Card organ_stomach = new Card(1, true, gp2, generic_stomach);
       ModelMap m = new ModelMap();
   
       @BeforeEach
       void setup2(){
        virus_bone.setGamePlayer(gp1);
           cards.add(virus_bone);
           gp1.setCards(cards);
           cards = new ArrayList<>();
       }
   
       @Test
       //Jugar un virus de hueso sobre un hueso sano
        public void testPlayVirusPositive1(){
           //setup
           organ_bone.setGamePlayer(gp2);
           cards.add(organ_bone);
           gp2.setCards(cards);
           //test
           cs.infect(organ_bone, virus_bone);
           assertEquals(gp2.getBody().size(), 1, "There should be 1 bone in the body.");
           assertEquals(gp2.getBody().get(0).getVirus().size(), 1, "There should be 1 virus in the bone.");
       }

       @Test
       //Jugar un virus de hueso sobre un hueso ya infectado
        public void testPlayVirusPositive2(){
           //setup
           virus_bone2.setCardVirus(organ_bone);
           virus_bone2.setGamePlayer(gp2);
           cards.add(virus_bone2);
           organ_bone.setVirus(cards);
           organ_bone.setGamePlayer(gp2);
           cards.add(organ_bone);
           gp2.setCards(cards);
           //test
           cs.infect(organ_bone, virus_bone);
           assertEquals(gp2.getBody().size(), 0, "There should be 0 organs in the body.");
       }

       @Test
       //Jugar un virus de hueso sobre un hueso ya vacunado
        public void testPlayVirusPositive3(){
           //setup
           vaccine_bone.setCardVaccine(organ_bone);
           vaccine_bone.setGamePlayer(gp2);
           cards.add(vaccine_bone);
           organ_bone.setVaccines(cards);
           List<Card> cards2 = new ArrayList<>();
           cards2.add(vaccine_bone);
           organ_bone.setGamePlayer(gp2);
           cards2.add(organ_bone);
           gp2.setCards(cards2);
           //test
           cs.infect(organ_bone, virus_bone);
           assertEquals(gp2.getBody().size(), 1, "There should be 1 organ in the body.");
           assertEquals(gp2.getBody().get(0).getVaccines().size(), 0, "The organ must be clean");
           assertEquals(gp2.getBody().get(0).getVirus().size(), 0, "the organ must be clean");
        }

        @Test
        //Jugar un virus de hueso sobre un cerebro
        public void testPlayVirusNegative1(){
            //setup
           organ_brain.setGamePlayer(gp2);
           cards.add(organ_brain);
           gp2.setCards(cards);
           //test
           assertThrows(IllegalArgumentException.class , ()-> cs.infect(organ_brain,virus_bone));
         }

         @Test
         //Jugar un virus de hueso sobre un hueso inmunizado
         public void testPlayVirusNegative2(){
             //setup
             vaccine_bone.setCardVaccine(organ_bone);
             vaccine_bone.setGamePlayer(gp2);
             cards.add(vaccine_bone);
             vaccine_bone2.setCardVaccine(organ_bone);
             vaccine_bone2.setGamePlayer(gp2);
             cards.add(vaccine_bone2);
             organ_bone.setVaccines(cards);
             List<Card> cards2 = new ArrayList<>();
             cards2.add(vaccine_bone);
             cards2.add(vaccine_bone2);
             organ_bone.setGamePlayer(gp2);
             cards2.add(organ_bone);
             gp2.setCards(cards2);
            //test
            assertThrows(IllegalArgumentException.class , ()-> cs.infect(organ_brain,virus_bone));
          }

          @BeforeEach
          void setup3(){
           vaccine_bone.setGamePlayer(gp1);
              cards.add(vaccine_bone);
              gp1.setCards(cards);
              cards = new ArrayList<>();
          }

          @Test
       //Jugar una vacuna de hueso sobre un hueso sano
        public void testPlayVaccinePositive1(){
           //setup
           organ_bone.setGamePlayer(gp2);
           cards.add(organ_bone);
           gp2.setCards(cards);
           //test
           cs.vaccinate(organ_bone, vaccine_bone);
           assertEquals(gp2.getBody().size(), 1, "There should be 1 bone in the body.");
           assertEquals(gp2.getBody().get(0).getVaccines().size(), 1, "There should be 1 vaccine in the bone.");
        }

       @Test
       //Jugar una vacuna de hueso sobre un hueso ya infectado
        public void testPlayVaccinePositive2(){
           //setup
           virus_bone2.setCardVirus(organ_bone);
           virus_bone2.setGamePlayer(gp2);
           cards.add(virus_bone2);
           organ_bone.setVirus(cards);
           organ_bone.setGamePlayer(gp2);
           cards.add(organ_bone);
           gp2.setCards(cards);
           //test
           cs.vaccinate(organ_bone, vaccine_bone);
           assertEquals(gp2.getBody().size(), 1, "There should be 0 organs in the body.");
       }

       @Test
       //Jugar una vacuna de hueso sobre un hueso ya vacunado
        public void testPlayVaccinePositive3(){
           //setup
           vaccine_bone2.setCardVaccine(organ_bone);
           vaccine_bone2.setGamePlayer(gp2);
           cards.add(vaccine_bone2);
           organ_bone.setVaccines(cards);
           List<Card> cards2 = new ArrayList<>();
           cards2.add(vaccine_bone2);
           organ_bone.setGamePlayer(gp2);
           cards2.add(organ_bone);
           gp2.setCards(cards2);
           //test
           cs.vaccinate(organ_bone, vaccine_bone);
           assertEquals(gp2.getBody().size(), 1, "There should be 1 organ in the body.");
           assertEquals(gp2.getBody().get(0).getVaccines().size(), 2, "The organ must be inmunized");
        }

        @Test
        //Jugar una vacuna de hueso sobre un cerebro
        public void testPlayVaccineNegative1(){
            //setup
           organ_brain.setGamePlayer(gp2);
           cards.add(organ_brain);
           gp2.setCards(cards);
           //test
           assertThrows(IllegalArgumentException.class , ()-> cs.vaccinate(organ_brain,vaccine_bone));
         }

         @Test
         //Jugar un virus de hueso sobre un hueso inmunizado
         public void testPlayVaccineNegative2(){
             //setup
             vaccine_bone2.setCardVaccine(organ_bone);
             vaccine_bone2.setGamePlayer(gp2);
             cards.add(vaccine_bone2);
             vaccine_bone3.setCardVaccine(organ_bone);
             vaccine_bone3.setGamePlayer(gp2);
             cards.add(vaccine_bone3);
             organ_bone.setVaccines(cards);
             List<Card> cards2 = new ArrayList<>();
             cards2.add(vaccine_bone2);
             cards2.add(vaccine_bone3);
             organ_bone.setGamePlayer(gp2);
             cards2.add(organ_bone);
             gp2.setCards(cards2);
            //test
            assertThrows(IllegalArgumentException.class , ()-> cs.vaccinate(organ_brain,vaccine_bone));
          }

}
