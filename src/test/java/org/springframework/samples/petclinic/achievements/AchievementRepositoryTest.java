package org.springframework.samples.petclinic.achievements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AchievementRepositoryTest {
    @Autowired
    AchievementRepository achievementRepository;

    @Test
    public void shouldFindAchievements() {
        List<Achievement> achievements = achievementRepository.findAchievementsBelowThreshold(2);
        assertFalse(achievements.isEmpty(), "The list of achievements should not be empty.");
        assertEquals(2 ,achievements.size());
    }

    @Test
    public void shouldFindAchievementTypes() {
        List<AchievementType> achievementTypes = achievementRepository.findAllTypes();

        assertFalse(achievementTypes.isEmpty(), "The list of achievement types should not be empty.");
        assertEquals(5, achievementTypes.size());
    }


}
