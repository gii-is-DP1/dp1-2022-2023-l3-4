package org.springframework.samples.petclinic.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.virus.room.Room;
import org.springframework.samples.virus.room.RoomRepository;
import org.springframework.samples.virus.room.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class RoomServiceTests {
    
    @Autowired(required = false)
    RoomRepository roomRepository;

    @Autowired
    protected RoomService roomService;

    @Test
    void testConstraints(){
        Room room = new Room();
        room.setId(3);
        assertThrows(ConstraintViolationException.class,() -> roomRepository.save(room),
        "You are not constraining "+
        "the roomName property, it should be mandatory");

        Room room2 = new Room();
        room2.setId(3);
        room2.setRoomName("sala7");
        room2.setNumMaxPlayers(1);
        assertThrows(Exception.class,() -> roomRepository.save(room2),
        "The numMaxPLayers property must be between 2 and 6");

        Room room3 = new Room();
        room3.setId(3);
        room3.setRoomName("salaaaaaaaaaaaaaaaaaaaa7");
        room3.setNumMaxPlayers(4);
        assertThrows(Exception.class,() -> roomRepository.save(room3),
        "The roomName property must be between 1 and 20 characters");
    }

    @Test
    void shouldFindRoomByRoomName() {
        Collection<Room> rooms = this.roomService.findRoomsByRoomName("sala1");
        assertThat(rooms.size()).isEqualTo(1);

        rooms = this.roomService.findRoomsByRoomName("salaa1");
        assertThat(rooms.isEmpty()).isTrue();
    }



    @Test
    @Transactional
    void shouldUpdateRoom() {
        Room room = this.roomService.findRoomById(1);
        String oldRoomName = room.getRoomName();
        String newRoomName = oldRoomName + "X";

        room.setRoomName(newRoomName);

        room = this.roomService.findRoomById(1);
        assertThat(room.getRoomName()).isEqualTo(newRoomName);
    }

}
