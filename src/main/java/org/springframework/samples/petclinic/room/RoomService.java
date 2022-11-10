package org.springframework.samples.petclinic.room;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.room.exceptions.DuplicatedNameRoomException;
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

    @Transactional(readOnly = true)
	public Room findRoomById(int id) throws DataAccessException {
		return roomRepository.findById(id).orElse(null);
	}

	@Transactional(rollbackFor = DuplicatedNameRoomException.class)
	public void saveRoom(Room room) throws DataAccessException, DuplicatedNameRoomException {
			if(roomRepository.findByRoomName(room.roomName)!=null){
				throw new DuplicatedNameRoomException();
           
			}else
				roomRepository.save(room);
	}
    
}
