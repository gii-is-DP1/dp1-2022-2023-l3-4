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

    
}
