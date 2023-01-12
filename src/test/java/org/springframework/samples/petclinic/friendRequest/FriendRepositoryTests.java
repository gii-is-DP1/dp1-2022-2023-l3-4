package org.springframework.samples.petclinic.friendRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.player.Player;

@DataJpaTest
public class FriendRepositoryTests {
    @Autowired
    FriendRepository friendRepository;

    @Test
    public void testFindFriendBySendId() {
        Collection<Player> players = friendRepository.findFriendBySendId(1);
        assertNotNull(players, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, players.size(), "Faltan datos de inicialización");
    }

    @Test
    public void testFindFriendByRecId() {
        Collection<Player> players = friendRepository.findFriendByRecId(3);
        assertNotNull(players, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, players.size(), "Faltan datos de inicialización");
    }

    @Test
    public void testFindMySendRequestById() {
        Collection<Friend> friends = friendRepository.findMySendRequestById(4);
        assertNotNull(friends, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, friends.size(), "Faltan datos de inicialización");
    }

    @Test
    public void testFindMyRecRequestById() {
        Collection<Friend> friends = friendRepository.findMyRecRequestById(1);
        assertNotNull(friends, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, friends.size(), "Faltan datos de inicialización");
    }

    @Test
    public void testFindByPlayersId() {
        Friend friend = friendRepository.findByPlayersId(1, 5);
        assertNotNull(friend, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, friend.getId(), "Los juagodores indicados no son amigos");
    }
}

