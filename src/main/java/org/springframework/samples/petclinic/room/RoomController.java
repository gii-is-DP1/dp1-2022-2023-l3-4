package org.springframework.samples.petclinic.room;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.room.exceptions.DuplicatedNameRoomException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room")
public class RoomController {

    private static final String VIEWS_ROOM_CREATE_OR_UPDATE_FORM = "createOrUpdateRoomForm";

    
	private final RoomService roomService;

    @Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;        
	}

	@GetMapping(value = "/new")
	public String initCreationForm(Owner owner, ModelMap model) {
		Room room = new Room();
		owner.addRoom(room);
		model.put("room", room);
		return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(Owner owner, @Valid Room room, BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			model.put("room", room);
			return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
		}
		else {
                    try{
                    	owner.addRoom(room);;
                    	this.roomService.saveRoom(room);;
                    }catch(DuplicatedNameRoomException ex){
                        result.rejectValue("name", "duplicate", "already exists");
                        return VIEWS_ROOM_CREATE_OR_UPDATE_FORM;
                    }
                    return "redirect:/welcome";
		}
	}
}
