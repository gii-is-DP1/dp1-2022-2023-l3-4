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
			Person person = new Person();
			person.setFirstName("José María");
			person.setLastName("García Berdejo");
			persons.add(person);
			model.put("persons", persons);
			model.put("title", "My Project");
			model.put("group", "L3-4");

	    return "welcome";
	  }
}
