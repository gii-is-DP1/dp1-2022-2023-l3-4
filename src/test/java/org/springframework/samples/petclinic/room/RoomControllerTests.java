package org.springframework.samples.petclinic.room;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RoomController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class RoomControllerTests {
    
    public static final int TEST_ROOM_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @MockBean
    private PlayerService playerService;

    @MockBean 
    private AuthenticationService authenticationService;

    @MockBean 
    private GameService gameService;

    private Room room;

    @BeforeEach
    void setup() {
        room = new Room();
        room.setId(TEST_ROOM_ID);
        room.setRoomName("sala5");
        room.setIsPrivate(true);
        room.setNumMaxPlayers(5);
        Player player = new Player();
            player.setFirstName("Jefferson");
            player.setLastName("Nunez");
            room.setHost(player);
            
            Player player2 = new Player();
            player2.setFirstName("Avery");
            player2.setLastName("Pruitt");

            Player player3 = new Player();
            player3.setFirstName("Kimberly");
            player3.setLastName("McMahan");
            
            Collection<Player> players = new ArrayList<Player>();
            players.add(player2);
            players.add(player3);
            
            room.setPlayers(players);
            
            given(this.roomService.findRoomById(TEST_ROOM_ID)).willReturn(room);
    }

    @WithMockUser
    @Test
    void testInitRoomCreationForm() throws Exception {
        mockMvc.perform(get("/room/new"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("room"))
        .andExpect(view().name("rooms/createOrUpdateRoomForm"));
    }

    @WithMockUser
    @Test
    void testCreateSearch() throws Exception {
        mockMvc.perform(get("/room/createSearch"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("room"))
        .andExpect(view().name("rooms/createOrSearch"));
    }

    @WithMockUser
	@Test
	void testProcessFindRoomFormSuccess() throws Exception {
		given(this.roomService.findRoomsByRoomName("")).willReturn(Lists.newArrayList(room, new Room()));

		mockMvc.perform(get("/room/find")).andExpect(status().isOk()).andExpect(view().name("rooms/roomsList"));
	}

}
