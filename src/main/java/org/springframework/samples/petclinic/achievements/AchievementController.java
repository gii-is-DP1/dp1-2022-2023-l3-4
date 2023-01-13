package org.springframework.samples.petclinic.achievements;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AchievementController {

  private AchievementService achievementService;
  private AuthenticationService authenticationService;
  public static final String ACHIEVEMENT_LISTING = "achievements/achievementsListing";
  public static final String GET_ACHIEVEMENT = "achievements/Achievement";
  public static final String EDIT_ACHIEVEMENT = "achievements/createOrUpdateAchievementForm";
  public static final String INVALID_ACH = "achievements/invalidAchievement";
  
  
  @Autowired
  public AchievementController(AchievementService achievementService, AuthenticationService authenticationService) {
    this.achievementService = achievementService;
    this.authenticationService = authenticationService;
  }
  
  @ModelAttribute("types")
	public Collection<AchievementType> populateAchievementTypes() {
    return this.achievementService.findAchievementTypes();
	}
  
  @GetMapping("/statistics/achievements")
  public String listAllAchievements(ModelMap model) {
    List<Achievement> allAchievements = achievementService.getAllAchievements();
    Boolean isAdmin = authenticationService.getUser().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"));
    model.put("achievements", allAchievements);
    model.put("isAdmin", isAdmin);
    return ACHIEVEMENT_LISTING;
  }
  
  @GetMapping("/statistics/achievements/new")
  public String addAchievement(ModelMap model) {
    model.put("achievement", new Achievement());
    model.put("types", populateAchievementTypes());
    return EDIT_ACHIEVEMENT;
  }
  @PostMapping("/statistics/achievements/new")
  public String saveAchievement(@Valid Achievement achievement, BindingResult bindingResult, ModelMap model) {
    if (bindingResult.hasFieldErrors("description")) {
      model.put("message", "The description cannot be empty");
      model.put("messageType", "info");
      return EDIT_ACHIEVEMENT;
    } else { 
      Achievement newAchievement = new Achievement();
      BeanUtils.copyProperties(achievement, newAchievement);
      try {
        achievementService.saveAchievement(newAchievement);
        model.put("message", "Achievement successfully created");
        return listAllAchievements(model);
      } catch (DuplicatedAchievementNameException e) {
        return INVALID_ACH;
      }
    }
  }
  
  @GetMapping("/statistics/achievements/{id}/edit")
  public String getAchievement(@PathVariable("id") Integer id, ModelMap model) {
    Achievement achievement = achievementService.getAchievement(id);
    if (achievement != null) {
      model.put("achievement", achievement);
      return EDIT_ACHIEVEMENT;
    } else {
      model.put("message", "The achievement " + id + " doesn't exist");
      model.put("messageType", "info");
      return listAllAchievements(model);
    }
  }
  
  @PostMapping("/statistics/achievements/{id}/edit")
  public String saveAchievement(@PathVariable("id") Integer id, @Valid Achievement achievement, BindingResult bindingResult, ModelMap model) {
    if (bindingResult.hasErrors()) {
      model.put("message", "Description cannot be empty");
      model.put("messageType", "info");
      return EDIT_ACHIEVEMENT;
    } else {
      Achievement achievementToUpdate = achievementService.getAchievement(id);
      if (achievementToUpdate != null) {
        BeanUtils.copyProperties(achievement, achievementToUpdate, "id");
        achievementService.updateAchievement(achievementToUpdate);
        model.put("message", "Achievement " + id + " succesfully updated");
        return listAllAchievements(model);
      } else {
        model.put("message", "Achievement " + id + " doesn't exist");
        model.put("messageType", "info");
        return listAllAchievements(model);
      }
    }
  }
  
  @GetMapping("/statistics/achievements/{id}/delete")
  public String removeAchievement(@PathVariable("id") Integer id, ModelMap model) {
    String message;

    try {
      achievementService.removeAchievement(id);
      message = "Achievement " + id + " succesfully deleted";
    } catch (EmptyResultDataAccessException e) {
      message = "Achievement " + id + " doesn't exist";
    }
    model.put("message", message);
    model.put("messageType", "info");
    return listAllAchievements(model);
  }

}
