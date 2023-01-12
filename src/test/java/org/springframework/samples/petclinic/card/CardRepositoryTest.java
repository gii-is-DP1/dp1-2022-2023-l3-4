package org.springframework.samples.petclinic.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setup(){
      c75.setId(75);
      c75.setBody(true);
      c75.setPlayed(false);
      c.setId(77);
      c.setColour(Colour.BLUE);
      c.setType(Type.ORGAN);
      c75.setType(c);
}

    @Test
    @Transactional
    public void testSave(){
        cr.save(c75);
        assertNotNull(cr.findById(75), "There should be a card with an id of 75.");
    }

    @Test
    @Transactional
    public void testFindAll(){
        cr.save(c75);
        List<Card> cards = cr.findAll();
        assertNotNull(cards, "There should be cards in the database.");
        assertEquals(1, cards.size());
    
    }

    @Test
    @Transactional
    public void testFindById(){
        cr.save(c75);
        Optional<Card> card = this.cr.findById(75);
        assertNotNull(card, "There should be a card with an id of 1.");
    }

}
