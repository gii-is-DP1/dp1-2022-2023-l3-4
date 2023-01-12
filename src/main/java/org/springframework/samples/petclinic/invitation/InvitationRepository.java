package org.springframework.samples.petclinic.invitation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.player.Player;

public interface InvitationRepository extends CrudRepository<Invitation, Integer> {

    @Query("SELECT i.playerInvitationRec FROM Invitation i WHERE i.playerInvitationSend.id = :playerId")
    public Collection<Player> findFriendBySendId(@Param("playerId") Integer id);

    @Query("SELECT i.playerInvitationSend FROM Invitation i WHERE i.playerInvitationRec.id = :playerId")
    public Collection<Player> findFriendByRecId(@Param("playerId") Integer id);

    // @Query("SELECT f FROM Friend f WHERE f.playerSend.id = :playerId AND f.status = null")
    // public Collection<Friend> findMySendRequestById(@Param("playerId") Integer id);

    @Query("SELECT i FROM Invitation i WHERE i.playerInvitationRec.id = :playerId")
    public Collection<Invitation> findMyRecInvitationById(@Param("playerId") Integer id);

    @Query("SELECT i FROM Invitation i WHERE i.playerInvitationSend.id = :playerId ")
    public Collection<Invitation> findAllMySendInvitationsById(@Param("playerId") Integer id);

    // // @Query("Select f FROM Friend f WHERE ( (f.playerRec.id = :player1Id AND f.playerSend.id = :player2Id) OR (f.playerRec.id = :player2Id AND f.playerSend.id = :player1Id ) ) ")
    // // public Friend findByPlayersId(@Param("player1Id") Integer player1Id,@Param("player2Id") Integer player2Id);
    
}

