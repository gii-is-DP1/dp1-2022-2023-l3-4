package org.springframework.samples.petclinic.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.gamePlayer.GamePlayerService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class UserControllerTests{
    
    private static final String TEST_USERNAME = "frabenrui1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthoritiesService authoritiesService;

    @MockBean
    private UserService userService;

    @MockBean
    private GamePlayerService gamePlayerService;
    
    @BeforeEach
    void setUp() {
        Player mockPlayer = new Player();
        User user = new User();
        user.setUsername(TEST_USERNAME);
        mockPlayer.setUser(user);
        List<Player> l = List.of(mockPlayer);
        Page<Player> mockPlayers = new PageImpl<>(l);
        when(playerService.findAll(any())).thenReturn(mockPlayers);
        when(userService.findUser(TEST_USERNAME)).thenReturn(user);
        when(playerService.getPlayerByUsername(TEST_USERNAME)).thenReturn(mockPlayer);
    }

    @WithMockUser(value = "spring")
    @Test
    public void testInitCreationForm() throws Exception{
        mockMvc.perform(get("/user/new")).andExpect(status().isOk()).andExpect(model().attributeExists("player"))
        .andExpect(view().name("users/createPlayerForm"));
    }
    
    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testFindAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("currentPage"))
            .andExpect(view().name("users/usersListing"));
    }

    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testFindAllUsersNoUsers() throws Exception {
        when(playerService.findAll(any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("message"))
            .andExpect(view().name("users/usersListing"));
    }

    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testEditUser() throws Exception {
        mockMvc.perform(get("/users/{username}/edit", TEST_USERNAME))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("player"))
        .andExpect(view().name("player/createOrUpdateProfileForm"));
    }
    
    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testEditPostUserSuccessful() throws Exception {
        mockMvc.perform(post("/users/{username}/edit", TEST_USERNAME)
            .with(csrf())
            .param("password", "pepito"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("message"))
            .andExpect(view().name("users/usersListing"));
    }

    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testEditPostUserNoPassword() throws Exception {
        mockMvc.perform(post("/users/{username}/edit", TEST_USERNAME)
        .with(csrf())
        .param("password",""))
        .andExpect(status().isOk())
        .andExpect(view().name("users/usersListing"))
        .andExpect(model().hasErrors());
    }
    
    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(get("/users/{username}/delete", TEST_USERNAME))
            .andExpect(status().isOk())
            .andExpect(view().name("users/usersListing"))
            .andExpect(model().attributeExists("message"));
    }



}