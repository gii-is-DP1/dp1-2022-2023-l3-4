package org.springframework.samples.petclinic.achievements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AchievementTypeFormatterTests {

  @Mock
  private AchievementService achievementService;

  private AchievementTypeFormatter achievementTypeFormatter;

  @BeforeEach
  void setup() {
    achievementTypeFormatter = new AchievementTypeFormatter(achievementService);
  }

  @Test
  public void testPrint() {
    AchievementType achievementType = new AchievementType();
    achievementType.setName("Great Master");
    String achievementTypeName = achievementTypeFormatter.print(achievementType, Locale.ENGLISH);
    assertEquals("Great Master", achievementTypeName);
  }

  @Test
	void shouldParse() throws ParseException {
		Mockito.when(achievementService.findAchievementTypes()).thenReturn(makeAchievementTypes());
		AchievementType achievementType = achievementTypeFormatter.parse("Master", Locale.ENGLISH);
		assertEquals("Master", achievementType.getName());
	}

	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(achievementService.findAchievementTypes()).thenReturn(makeAchievementTypes());
		Assertions.assertThrows(ParseException.class, () -> {
			achievementTypeFormatter.parse("Great Master", Locale.ENGLISH);
		});
	}

  private List<AchievementType> makeAchievementTypes() {
    List<AchievementType> achievementTypes = new ArrayList<>();
    AchievementType at1 = new AchievementType();
    AchievementType at2 = new AchievementType();
    at1.setName("Master");
    at2.setName("Jedi");
    achievementTypes.add(at1);
    achievementTypes.add(at2);
    return achievementTypes;
  }
  
}
