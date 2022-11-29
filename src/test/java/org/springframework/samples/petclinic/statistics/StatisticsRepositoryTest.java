package org.springframework.samples.petclinic.statistics;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.samples.petclinic.player.Player;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class StatisticsRepositoryTest {

  private static final Integer ID_PLAYER = 1;  

  @Autowired
  StatisticsRepository statisticsRepository;

  @Test
  public void testFindPlayerStatisticsbyPlayer() throws Exception {
  
    Player player = new Player();
    player.setId(ID_PLAYER);

    Statistics statistics = statisticsRepository.findByPlayer(player);
    assertNotNull(statistics);
  }
  
}
