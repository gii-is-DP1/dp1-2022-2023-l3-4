package org.springframework.samples.petclinic.invitation;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {
    
    private InvitationRepository invitationRepository;

    @Autowired
    public InvitationService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Transactional
    public Collection<Player> findInvitationsById(Integer playerId) throws DataAccessException {
        Collection<Player> friendRec=invitationRepository.findFriendByRecId(playerId);
        Collection<Player> friendSend=invitationRepository.findFriendBySendId(playerId);
        Collection<Player> friends=new ArrayList<>(friendRec);
        friends.addAll(friendSend);
        return friends;
    }

    @Transactional
    public Collection<Invitation> findMyRecInvitationById(Integer playerId) throws DataAccessException {
        Collection<Invitation> invitationRec = invitationRepository.findMyRecInvitationById(playerId);
        return invitationRec;
    }

    @Transactional
    public Collection<Invitation> findSendInvitationById(Integer playerId) throws DataAccessException {
        Collection<Invitation> invitationSend = invitationRepository.findAllMySendInvitationsById(playerId);
        return invitationSend;
    }

    @Transactional
    public Invitation findInvitationById(Integer playerId) throws DataAccessException {
        Invitation invitation = invitationRepository.findById(playerId).get();
        return invitation;
    }

    @Transactional
    public void deleteInvitationById(Integer requestId) throws DataAccessException {
        invitationRepository.deleteById(requestId);
    }
}

