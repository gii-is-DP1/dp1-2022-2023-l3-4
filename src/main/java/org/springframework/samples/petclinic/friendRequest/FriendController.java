package org.springframework.samples.petclinic.friendRequest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/friend")
public class FriendController {

    private static final String VIEWS_MY_FRIENDS = "friends/FriendsListing";

    private final FriendService friendService;

    @Autowired
    private AuthenticationService authService;

	@Autowired
    private PlayerService playerService;

    @Autowired
	public FriendController(FriendService friendService) {
		this.friendService = friendService;        
	}


    // @GetMapping("/myFriends")
	// public String createSearch(ModelMap model) {
    //     Player playerAuth= authService.getPlayer();
    //     Collection<Player> myFriends=friendRequestService.findFriendById(playerAuth.getId());
    //     model.put("myFriends",myFriends);
	// 	return VIEWS_MY_FRIENDS;
	// }
}
