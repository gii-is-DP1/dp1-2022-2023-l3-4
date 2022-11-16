package org.springframework.samples.petclinic.card;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class GenericCardService {
    
    private GenericCardRepository gCardRepository;

	@Autowired
	public GenericCardService(GenericCardRepository gCardRepository) {
		this.gCardRepository = gCardRepository;
	}

	@Transactional(readOnly = true)
	public List<GenericCard> listGCards(){
		return gCardRepository.findAll();
	}

    @Transactional
	public GenericCard save(GenericCard gcard){
		return gCardRepository.save(gcard);	
	}

	@Transactional(readOnly = true)	
	public List<GenericCard> findGCards() throws DataAccessException {
		return gCardRepository.findAll();
	}	
}
