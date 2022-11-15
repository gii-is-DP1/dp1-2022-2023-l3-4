package org.springframework.samples.petclinic.card;

import java.util.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {
    
    List<Card> findAll();
    Optional<Card> findById(Integer i);
    Card save(Card card);
    @Query("SELECT c FROM Card c WHERE c.body() AND c.gamePlayer_id = :gamePlayerId")
    List<Card> getBodyFromAGamePlayer(Integer gamePlayerId);
    
}
