package org.springframework.samples.petclinic.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends  CrudRepository<User, String>{

  public List<User> findAll();

}
