package org.springframework.samples.petclinic.gamePlayer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.virus.gamePlayer.GamePlayer;
import org.springframework.samples.virus.gamePlayer.GamePlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class GamePlayerServiceTest {
    @Autowired
    GamePlayerService gs;
    GamePlayer g75 = new GamePlayer();
    
    @Test
    @Transactional
    public void testFindAll(){
        List<GamePlayer> gps = gs.findAll();
        assertNotNull(gps.size()==1, "There should be cards in the database.");
    }

    @Test
    @Transactional
    public void testFindById(){
        Optional<GamePlayer> gps = gs.findById(1);
        assertTrue(gps.get().getId() == 1, "There should be a Gameplayer with an id of 1.");
    }

    @Test
    @Transactional
    public void testSave(){
        g75.setId(75);
        gs.save(g75);
        assertNotNull(gs.findById(75), "There should be a card with an id of 75.");
    }

}
