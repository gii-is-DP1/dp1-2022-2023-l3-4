package org.springframework.samples.petclinic.room;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

public class RoomService {
    private RoomRepository roomRepository;

	@Autowired
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

    @Transactional
	public void saveRoom(Room room) throws DataAccessException {
        Integer code=(int)Math.floor(Math.random()*999999+1);
		room.setCode(code);
        room.setTotalGamesPlayed(0);
		roomRepository.save(room);
	}


}
