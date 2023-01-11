package org.springframework.samples.petclinic.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserRepository;

@DataJpaTest
public class PlayerRepositoryTest {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldFindPlayersByUsername() {
        User u1 = new User();
        u1.setUsername("player1");
        u1.setPassword("pwd");
        userRepository.save(u1);

        User u2 = new User();
        u2.setUsername("player2");
        u2.setPassword("pwd");
        userRepository.save(u2);
        
        Player p1 = new Player();
        p1.setFirstName("Player");
        p1.setLastName("1");
        p1.setUser(u1);
        playerRepository.save(p1);

        Player p2 = new Player();
        p2.setFirstName("Player");
        p2.setLastName("2");
        p2.setUser(u2);
        playerRepository.save(p2);


        List<Player> players = (List<Player>) playerRepository.findPlayersByUsername("player");
        assertEquals(2, players.size(), "It should find 2 players.");
    }

    @Test
    public void shouldFindPlayerByUsername() {
        User u1 = new User();
        u1.setUsername("player1");
        u1.setPassword("pwd");
        userRepository.save(u1);

        Player p1 = new Player();
        p1.setFirstName("Player");
        p1.setLastName("1");
        p1.setUser(u1);
        playerRepository.save(p1);
        
        Player player = playerRepository.findPlayerByUsername("player1");
        assertEquals("player1", player.getUser().getUsername(), "It should find the player player1.");
    }
}
