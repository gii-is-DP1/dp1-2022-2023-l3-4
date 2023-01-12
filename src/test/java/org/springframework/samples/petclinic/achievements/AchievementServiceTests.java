package org.springframework.samples.petclinic.achievements;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AchievementServiceTests {
  
  @Autowired
  protected AchievementService achievementService;

  @Autowired
  protected PlayerService playerService;

  @Test
  public void shouldFindAllAchievements() {
    Collection<Achievement> achievements = achievementService.getAllAchievements();
    assertNotNull(achievements.size() == 2, "There should be achievements in the database");
  }

  @Test
  public void shouldFindAchievementTypes() {
    Collection<AchievementType> achievementTypes = achievementService.findAchievementTypes();
    assertNotNull(achievementTypes.size() == 5, "There should be achievement types in the database");
  }

  @Test
  public void shouldSaveAchievement() throws DuplicatedAchievementNameException {
    Achievement achievement = new Achievement();
    achievement.setName("Rompiendo el sistema");
    achievement.setDescription("Nada que objetar");
    achievement.setThreshold(100);
    achievement.setType(achievementService.findAchievementTypes().get(0));
    Achievement newAchievement;
    newAchievement = achievementService.saveAchievement(achievement);
    assertTrue(newAchievement.getName().equals(achievement.getName()), "The achievement was not saved ");
  }
  
  @Test
  public void shouldNotSaveAchievement() throws DuplicatedAchievementNameException {
    Achievement achievement = new Achievement();
    achievement.setName("Viciado");
    achievement.setDescription("Nada que objetar");
    achievement.setType(achievementService.findAchievementTypes().get(0));
    assertThrows(DuplicatedAchievementNameException.class, () -> achievementService.saveAchievement(achievement));
  }

}
