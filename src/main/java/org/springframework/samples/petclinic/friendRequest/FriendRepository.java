package org.springframework.samples.petclinic.friendRequest;


import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.player.Player;

public interface FriendRepository extends CrudRepository<Friend,Integer>{

    // @Query("SELECT f FROM Friend f WHERE f.requestingPlayer = :playerId")
    // public Collection<Player> findFriendById(@Param("playerId") Integer playerId);

    
}
