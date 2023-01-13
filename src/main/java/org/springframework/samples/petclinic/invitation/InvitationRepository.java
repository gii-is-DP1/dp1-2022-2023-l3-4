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

    @Query("SELECT i FROM Invitation i WHERE i.playerInvitationRec.id = :playerId")
    public Collection<Invitation> findMyRecInvitationById(@Param("playerId") Integer id);

    @Query("SELECT i FROM Invitation i WHERE i.playerInvitationSend.id = :playerId ")
    public Collection<Invitation> findAllMySendInvitationsById(@Param("playerId") Integer id);

    @Query("Select i FROM Invitation i WHERE (i.playerInvitationRec.id = :player1Id AND i.playerInvitationSend.id = :player2Id AND i.room.id = :roomId)")
    public Invitation findByPlayersAndRoomId(@Param("player1Id") Integer player1Id,@Param("player2Id") Integer player2Id, @Param("roomId") Integer roomId);
    
}

