package org.springframework.samples.petclinic.achievements;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics/achievements")
public class AchievementController {

  private AchievementService achievementService;
  public static final String ACHIEVEMENT_LISTING = "achievements/AchievementsListing";
  public static final String GET_ACHIEVEMENT = "achievements/Achievement";
  public static final String EDIT_ACHIEVEMENT = "achievements/createOrUpdateAchievementForm";

  @Autowired
  public AchievementController(AchievementService achievementService) {
    this.achievementService = achievementService;
  }

  @GetMapping("/")
  public String listAllAchievements(ModelMap model) {
    List<Achievement> allAchievements = achievementService.getAllAchievements();
    model.put("achievements", allAchievements);
    return ACHIEVEMENT_LISTING;
  }

  @GetMapping("/me")
  public String getMyAchiements(ModelMap model, Principal principal) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    List<Achievement> myAchievements = achievementService.getMyAchievements(auth.getPrincipal().toString());
    model.put("achievements", myAchievements);
    return ACHIEVEMENT_LISTING;
  }

  @GetMapping("/{id}/edit")
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
  
  @PostMapping("/{id}/edit")
  public String saveAchievement(@PathVariable("id") Integer id, @Valid Achievement achievement, BindingResult bindingResult, ModelMap model) {
    if (bindingResult.hasErrors()) {
      model.put("message", "Description cannot be empty");
      model.put("messageType", "info");
      return EDIT_ACHIEVEMENT;
    } else {
      Achievement achievementToUpdate = achievementService.getAchievement(id);
      if (achievementToUpdate != null) {
        BeanUtils.copyProperties(achievement, achievementToUpdate, "id");
        achievementService.saveAchievement(achievementToUpdate);
        model.put("message", "Achievement " + id + " succesfully updated");
        return listAllAchievements(model);
      } else {
        model.put("message", "Achievement " + id + " doesn't exist");
        model.put("messageType", "info");
        return listAllAchievements(model);
      }
    }
  }
  
  @GetMapping("/{id}/delete")
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

  @GetMapping("/new")
  public String addAchievement(ModelMap model) {
    model.put("achievement", new Achievement());
    return EDIT_ACHIEVEMENT;
  }
  @PostMapping("/new")
  public String saveAchievement(@Valid Achievement achievement, BindingResult bindingResult, ModelMap model) {
    if (bindingResult.hasErrors()) {
      model.put("message", "Description cannot be empty");
      model.put("messageType", "info");
      return EDIT_ACHIEVEMENT;
    } else { 
      Achievement newAchievement = new Achievement();
      BeanUtils.copyProperties(achievement, newAchievement, "id");
      Achievement createdAchievement = achievementService.saveAchievement(newAchievement);
      model.put("message", "Achievement " + createdAchievement.getId() + " succesfully created");
      return "redirect:/statistics/achievements/"; // con esto evitamos que vuelvan a cargar el formulario en servidor
    }
  }

}
