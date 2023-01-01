package org.springframework.samples.petclinic.achievements;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class AchievementTypeFormatter implements Formatter<AchievementType> {

  private final AchievementService achievementService;

  @Autowired
  public AchievementTypeFormatter(AchievementService as) {
    this.achievementService = as;
  }

  @Override
  public String print(AchievementType type, Locale locale) {
    return type.getName();
  }

  @Override
  public AchievementType parse(String text, Locale locale) throws ParseException {
    Collection<AchievementType> findAchievementTypes = this.achievementService.findAchievementTypes();
    for (AchievementType type: findAchievementTypes) {
      if (type.getName().equals(text)) {
        return type;
      }
    }
    throw new ParseException("Type not found: " + text, 0);
  }
  
}
