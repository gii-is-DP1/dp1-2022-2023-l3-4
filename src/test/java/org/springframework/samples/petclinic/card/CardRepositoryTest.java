package org.springframework.samples.petclinic.card;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.virus.card.Card;
import org.springframework.samples.virus.card.CardRepository;
import org.springframework.samples.virus.card.GenericCard;
import org.springframework.samples.virus.card.GenericCard.Colour;
import org.springframework.samples.virus.card.GenericCard.Type;

@DataJpaTest
public class CardRepositoryTest {
    
    @Autowired
    CardRepository cr;
    Card c75 = new Card();
    GenericCard c = new GenericCard();

    @Test
    @Transactional
    public void testFindAll(){
        List<Card> cards = cr.findAll();
        assertNotNull(cards, "There should be cards in the database.");
    }

    @Test
    @Transactional
    public void testFindById(){
        Optional<Card> card = this.cr.findById(1);
        assertNotNull(card, "There should be a card with an id of 1.");
        assertTrue(card.get().getId() == 1);
    }

    @Test
    @Transactional
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
