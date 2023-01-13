package org.springframework.samples.petclinic.invitations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.invitation.Invitation;
import org.springframework.samples.petclinic.invitation.InvitationRepository;
import org.springframework.samples.petclinic.player.Player;

@DataJpaTest
public class InvitationRepositoryTest {
    @Autowired
    InvitationRepository invitationRepository;

    @Test
    public void testFriendBySendId() {
        Collection<Player> players = invitationRepository.findFriendBySendId(5);
        assertNotNull(players, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, players.size(), "Faltan datos de inicialización");
    }

    @Test
    public void testFriendByRecId() {
        Collection<Player> players = invitationRepository.findFriendByRecId(1);
        assertNotNull(players, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, players.size(), "Faltan datos de inicialización");
    }

    @Test
    public void testFindMyRecInvitationById() {
        Collection<Invitation> invitations = invitationRepository.findMyRecInvitationById(1);
        assertNotNull(invitations, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, invitations.size(), "Faltan datos de inicialización");
    }

    @Test
    public void testFindAllMySendInvitationsById() {
        Collection<Invitation> invitations = invitationRepository.findAllMySendInvitationsById(5);
        assertNotNull(invitations, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, invitations.size(), "Faltan datos de inicialización");
    }

    @Test
    public void testFindByPlayersAndRoomId() {
        Invitation invitation = invitationRepository.findByPlayersAndRoomId(1, 5, 1);
        assertNotNull(invitation, "El reposisotorio ha devuelto una lista nula");
        assertEquals(1, invitation.getId(), "Hay dos invitaciones iguales");
    }
}
