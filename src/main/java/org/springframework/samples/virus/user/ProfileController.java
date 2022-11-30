package org.springframework.samples.virus.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/profile")
public class ProfileController {

  @GetMapping(value="/")
  public String getPlayer() {

      return "redirect:/statistics/{username}";
  }
  
  
}
