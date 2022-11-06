package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.samples.petclinic.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class WelcomeController {
	
	
	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {	  
			
			List<Person> persons = new ArrayList<Person>();
			Person person1 = new Person();
			Person person2 = new Person();
			Person person3 = new Person();
			Person person4 = new Person();
			Person person5 = new Person();
			Person person6 = new Person();
			
			person1.setFirstName("José María");
			person1.setLastName(" García Berdejo");
			persons.add(person1);
			person2.setFirstName("María");
			person2.setLastName(" Marquez Sierra");
			persons.add(person2);
			person3.setFirstName("Lucia");
			person3.setLastName(" Perez Romero");
			persons.add(person3);
			person4.setFirstName("Francisco Sebastián");
			person4.setLastName(" Benítez Ruis Díaz");
			persons.add(person4);
			person5.setFirstName("Joaquín");
			person5.setLastName(" Restoy Barrero");
			persons.add(person5);
			person6.setFirstName("Juan Antonio");
			person6.setLastName(" Jiménez del Villar");
			persons.add(person6);
			
			model.put("persons", persons);
			model.put("title", "Virus!");
			model.put("group", "L3-4");

	    return "welcome";
	  }
}
