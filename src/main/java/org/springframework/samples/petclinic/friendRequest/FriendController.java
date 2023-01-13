package org.springframework.samples.petclinic.friendRequest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.invitation.Invitation;
import org.springframework.samples.petclinic.invitation.InvitationService;
import org.springframework.samples.petclinic.player.Player;
import org.springframework.samples.petclinic.player.PlayerService;
import org.springframework.samples.petclinic.room.Room;
import org.springframework.samples.petclinic.util.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/friend")
public class FriendController {

    private static final String VIEWS_MY_FRIENDS = "friends/FriendsListing";
    private static final String VIEWS_MY_REQUEST = "friends/FriendsRequestListing";
    private static final String VIEWS_MY_INVITATIONS = "invitations/FriendsInvitationListing";

    private final FriendService friendService;

    @Autowired
    private AuthenticationService authService;

	@Autowired
    private PlayerService playerService;

    @Autowired

    private InvitationService invitationService;

    @Autowired
	public FriendController(FriendService friendService,AuthenticationService authService,PlayerService playerService) {
		this.friendService = friendService;  
        this.authService=authService;
        this.playerService=playerService;     
	}


    @GetMapping("/myFriends")
	public String friendsListing(ModelMap model) {
        Player playerAuth= authService.getPlayer();
        Collection<Player> myFriends=friendService.findFriendsById(playerAuth.getId());
        model.put("myFriends",myFriends);
        model.put("playerAuth",playerAuth);
		return VIEWS_MY_FRIENDS;
	}

    @GetMapping("/myFriendsRequest")
	public String friendsRequestListing(ModelMap model) {
        Player playerAuth= authService.getPlayer();
        Collection<Friend> myRequest=friendService.findMyRecRequestById(playerAuth.getId());
        Collection<Friend> mySendRequest=friendService.findSendRequestById(playerAuth.getId());
        model.put("myRequest",myRequest);
        model.put("mySendRequest",mySendRequest);
		return VIEWS_MY_REQUEST;
	}

    @GetMapping("/myInvitationRequests")
	public String friendsInvitationListing(ModelMap model) {
        Player playerAuth= authService.getPlayer();
        Collection<Invitation> myInvitation=invitationService.findMyRecInvitationById(playerAuth.getId());
        model.put("myInvitation",myInvitation);
		return VIEWS_MY_INVITATIONS;
	}

    @GetMapping("/myFriendsRequest/{requestId}/accept")
	public String friendRequestAccept(@PathVariable("requestId") int requestId) {
        Friend friend=friendService.findFriendById(requestId);
        friend.setStatus(true);
        this.friendService.savePlayer(friend);
		return "redirect:/friend/myFriendsRequest";
	}
    
    @GetMapping("/myFriendsRequest/{requestId}/denied")
	public String friendRequestDenied(@PathVariable("requestId") int requestId) {
        Friend friend=friendService.findFriendById(requestId);
        friend.setStatus(false);
        this.friendService.savePlayer(friend);
        this.friendService.savePlayer(friend);
		return "redirect:/friend/myFriendsRequest";
	}

    @GetMapping("/myInvitationRequest/{invitationId}/denied")
	public String friendInvitationDenied(@PathVariable("invitationId") int invitationId) {
        Invitation invitation = invitationService.findInvitationById(invitationId);
        invitation.setIsViewer(null);
        invitation.setPlayerInvitationRec(null);
        invitation.setPlayerInvitationSend(null);
        invitation.setRoom(null);
        this.invitationService.saveInvitation(invitation);
        this.invitationService.deleteInvitationById(invitationId);

		return "redirect:/friend/myInvitationRequests";
	}

    @GetMapping("/{player1Id}/delete/{player2Id}")
	public String friendDelete(@PathVariable("player1Id") int player1Id,@PathVariable("player2Id") int player2Id) {
        Friend friend= friendService.findByPlayersId(player1Id, player2Id);
        friend.setPlayerRec(null);
        friend.setPlayerSend(null);
        this.friendService.savePlayer(friend);
        friendService.deleteFriendById(friend.getId());
		return "redirect:/friend/myFriends";
	}

    @GetMapping("/request/{playerId}")
	public String friendRequest(@PathVariable("playerId") int playerId) {
        Player playerSend = authService.getPlayer();
        Player playerRec=playerService.findPlayerById(playerId);
        Friend repeat=friendService.findByPlayersId(playerRec.getId(), playerSend.getId());
        if(repeat==null){
            Friend request = new Friend();
            request.setStatus(null);
            request.setPlayerSend(playerSend);
            request.setPlayerRec(playerRec);
            this.friendService.savePlayer(request);
            
        }
		return "redirect:/friend/myFriends";
	}

    @GetMapping("/invitation/{playerId}")
	public String invitationRequest(@PathVariable("playerId") int playerId, ModelMap model) {
        Player playerInvitationSend = authService.getPlayer();
        Player playerInvitationRec = playerService.findPlayerById(playerId);
        Room room = playerInvitationSend.getRoom();
        Invitation repeat = invitationService.findInvitationByPlayersAndRoomId(playerInvitationRec.getId(), playerInvitationSend.getId(), room.getId());
        if(repeat == null) {
            if(playerInvitationSend.getRoom() != null) {
                Invitation invitation = new Invitation();
                invitation.setIsViewer(false);
                invitation.setPlayerInvitationSend(playerInvitationSend);
                invitation.setPlayerInvitationRec(playerInvitationRec);
                invitation.setRoom(playerInvitationSend.getRoom());
                this.invitationService.saveInvitation(invitation);
           } else {
            model.put("message", "You should be in a room");
            model.put("messageType", "warning");
           }
        } else {
            model.put("message", "You already sent a invitation to this friend");
            model.put("messageType", "warning");
        }
		return "redirect:/room/myRoom";
	}

}
