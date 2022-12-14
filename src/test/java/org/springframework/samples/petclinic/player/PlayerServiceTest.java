package org.springframework.samples.petclinic.player;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlayerServiceTest {
    @Autowired
    protected PlayerService playerService;


    @Test
    public void shouldFindPlayerByUsername() {
        Player player = this.playerService.getPlayerByUsername("frabenrui1");
        assertNotNull(player);
    }

    @Test
    public void shouldFindPlayersByUsername() {
        Collection<Player> player = this.playerService.getPlayersByUsername("");
        Collection<Player> allPlayer = this.playerService.getAllPlayers();
        assertTrue(player.size()==allPlayer.size());
    }
}

