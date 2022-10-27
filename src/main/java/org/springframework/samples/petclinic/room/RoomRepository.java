package org.springframework.samples.petclinic.room;

import org.springframework.data.repository.CrudRepository;


public interface RoomRepository extends  CrudRepository<Room, String>{
	
}