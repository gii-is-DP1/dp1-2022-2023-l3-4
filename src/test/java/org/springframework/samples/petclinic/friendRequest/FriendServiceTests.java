package org.springframework.samples.petclinic.friendRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.stereotype.Service;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class FriendServiceTests {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    protected FriendService friendService;
    Friend friend20 = new Friend();


    @Test
    @Transactional
    public void testFindFriendsById() {
        Collection<Player> friends = friendService.findFriendsById(1);
        assertTrue(friends.size() == 2, "The friend with id 1 should have 2 friends");
    }

    @Test
    @Transactional
    public void testFindMyRecRequestById() {
        Collection<Friend> requests = friendService.findMyRecRequestById(1);
        assertTrue(requests.size() == 1, "The frind with id 1 should have 1 friend request");

    }

    @Test
    @Transactional
    public void testFindFriendById() {
        Friend friend = friendService.findFriendById(1);
        assertTrue(friend.getId() == 1, "There should be a friend with an id of 1.");
    }

    @Test
    @Transactional
    public void testFindByPlayersId() {
        Friend friend = friendService.findByPlayersId(3, 1);
        assertNotNull(friend, "El reposisotorio ha devuelto una lista nula");
        assertEquals(3, friend.getId(), "Los juagodores indicados no son amigos");

    }
    @Test
    @Transactional
    public void testSavePlayer() { 


        Player player1 = playerService.findPlayerById(3);
        Player player2 = playerService.findPlayerById(4);

        friend20.setStatus(null);
        friend20.setPlayerRec(player1);
        friend20.setPlayerSend(player2);
        friendService.savePlayer(friend20);
        assertNotNull(friendService.findFriendById(5), "There should be a friend with an id of 20.");
   }

   @Test
   @Transactional
   public void testDeleteFriendById() {

        friendService.deleteFriendById(4);
        assertFalse(friendRepository.findById(4).isPresent(), "friend20 should have been deleted");
   }

}