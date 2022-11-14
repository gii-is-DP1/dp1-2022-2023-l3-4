package org.springframework.samples.petclinic.gamePlayer;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataJpaTest
public class GamePlayerRepositoryTest {

    @Autowired
    GamePlayerRepository gr;
    GamePlayer g75 = new GamePlayer();

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
        List<GamePlayer> gps = gr.findAll();
        assertNotNull(gps, "There should be GamePlayers in the database.");
    }

    public void testFindById(){
        Optional<GamePlayer> gp = this.gr.findById(1);
        assertTrue(gp.isPresent(), "There should be a GamePlayer with an id of 1.");
        assertTrue(gp.get().getId() == 1);
    }

    public void testSave(){
        g75.setId(75);
        gr.save(g75);
        assertNotNull(gr.findById(75), "There should be a card with an id of 75.");
    }
    
}
