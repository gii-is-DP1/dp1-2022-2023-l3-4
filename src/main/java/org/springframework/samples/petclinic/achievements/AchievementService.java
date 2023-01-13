package org.springframework.samples.petclinic.achievements;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.game.GameService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AchievementService {

  private AchievementRepository achievementRepository;
  private GameService gameService;

  @Autowired
  public AchievementService(AchievementRepository achievementRepository, GameService gameService) {
    this.achievementRepository = achievementRepository;
    this.gameService = gameService;
  }

  @Transactional(readOnly=true)
  public List<Achievement> getAllAchievements() {
    return achievementRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<AchievementType> findAchievementTypes() {
    return achievementRepository.findAllTypes();
  }

  @Transactional(rollbackFor = DuplicatedAchievementNameException.class)
  public Achievement saveAchievement(Achievement achievement) throws DuplicatedAchievementNameException {
    List<String> achNames = getAllAchievements().stream().map(x -> x.getName()).collect(Collectors.toList());
    if (achNames.contains(achievement.getName())) {
      throw new DuplicatedAchievementNameException();
    } else {
      return this.achievementRepository.save(achievement);
    }
  }

  @Transactional
  public Achievement updateAchievement(Achievement achievement) {
    return this.achievementRepository.save(achievement);
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

  @Transactional(readOnly = true)
  public List<Achievement> getPlayerAchievements(Player p) {
    Integer numWonGames = gameService.getNumGamesWon(p.getGamePlayer());
    return achievementRepository.findAchievementsBelowThreshold(numWonGames);
  }
  
}
