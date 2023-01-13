package org.springframework.samples.petclinic.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.GenericCard.Colour;
import org.springframework.samples.petclinic.card.GenericCard.Type;
import org.springframework.samples.petclinic.gamePlayer.GamePlayer;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CardServiceTests {
    @Autowired
    CardService cs;

    @MockBean
    private CardRepository cr;

    @Test
    @Transactional
    public void testFindPlayed(){
        List<Card> cards = cs.findPlayed();
        assertEquals(cards.size(), 0, "There should be x played cards in the database.");
    }

       //Elementos comunes a playVirus
       GenericCard generic_bone =new GenericCard(0,Colour.YELLOW, Type.ORGAN);
       GenericCard generic_brain =new GenericCard(1,Colour.BLUE, Type.ORGAN);
       GenericCard generic_rainbow =new GenericCard(2,Colour.RAINBOW, Type.ORGAN);
       GenericCard generic_virus_bone =new GenericCard(2,Colour.YELLOW, Type.VIRUS);
       GenericCard generic_virus_rainbow =new GenericCard(3,Colour.RAINBOW, Type.VIRUS);
       GenericCard generic_vaccine_bone =new GenericCard(4,Colour.YELLOW, Type.VACCINE);
       GenericCard generic_vaccine_rainbow =new GenericCard(4,Colour.RAINBOW, Type.VACCINE);

       GamePlayer gp1 = new GamePlayer(0);
       GamePlayer gp2 = new GamePlayer(1);
       GamePlayer gp3 = new GamePlayer(2);
       GamePlayer gp4 = new GamePlayer(3);

       Card organ_bone = new Card(0, true, gp2, generic_bone);
       Card organ_brain = new Card(1, true, gp2, generic_brain);
       Card organ_rainbow = new Card(2, true, gp2, generic_rainbow);

       Card virus_bone = new Card(3, false, gp1, generic_virus_bone);
       Card virus_bone2 = new Card(4, false, gp2, generic_virus_bone);
       Card virus_rainbow = new Card(5, false, gp1, generic_virus_rainbow); 

       Card vaccine_bone = new Card(6, false, gp2, generic_vaccine_bone);  
       Card vaccine_bone2 = new Card(7, false, gp2, generic_vaccine_bone); 
       Card vaccine_bone3 = new Card(8, false, gp2, generic_vaccine_bone);
       Card vaccine_rainbow = new Card(9, false, gp2, generic_vaccine_rainbow); 
       List<Card> cards = new ArrayList<>();
       //Card organ_stomach = new Card(1, true, gp2, generic_stomach);
       ModelMap m = new ModelMap();
   
       @BeforeEach
       void setup2(){
        virus_bone.setGamePlayer(gp1);
           cards.add(virus_bone);
           cards.add(virus_rainbow);
           virus_bone.setGamePlayer(gp1);
           virus_rainbow.setGamePlayer(gp1);
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
           assertEquals(gp1.getHand().size(), 1,"There should be 1 card in the body.");
       }

       @Test
       //Jugar un virus de hueso sobre un órgano arcoíris sano
        public void testPlayVirusPositive2(){
           //setup
           organ_rainbow.setGamePlayer(gp2);
           cards.add(organ_rainbow);
           gp2.setCards(cards);
           //test
           cs.infect(organ_rainbow, virus_bone);
           assertEquals(gp2.getBody().size(), 1, "There should be 1 bone in the body.");
           assertEquals(gp2.getBody().get(0).getVirus().size(), 1, "There should be 1 virus in the bone.");
       }

       @Test
       //Jugar un virus arcoíris sobre un hueso sano
        public void testPlayVirusPositive3(){
           //setup
           organ_bone.setGamePlayer(gp2);
           cards.add(organ_bone);
           gp2.setCards(cards);
           //test
           cs.infect(organ_bone, virus_rainbow);
           assertEquals(gp2.getBody().size(), 1, "There should be 1 bone in the body.");
           assertEquals(gp2.getBody().get(0).getVirus().size(), 1, "There should be 1 vaccine in the bone.");
           assertEquals(gp1.getHand().size(), 1, "There should be 1 card in the body.");
        }

       @Test
       //Jugar un virus de hueso sobre un hueso ya infectado
        public void testPlayVirusPositive4(){
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
        public void testPlayVirusPositive5(){
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
            assertThrows(IllegalArgumentException.class , ()-> cs.infect(organ_bone,virus_bone));
          }

          @BeforeEach
          void setup3(){
           vaccine_bone.setGamePlayer(gp3);
              cards.add(vaccine_bone);
              cards.add(vaccine_rainbow);
              vaccine_bone.setGamePlayer(gp3);
              vaccine_rainbow.setGamePlayer(gp3);
              gp3.setCards(cards);
              cards = new ArrayList<>();
          }

          @Test
       //Jugar una vacuna de hueso sobre un hueso sano
        public void testPlayVaccinePositive1(){
           //setup
           organ_bone.setGamePlayer(gp4);
           cards.add(organ_bone);
           gp4.setCards(cards);
           //test
           cs.vaccinate(organ_bone, vaccine_bone);
           assertEquals(gp4.getBody().size(), 1, "There should be 1 bone in the body.");
           assertEquals(gp4.getBody().get(0).getVaccines().size(), 1, "There should be 1 vaccine in the bone.");
        }

        @Test
        //Jugar una vacuna arcoíris sobre un hueso sano
         public void testPlayVaccinePositive2(){
            //setup
            organ_bone.setGamePlayer(gp4);
            cards.add(organ_bone);
            gp4.setCards(cards);
            //test
            cs.vaccinate(organ_bone, vaccine_rainbow);
            assertEquals(gp4.getBody().size(), 1, "There should be 1 bone in the body.");
            assertEquals(gp4.getBody().get(0).getVaccines().size(), 1, "There should be 1 vaccine in the bone.");
         }

       @Test
       //Jugar una vacuna de hueso sobre un hueso ya infectado
        public void testPlayVaccinePositive3(){
           //setup
           virus_bone2.setCardVirus(organ_bone);
           virus_bone2.setGamePlayer(gp4);
           cards.add(virus_bone2);
           organ_bone.setVirus(cards);
           organ_bone.setGamePlayer(gp4);
           cards.add(organ_bone);
           gp4.setCards(cards);
           //test
           cs.vaccinate(organ_bone, vaccine_bone);
           assertEquals(gp4.getBody().size(), 1, "There should be 0 organs in the body.");
       }

       @Test
       //Jugar una vacuna de hueso sobre un hueso ya vacunado
        public void testPlayVaccinePositive4(){
           //setup
           vaccine_bone2.setCardVaccine(organ_bone);
           vaccine_bone2.setGamePlayer(gp4);
           cards.add(vaccine_bone2);
           organ_bone.setVaccines(cards);
           List<Card> cards2 = new ArrayList<>();
           cards2.add(vaccine_bone2);
           organ_bone.setGamePlayer(gp4);
           cards2.add(organ_bone);
           gp4.setCards(cards2);
           //test
           cs.vaccinate(organ_bone, vaccine_bone);
           assertEquals(gp4.getBody().size(), 1, "There should be 1 organ in the body.");
           assertEquals(gp4.getBody().get(0).getVaccines().size(), 2, "The organ must be inmunized");
        }

        @Test
        //Jugar una vacuna de hueso sobre un cerebro
        public void testPlayVaccineNegative1(){
            //setup
           organ_brain.setGamePlayer(gp4);
           cards.add(organ_brain);
           gp4.setCards(cards);
           //test
           assertThrows(IllegalArgumentException.class , ()-> cs.vaccinate(organ_brain,vaccine_bone));
         }

         @Test
         //Jugar un virus de hueso sobre un hueso inmunizado
         public void testPlayVaccineNegative2(){
             //setup
             vaccine_bone2.setCardVaccine(organ_bone);
             vaccine_bone2.setGamePlayer(gp4);
             cards.add(vaccine_bone2);
             vaccine_bone3.setCardVaccine(organ_bone);
             vaccine_bone3.setGamePlayer(gp4);
             cards.add(vaccine_bone3);
             organ_bone.setVaccines(cards);
             List<Card> cards2 = new ArrayList<>();
             cards2.add(vaccine_bone2);
             cards2.add(vaccine_bone3);
             organ_bone.setGamePlayer(gp4);
             cards2.add(organ_bone);
             gp4.setCards(cards2);
            //test
            assertThrows(IllegalArgumentException.class , ()-> cs.vaccinate(organ_bone,vaccine_bone));
          }

          //Elementos comunes para métodos restantes
          List<Card> cards2 = new ArrayList<>();
          Card c1 = new Card(10, false, null, generic_bone);

          @BeforeEach
          void setup4(){
            organ_bone.setPlayed(true);
            virus_rainbow.setPlayed(true);
            vaccine_bone.setPlayed(true);
            cards2.add(organ_bone);
            cards2.add(virus_rainbow);
            cards2.add(vaccine_bone);
          }

          @Test
          //Devuelve la lista de colores dada una lista de cartas
          public void testGetColours(){
            assertEquals(List.of("YELLOW", "RAINBOW", "YELLOW"), cs.getColours(cards2));
          }

          @Test
          //Devuelve una carta que sea arcoíris de una lista de cartas
          public void testGetARainbow(){
            assertEquals(virus_rainbow, cs.getARainBow(cards2));
          }

          @Test
          //Devuelve desordenados los elementos de la lista con played a false
          public void testShuffle(){
            when(cr.save(organ_bone)).thenReturn(organ_bone);
            when(cr.save(virus_rainbow)).thenReturn(virus_rainbow);
            when(cr.save(vaccine_bone)).thenReturn(vaccine_bone);
            cs.shuffle(cards2);
            assertEquals(List.of(false,false,false), cards2.stream().map(c->c.getPlayed()).collect(Collectors.toList()));
          }

          @Test
          //Devuelve una lista de cartas dado una lista de Ids
          @Transactional
          public void testFindAllCardsByIds(){
            Card c2 = new Card(11, false, null, generic_virus_rainbow);
            List<Card> cards = new ArrayList<>();
            cards.add(c1);
            cards.add(c2);
            List<Integer> cardsIds = new ArrayList<>();
            cardsIds.add(10);
            cardsIds.add(11);
            when(cr.findCardsByIds(cardsIds)).thenReturn(cards);
            assertEquals(cards, cs.findAllCardsByIds(cardsIds));
          }

          @Test
          //Guarda una carta
          @Transactional
          public void testSave(){
            when(cr.save(organ_bone)).thenReturn(organ_bone);
            assertEquals(organ_bone, cs.save(organ_bone));
          }

          @Test
          //Encuentra una carta
          @Transactional
          public void testFindCard(){
           Optional<Card> c1_o = Optional.of(c1);
           when(cr.findById(10)).thenReturn(c1_o);
           assertEquals(c1_o, cs.findCard(10));
          }

          @Test
          //Devuelve el cuerpo de un gamePlayer
          @Transactional
          public void testGetBodyFromAGamePlayer(){
            when(cr.getBodyFromAGamePlayer(gp3.getId())).thenReturn(new ArrayList<>());
            assertEquals(new ArrayList<>(), cs.getBodyFromAGamePlayer(gp3.getId()));
          }

          @Test
          @Transactional
          public void testFindCards(){
          organ_rainbow.setPlayed(false);
          cards2.clear();
          cards2.add(organ_bone);
          cards2.add(organ_rainbow);
          when(cr.findAll()).thenReturn(cards2);
          assertEquals(List.of(organ_bone), cs.findPlayed());
    }



}
