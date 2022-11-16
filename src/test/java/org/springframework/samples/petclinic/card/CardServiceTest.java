package org.springframework.samples.petclinic.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

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


}
