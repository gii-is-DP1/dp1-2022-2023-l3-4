package org.springframework.samples.petclinic.game;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.virus.game.Game;
import org.springframework.samples.virus.game.GameRepository;

@DataJpaTest
public class GameRepositoryTest {
    @Autowired
    GameRepository gr;
    Game g75 = new Game();

    @Test
    @Transactional
    public void testFindAll(){
        List<Game> gs = gr.findAll();
        assertNotNull(gs.size()==1, "There should be cards in the database.");
    }

    @Test
    @Transactional
    public void testFindById(){
        Optional<Game> g = gr.findById(1);
        assertTrue(g.get().getId() == 1, "There should be a Gameplayer with an id of 1.");
    }

    @Test
    @Transactional
    public void testSave(){
        g75.setId(75);
        gr.save(g75);
        assertNotNull(gr.findById(75), "There should be a card with an id of 75.");
    }
}
