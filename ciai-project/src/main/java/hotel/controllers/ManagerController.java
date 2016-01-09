package hotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hotel.exceptions.HotelNotFoundException;
import hotel.exceptions.UserNotFoundException;
import hotel.models.Hotel;
import hotel.models.Manager;
import hotel.repositories.CommentRepository;
import hotel.repositories.HotelRepository;
import hotel.repositories.RoomRepository;
import hotel.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;


/*
 * Mapping
 * GET  	/users/managers							- list all hotel managers
 * DELETE 	/users/managers/{idUser}	 			- delete the hotel manager with identifier {idUser}
 * GET  	/users/managers/{idUser}/hotels			- list all hotels from one hotel manager
 * GET		/users/managers/{idUser}/hotels/{id}	- show the hotel with identifier {id}
 * DELETE 	/users/managers/{idUser}/hotels/{id}	- delete the hotel with identifier {id}
 */

@Controller
@RequestMapping(value="/users/managers")
public class ManagerController {
	
	@Autowired
    UserRepository managers;
	@Autowired
    HotelRepository hotels;
	@Autowired
    RoomRepository rooms;
	@Autowired
    CommentRepository comments;
	
	// GET  /users/managers			- list all hotel managers
   	@RequestMapping(method=RequestMethod.GET)
   	public String listHotelsManagers(Model model) {
   		model.addAttribute("manager", managers.findByStatus(true));
   		return "users/manager";
   	}
   	
   	// DELETE /users/managers/{idUser}	 	- delete the hotel manager with identifier {idUser}
    @RequestMapping(value = "/{idUser}", method = RequestMethod.DELETE)
    public String deleteManager(@PathVariable("idUser") long idUser, Model model) {
    	managers.delete(idUser);
        return "users/manager";
    }
    
    // GET  /users/managers/{idUser}/hotels		- list all hotels from one hotel manager
   	@RequestMapping(value="/{idUser}/hotels", method=RequestMethod.GET) 
    public String showManagers(@PathVariable("idUser") long idUser, Model model) {
    	Manager manager = (Manager) managers.findOne(idUser);
    	if( manager == null ) {
    		throw new UserNotFoundException();
    	}
    	model.addAttribute("hotel", hotels.findAllHotels(idUser));
    	model.addAttribute("manager", manager);
    	return "users/index";
    }
   	
   	//GET	/users/managers/{idUser}/hotels/{id}		-  show the hotel with identifier {id}
  	@RequestMapping(value="/{idUser}/hotels/{id}", method=RequestMethod.GET)
  	public String show(@PathVariable("id") long id, Model model) {
      	Hotel hotel = hotels.findOne(id);
      	if( hotel == null ) {
      		throw new HotelNotFoundException();
      	}
      	Hotel original = hotels.findByHotelEditId(id);
      	model.addAttribute("hotel", hotel);
      	model.addAttribute("original", original);
      	return "users/showHotel";
    }
  	
  	// DELETE /users/managers/{idUser}/hotels/{id}	 	- delete the hotel with identifier {id}
    @RequestMapping(value = "/{idUser}/hotels/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") long id, Model model) {
        hotels.delete(id);
        return "redirect:/users/managers/{idUser}/hotels";
    }
}
