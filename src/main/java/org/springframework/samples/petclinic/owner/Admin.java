package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.model.Person;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "admins")
public class Admin extends Person {

  @Column(name = "first_name")
  @NotEmpty
  private String fistName;

  @Column(name = "")
  
}