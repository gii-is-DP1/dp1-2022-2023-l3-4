package org.springframework.samples.petclinic.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collection;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.player.PlayerRepository;



@DataJpaTest
public class RoomRepositoryTest {
    @Autowired
    RoomRepository roomRepository;
    
    @Autowired
    PlayerRepository playerRepository;

    @Test
    public void testFindRoomsByRoomName() {
        Collection<Room> rooms = roomRepository.findRoomsByRoomName("prueba");
        assertNotNull(rooms, "El repositorio ha devuelto una lista nula");
        assertEquals(1,rooms.size(),"Faltan datos de inicialización");
    }

    @Test
    public void testFindByRoomName() {
        Room room = roomRepository.findByRoomName("prueba");
        assertNotNull(room, "El repositorio ha devuelto una lista nula");
        assertEquals("prueba", room.getRoomName(), "El nombre de la sala buscada no coincide con la devuelta del repositrio");
    }

    @Test
    public void testfindRoomByHost() {
        Room room = roomRepository.findRoomByHost(playerRepository.findById(5).get());
        assertNotNull(room, "La sala debe tener un host asignado");
        assertEquals("prueba", room.getRoomName(), "El nombre de la sala no coincide con la del host");
    }

}
