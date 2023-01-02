package org.springframework.samples.petclinic.card;

import java.util.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<Card, Integer> {
    
    List<Card> findAll();
    Optional<Card> findById(Integer i);

    @Query("SELECT c FROM Card c WHERE c.body=1 AND c.gamePlayer.id = :gamePlayerId")
    List<Card> getBodyFromAGamePlayer(Integer gamePlayerId);

    @Query("SELECT c FROM Card c WHERE c.id IN :cardIds")
    List<Card> findCardsByIds(@Param("cardIds") List<Integer> cardIds);
    
}
