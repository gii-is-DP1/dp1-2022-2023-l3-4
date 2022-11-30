package org.springframework.samples.virus.card;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericCardRepository extends CrudRepository<GenericCard, Integer> {
    List<GenericCard> findAll();
}
