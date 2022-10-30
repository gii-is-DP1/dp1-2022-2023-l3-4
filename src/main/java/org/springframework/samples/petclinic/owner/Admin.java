package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.model.Person;
import javax.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends Person {

}