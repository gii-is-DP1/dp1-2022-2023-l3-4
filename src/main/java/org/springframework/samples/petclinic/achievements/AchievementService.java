package org.springframework.samples.petclinic.achievements;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchievementService {

  private AchievementRepository achievementRepository;

  @Autowired
  public AchievementService(AchievementRepository achievementRepository) {
    this.achievementRepository = achievementRepository;
  }

  @Transactional(readOnly=true)
  public List<Achievement> getAllAchievements() {
    return achievementRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Achievement> getMyAchievements(String username) {
    return null;
    // return achievementRepository.findByUsername(username);
  }

  @Transactional(readOnly = true)
  public List<AchievementType> findAchievementTypes() {
    return achievementRepository.findAllTypes();
  }

  @Transactional(rollbackFor = DuplicatedAchievementNameException.class)
  public Achievement saveAchievement(Achievement achievement) throws DuplicatedAchievementNameException {
    List<String> achNames = getAllAchievements().stream().map(x -> x.getName()).toList();
    if (achNames.contains(achievement.getName())) {
      throw new DuplicatedAchievementNameException();
    } else {
      return this.achievementRepository.save(achievement);
    }
  }

  @Transactional
  public void removeAchievement(Integer id) {
    this.achievementRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public Achievement getAchievement(Integer id) {
    Optional<Achievement> achievement = this.achievementRepository.findById(id);
    return achievement.isPresent() ? achievement.get() : null;
  }
  
}
