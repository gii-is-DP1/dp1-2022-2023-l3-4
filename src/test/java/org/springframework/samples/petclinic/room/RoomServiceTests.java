package org.springframework.samples.petclinic.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.springframework.samples.petclinic.player.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.room.exceptions.DuplicatedNameRoomException;
import org.springframework.samples.petclinic.room.exceptions.PlayerHostsExistingRoomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class RoomServiceTests {
    
    @Autowired(required = false)
    RoomRepository roomRepository;

    @Autowired
    protected RoomService roomService;

    @Autowired
    protected PlayerService playerService;


    // @Test
    // void testConstraints(){
    //     Room room = new Room();
    //     room.setId(3);
    //     assertThrows(ConstraintViolationException.class,() -> roomRepository.save(room),
    //     "You are not constraining "+
    //     "the roomName property, it should be mandatory");

    //     Room room2 = new Room();
    //     room2.setId(3);
    //     room2.setRoomName("sala7");
    //     room2.setNumMaxPlayers(1);
    //     assertThrows(Exception.class,() -> roomRepository.save(room2),
    //     "The numMaxPLayers property must be between 2 and 6");

    //     Room room3 = new Room();
    //     room3.setId(3);
    //     room3.setRoomName("salaaaaaaaaaaaaaaaaaaaa7");
    //     room3.setNumMaxPlayers(4);
    //     assertThrows(Exception.class,() -> roomRepository.save(room3),
    //     "The roomName property must be between 1 and 20 characters");
    // }

    @Test
    @Transactional
    public void shouldInsertRoom() throws DuplicatedNameRoomException, PlayerHostsExistingRoomException {
        Collection<Room> rooms = this.roomService.findRoomsByRoomName("room");
        int found = rooms.size();

        Room room = new Room();
        room.setRoomName("sala3");
        room.setIsPrivate(true);
        room.setNumMaxPlayers(5);
            Player player = new Player();
            player.setFirstName("Jefferson");
            player.setLastName("Nunez");
            room.setHost(player);
            player.setRoom(room);

            Player player2 = new Player();
            player2.setFirstName("Avery");
            player2.setLastName("Pruitt");
            player2.setRoom(room);

            Player player3 = new Player();
            player3.setFirstName("Kimberly");
            player3.setLastName("McMahan");
            player3.setRoom(room);

            Collection<Player> Players = new ArrayList<Player>();
            Players.add(player);
            Players.add(player2);
            Players.add(player3);

            this.playerService.savePlayer(player);
            this.playerService.savePlayer(player2);
            this.playerService.savePlayer(player3);
            room.setPlayers(Players);

            this.roomService.saveRoom(room);
        assertThat(room.getId().longValue()).isNotEqualTo(0);

        rooms = this.roomService.findRoomsByRoomName("sala3");
        assertThat(rooms.size()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
    void shouldUpdateRoom() throws DuplicatedNameRoomException, PlayerHostsExistingRoomException {
        Room room = this.roomService.findRoomById(1);
        String oldRoomName = room.getRoomName();
        String newRoomName = oldRoomName + "X";
    
        room.setRoomName(newRoomName);
        this.roomService.saveRoom(room);
    
        room = this.roomService.findRoomById(1);
        assertThat(room.getRoomName()).isEqualTo(newRoomName);
    }
    

    @Test
    void shouldFindRoomsByRoomName() {
        Collection<Room> rooms = this.roomService.findRoomsByRoomName("prueba");
        assertThat(rooms.size()).isEqualTo(1);

        rooms = this.roomService.findRoomsByRoomName("sala1");
        assertThat(rooms.isEmpty()).isTrue();
    }

    @Test
    void shouldFindRoomByRoomName() {
        Room room = this.roomService.findRoomByRoomName("prueba");
        assertNotNull(room);

        room = this.roomService.findRoomByRoomName("sala2");
        assertNull(room);
    }

    @Test
    void shouldFindRoomByHost() {
        Room room1 = this.roomService.findRoomByHost(playerService.findPlayerById(5));
        assertNotNull(room1);

        room1 = this.roomService.findRoomByHost(playerService.findPlayerById(1));
        assertNull(room1);
    }

    @Test
    void shouldFindRoomById() {
        Room room = this.roomService.findRoomById(1);
        assertNotNull(room);

        room = this.roomService.findRoomById(2);
        assertNull(room);
    }

    @Test
    void shouldDeleteRoom() {
        roomService.deleteRoom(1);
        assertNull(roomService.findRoomById(1), "Room with ID 1 should have been deleted");
    }



}
