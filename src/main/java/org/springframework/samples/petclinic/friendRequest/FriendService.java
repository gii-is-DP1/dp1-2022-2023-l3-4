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
    public Collection<Player> findFriendsById(Integer playerId) throws DataAccessException {
        Collection<Player> friendRec=friendRepository.findFriendByRecId(playerId);
        Collection<Player> friendSend=friendRepository.findFriendBySendId(playerId);
        Collection<Player> friends=new ArrayList<>(friendRec);
        friends.addAll(friendSend);
        return friends;
    }

    @Transactional
    public Collection<Friend> findMyRecRequestById(Integer playerId) throws DataAccessException {
        Collection<Friend> requestRec=friendRepository.findMyRecRequestById(playerId);
        return requestRec;
    }

    @Transactional
    public Collection<Friend> findAllMyRequestById(Integer playerId) throws DataAccessException {
        Collection<Friend> myFriendRec=friendRepository.findMyRecRequestById(playerId);
        Collection<Friend> myFriendSend=friendRepository.findMySendRequestById(playerId);
        Collection<Friend> request=new ArrayList<>(myFriendRec);
        request.addAll(myFriendSend);
        return request;
    }

    @Transactional
    public Friend findFriendById(Integer playerId) throws DataAccessException {
        Friend friend=friendRepository.findById(playerId).get();
        return friend;
    }

    @Transactional
    public Friend findByPlayersId(Integer player1,Integer player2) throws DataAccessException {
        return friendRepository.findByPlayersId(player1,player2);
    }

    @Transactional
    public void deleteFriendById(Integer requestId) throws DataAccessException{
        friendRepository.deleteById(requestId);
    }
}
