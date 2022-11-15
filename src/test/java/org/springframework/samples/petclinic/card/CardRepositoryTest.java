package org.springframework.samples.petclinic.card;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.card.GenericCard.Colour;
import org.springframework.samples.petclinic.card.GenericCard.Type;

@DataJpaTest
public class CardRepositoryTest {
    
    @Autowired
    CardRepository cr;
    Card c75 = new Card();
    GenericCard c = new GenericCard();

    @Test
    public void test1(){
        testFindAll();
    }

    @Test
    public void test2(){
        testFindById();
    }

    @Test
    public void test3(){
        testSave();
    }

    public void testFindAll(){
        List<Card> cards = cr.findAll();
        assertNotNull(cards, "There should be cards in the database.");
    }

    public void testFindById(){
        Optional<Card> card = this.cr.findById(1);
        assertTrue(card.isPresent(), "There should be a card with an id of 1.");
        assertTrue(card.get().getId() == 1);
    }

    public void testSave(){
        c75.setId(75);
        c75.setBody(true);
        c75.setPlayed(false);
        c.setId(77);
        c.setColour(Colour.BLUE);
        c.setType(Type.ORGAN);
        c75.setType(c);
        cr.save(c75);
        assertNotNull(cr.findById(75), "There should be a card with an id of 75.");
    }
}
