package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.card.Card;
import org.springframework.samples.petclinic.card.CardRepository;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CardTests {
    @Autowired
    CardRepository cr;
    
    @Test
    public void test3(){
        testInitialCards();
        
    }
    
    public void testInitialCards(){
        List<Card> cards=cr.findAll();
        assertTrue(cards.size()==1, "Exactly two products should be present in the DB");

        Optional<Card> c1=cr.findById(1);
        assertTrue(c1.isPresent(),"There should exist a product with id:1");
        assertEquals(c1.get().getVaccines().size(), 0);
       
        
    }
}
