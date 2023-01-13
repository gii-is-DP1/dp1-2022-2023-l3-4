package org.springframework.samples.petclinic.user;



import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class UserServiceTests {
    @Autowired
    protected UserService userService;

    @Test
    public void shouldFindUser(){
        User userPrueba= userService.findUser("frabenrui1");
        assertTrue(userPrueba.getUsername().equals("frabenrui1") , "There should be a user with name = p.");
        
    }

    @Test
    public void shouldSaveUser(){
        User userPrueba= new User();
        userPrueba.setUsername("prueba");
        userService.saveUser(userPrueba);
        User comprobation= userService.findUser("prueba");
        assertTrue(comprobation.getUsername() == "prueba", "The user was not saved.");
        
    }

    @Test
    public void shouldUpdateUser(){
        User userPrueba= userService.findUser("p");
        userPrueba.setPassword("pswd");
        userService.saveUser(userPrueba);
        User comprobation= userService.findUser("p");
        assertTrue(comprobation.getPassword() == "pswd", "The user was not updated.");
        
    }

    @Test
    public void shouldDeleteUser(){
        userService.deleteUser("p");
        assertThrows(NoSuchElementException.class, () -> userService.findUser("p"));
        
    }

}
