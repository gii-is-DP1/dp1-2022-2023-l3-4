package org.springframework.samples.petclinic.room;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends CrudRepository<Room,Integer>{

    
    @Query("SELECT room FROM Room room WHERE room.roomName like :roomName%")
	public Collection<Room> findByRoomName(@Param("roomName") String roomName);
    

}
