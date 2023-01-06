package org.springframework.samples.petclinic.room;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.room.exceptions.DuplicatedNameRoomException;
import org.springframework.samples.petclinic.room.exceptions.PlayerHostsExistingRoomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {

    private RoomRepository roomRepository;


    @Autowired
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}	

	@Transactional
	public Collection<Room> findRoomsByRoomName(String roomName) throws DataAccessException{
		return roomRepository.findRoomsByRoomName(roomName);
	}

	@Transactional
	public Room findRoomByRoomName(String roomName) throws DataAccessException{
		return roomRepository.findByRoomName(roomName);
	}

	@Transactional
	public Room findRoomByHost(Player host) throws DataAccessException{
		return roomRepository.findRoomByHost(host);
	}


    @Transactional(readOnly = true)
	public Room findRoomById(int id) throws DataAccessException {
		return roomRepository.findById(id).orElse(null);
	}

	@Transactional(rollbackFor = DuplicatedNameRoomException.class)
	public void saveRoom(Room room) throws DataAccessException, DuplicatedNameRoomException, PlayerHostsExistingRoomException {
			Room sameRoomName=roomRepository.findByRoomName(room.getRoomName());
			Room sameHostRoom=roomRepository.findRoomByHost(room.getHost());
			if(sameRoomName!=null){
				throw new DuplicatedNameRoomException();
			}else if(sameHostRoom!=null ) {
				throw new PlayerHostsExistingRoomException();
			}else
				roomRepository.save(room);
			}

	@Transactional()
	public void updateRoom(Room room) throws DuplicatedNameRoomException{

		Room existRoom=roomRepository.findByRoomName(room.getRoomName());
			if(existRoom!=null && 
			existRoom.getHost()!=room.getHost()){
					throw new DuplicatedNameRoomException();
			}else
				roomRepository.save(room);
			}

	@Transactional
	public void deleteRoom(int id) throws DataAccessException {
		roomRepository.deleteById(id);
	}
    
}
