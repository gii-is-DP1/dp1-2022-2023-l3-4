package org.springframework.samples.petclinic.achievements;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = AchievementController.class, 
  includeFilters = @ComponentScan.Filter(value = AchievementTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
  excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), 
  excludeAutoConfiguration = SecurityConfiguration.class)
public class AchievementControllerTests {

  private static final Integer TEST_ACHIEVEMENT_ID = 1;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AchievementService achievementService;

  @MockBean
  private AuthenticationService authenticationService;

  @MockBean
  private PlayerService playerService;

  @BeforeEach
  void setup() {
    AchievementType maestro = new AchievementType();
    maestro.setId(10);
    maestro.setName("Maestro");
    given(achievementService.findAchievementTypes()).willReturn(Lists.newArrayList(maestro));

    GrantedAuthority adminAuth = new GrantedAuthority() {
      
      @Override
      public String getAuthority() {
        return "ADMIN";
      }
    };
    User admin = new User("admin1", "4dm1n", List.of(adminAuth));
    when(authenticationService.getUser()).thenReturn(admin);
    
    Achievement testAchievement = new Achievement();
    testAchievement.setName("Name");
    testAchievement.setDescription("Description");
    testAchievement.setType(new AchievementType());
    when(achievementService.getAchievement(TEST_ACHIEVEMENT_ID)).thenReturn(testAchievement);
  }

  @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
  @Test
  public void testNewAchievement() throws Exception {
    mockMvc.perform(get("/statistics/achievements/new"))
    .andExpect(status().isOk())
    .andExpect(view().name("achievements/createOrUpdateAchievementForm"))
      .andExpect(model().attributeExists("achievement"));
  }
  
  @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
  @Test
  public void testNewAchievementPost() throws Exception {
    mockMvc.perform(post("/statistics/achievements/new")
      .with(csrf())
      .param("name", "Rompe")
      .param("description", "Nueva descripcion")
      .param("threshold", "5")
      .param("type", "Maestro"))
      .andExpect(status().isOk())
      .andExpect(model().attributeExists("message"))
      .andExpect(view().name("achievements/achievementsListing"));
    }

  @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
  @Test
  public void testNewAchievementPostDuplicatedName() throws Exception {
    when(achievementService.saveAchievement(any())).thenThrow(DuplicatedAchievementNameException.class);
    mockMvc.perform(post("/statistics/achievements/new")
    .with(csrf())
      .param("name", "Muro")
      .param("description", "Nueva descripcion")
      .param("threshold", "5")
      .param("type", "Maestro"))
      .andExpect(status().isOk())
      .andExpect(view().name("achievements/invalidAchievement"));
    }
    
    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testEditAchievement() throws Exception {
      mockMvc.perform(get("/statistics/achievements/{id}/edit", TEST_ACHIEVEMENT_ID))
      .andExpect(status().isOk())
      .andExpect(view().name("achievements/createOrUpdateAchievementForm"))
      .andExpect(model().attributeExists("achievement"));
    }
    
    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testEditAchievementPost() throws Exception {
      mockMvc.perform(post("/statistics/achievements/{id}/edit", TEST_ACHIEVEMENT_ID)
        .with(csrf())
        .param("name", "New name")
        .param("description", "New description")
        .param("threshold", "5"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("message"))
        .andExpect(view().name("achievements/achievementsListing"));
    }

    @WithMockUser(value = "admin1", password = "4dm1n", roles = "ADMIN")
    @Test
    public void testDeleteAchievement() throws Exception {
        mockMvc.perform(get("/statistics/achievements/{id}/delete", TEST_ACHIEVEMENT_ID))
          .andExpect(status().isOk())
          .andExpect(view().name("achievements/achievementsListing"))
          .andExpect(model().attributeExists("message"));
    }

}
