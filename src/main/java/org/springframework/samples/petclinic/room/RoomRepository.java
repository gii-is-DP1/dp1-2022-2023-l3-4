package org.springframework.samples.petclinic.room;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.player.Player;

public interface RoomRepository extends CrudRepository<Room,Integer>{

    
    @Query("SELECT room FROM Room room WHERE room.roomName like :roomName%")
	public Collection<Room> findRoomsByRoomName(@Param("roomName") String roomName);

    @Query("SELECT room FROM Room room WHERE room.roomName =:roomName")
	public Room findByRoomName(@Param("roomName") String roomName);

    @Query("SELECT room FROM Room room WHERE room.host =:player")
    public Room findRoomByHost(@Param("player") Player player);
    


}
