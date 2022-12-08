package org.springframework.samples.petclinic.friendRequest;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    

    private FriendRepository friendRepository;
    

    @Autowired
    public FriendService(FriendRepository friendRepository){
        this.friendRepository = friendRepository;
    }

    @Transactional
    public void savePlayer(Friend friend) throws DataAccessException {
        friendRepository.save(friend);
    }

    @Transactional
    public Collection<Player> findFriendById(Integer playerId) throws DataAccessException {
        Collection<Player> friendRec=friendRepository.findFriendBySendId(playerId);
        Collection<Player> friendSend=friendRepository.findFriendByRecId(playerId);
        Collection<Player> friends=new ArrayList<>(friendRec);
        friends.addAll(friendSend);
        return friends;
    }
}
