package org.springframework.samples.petclinic.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends  CrudRepository<User, String>{

  Page<User> findAll(Pageable pageable);
	
}
