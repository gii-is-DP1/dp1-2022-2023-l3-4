package org.springframework.samples.petclinic.invitations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.stereotype.Service;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.invitation.Invitation;
import org.springframework.samples.petclinic.invitation.InvitationRepository;
import org.springframework.samples.petclinic.invitation.InvitationService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class InvitationServiceTest {
    
    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    PlayerService playerService;

    @Autowired
    InvitationService invitationService;
    Invitation invitation2 = new Invitation();

    @Test
    @Transactional
    public void testFindInvitationsById() {
        Collection<Player> friends = invitationService.findInvitationsById(5);
        assertTrue(friends.size() == 1, "The player with id 5 should have 1 invitation");
    }

    @Test
    @Transactional
    public void testFindMyRecInvitationById() {
        Collection<Invitation> invitations = invitationService.findMyRecInvitationById(1);
        assertTrue(invitations.size() == 1, "The player with id 1 should have 1 invitation request");
    }

    @Test
    @Transactional
    public void testFindSendInvitationById() {
        Collection<Invitation>  invitations = invitationService.findSendInvitationById(5);
        assertTrue(invitations.size() == 1 , "The player with id 5 should have 1 invitation send");
    }

    @Test
    @Transactional
    public void testFindInvitationById() {
        Invitation invitation = invitationService.findInvitationById(1);
        assertTrue(invitation.getId() == 1, "There should be a invitation with an id of 1.");
    }

    @Test
    @Transactional
    public void testFindInvitationByPlayersAndRoomId() {
        Invitation invitation = invitationService.findInvitationByPlayersAndRoomId(1, 5, 1);
        assertNotNull(invitation, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, invitation.getId(), "Hay una invitación duplicada");
    }

    @Test
    @Transactional
    public void testSaveInvitation() {
        Player player1 = playerService.findPlayerById(3);
        Player player2 = playerService.findPlayerById(4);

        invitation2.setIsViewer(false);
        invitation2.setPlayerInvitationSend(player1);
        invitation2.setPlayerInvitationRec(player2);
        invitationService.saveInvitation(invitation2);
        assertNotNull(invitationService.findInvitationById(2), "There should be a invitation with an id of 20.");
    }

    @Test
    @Transactional
    public void testDeleteInvitationById() {
        invitationService.deleteInvitationById(1);
        assertFalse(invitationRepository.findById(2).isPresent(), "The invitation should have been deleted");
    }
}
