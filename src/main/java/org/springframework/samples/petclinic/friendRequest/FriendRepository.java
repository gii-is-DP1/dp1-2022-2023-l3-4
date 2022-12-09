package org.springframework.samples.petclinic.friendRequest;


import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.player.Player;

public interface FriendRepository extends CrudRepository<Friend,Integer>{

    @Query("SELECT f.playerRec FROM Friend f WHERE f.playerSend.id = :playerId AND f.status = true")
    public Collection<Player> findFriendBySendId(@Param("playerId") Integer id);

    @Query("SELECT f.playerSend FROM Friend f WHERE f.playerRec.id = :playerId AND f.status = true")
    public Collection<Player> findFriendByRecId(@Param("playerId") Integer id);

    @Query("SELECT f FROM Friend f WHERE f.playerSend.id = :playerId AND f.status = null")
    public Collection<Friend> findMySendRequestById(@Param("playerId") Integer id);

    @Query("SELECT f FROM Friend f WHERE f.playerRec.id = :playerId AND f.status = null")
    public Collection<Friend> findMyRecRequestById(@Param("playerId") Integer id);

    @Query("Select f FROM Friend f WHERE ( (f.playerRec.id = :player1Id AND f.playerSend.id = :player2Id) OR (f.playerRec.id = :player2Id AND f.playerSend.id = :player1Id ) ) ")
    public Friend findByPlayersId(@Param("player1Id") Integer player1Id,@Param("player2Id") Integer player2Id);

    
}
