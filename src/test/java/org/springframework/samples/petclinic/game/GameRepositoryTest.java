package org.springframework.samples.petclinic.game;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class GameRepositoryTest {
    @Autowired
    GameRepository gr;
    Game g75 = new Game();

    @Test
    @Transactional
    public void test1(){
        testFindAll();
    }

    @Test
    @Transactional
    public void test2(){
        testFindById();
    }

    @Test
    @Transactional
    public void test3(){
        testSave();
    }
    
    public void testFindAll(){
        List<Game> gs = gr.findAll();
        assertNotNull(gs.size()==1, "There should be cards in the database.");
    }

    public void testFindById(){
        Optional<Game> g = gr.findById(1);
        assertTrue(g.get().getId() == 1, "There should be a Gameplayer with an id of 1.");
    }

    public void testSave(){
        g75.setId(75);
        gr.save(g75);
        assertNotNull(gr.findById(75), "There should be a card with an id of 75.");
    }
}
