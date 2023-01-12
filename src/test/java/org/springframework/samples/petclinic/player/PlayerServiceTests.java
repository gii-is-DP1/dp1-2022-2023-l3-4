package org.springframework.samples.petclinic.player;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PlayerServiceTests {
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

    @Test
    public void shouldFindPlayerById() {
        Player p = playerService.findPlayerById(1);
        assertEquals("frabenrui1", p.getUser().getUsername(), "The Player frabenrui1 was not found.");
    }

    @Test
    public void shouldDeletePlayer() {
        Player p = playerService.findPlayerById(1);
        playerService.deletePlayer(p);
        Player pDeleted = playerService.findPlayerById(1);

        assertNull(pDeleted, "The player was not deleted.");
    }
}

