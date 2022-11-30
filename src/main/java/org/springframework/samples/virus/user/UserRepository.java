package org.springframework.samples.virus.user;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends  CrudRepository<User, String>{
	
}
