package hotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;

import hotel.exceptions.UserNotFoundException;
import hotel.models.Roles;
import hotel.models.User;
import hotel.repositories.UserRepository;

/*
 * Mapping
 * GET  	/users/moderators 				- list all comment moderators
 * GET  	/users/moderators/new			- the form to fill the data for a new comment moderator
 * POST 	/users/moderators				- creates a new comment moderator
 * GET  	/users/moderators/{idUser}/edit	- the form to edit the comment moderator with identifier {idUser}
 * POST 	/users/moderators/{idUser} 	 	- update the comment moderator with identifier {idUser}
 * GET  	/users/moderators/{idUser}		- show the comment moderator with identifier {idUser}
 * DELETE 	/users/moderators/{idUser} 	 	- delete the comment moderator with identifier {idUser}
 */

@Controller
@RequestMapping(value="/users/moderators")
public class ModeratorController {
	
	@Autowired
    UserRepository users;
 	
   	// GET  /users/moderators 			- list all comment moderators
   	@RequestMapping(method=RequestMethod.GET)
   	public String listCommentModerators(Model model) {
   		model.addAttribute("user", users.findByRoles(Roles.ROLE_MODERATOR.toString()));
   	    return "users/moderator";
   	}
   	
    // GET  /users/moderators/new			- the form to fill the data for a new comment moderator
    @RequestMapping(value="/new", method=RequestMethod.GET)
    public String newCommentModerator(Model model) {
    	model.addAttribute("user", new User());
    	return "users/create";
    }
    
    // POST /users/moderators				- creates a new comment moderator
    @RequestMapping(method=RequestMethod.POST)
    public String saveIt(@ModelAttribute User user, Model model) {
    	user.setRole(Roles.ROLE_MODERATOR);
    	users.save(user);
    	return "redirect:/users/moderators";
    }	
    
    // GET  /users/moderators/{idUser}/edit	- the form to edit the comment moderator with identifier {idUser}
    @RequestMapping(value="{idUser}/edit", method=RequestMethod.GET)
    public String edit(@PathVariable("idUser") long idUser, Model model) {
    	model.addAttribute("user", users.findOne(idUser));
    	return "users/edit";
    }
   
    // POST /users/moderators/{idUser} 	 		- update the comment moderator with identifier {idUser}
    @RequestMapping(value="{idUser}", method=RequestMethod.POST)
    public String editSave(@PathVariable("idUser") long idUser, User user, Model model) {
    	user.setRole(Roles.ROLE_MODERATOR);
    	users.save(user);
    	return "redirect:/users/moderators/{idUser}";
    }
    
    // GET  /users/moderators/{idUser}		- show the comment moderator with identifier {idUser}
    @RequestMapping(value="/{idUser}", method=RequestMethod.GET) 
    public String show(@PathVariable("idUser") long idUser, Model model) {
    	User user = users.findOne(idUser);
    	if( user == null )
    		throw new UserNotFoundException();
    	model.addAttribute("user", user );
    	return "users/show_moderator";
    }
   
    // DELETE /users/moderators/{idUser} 	 	- delete the comment moderator with identifier {idUser}
    @RequestMapping(value = "{idUser}", method = RequestMethod.DELETE)
    public String deleteModerator(@PathVariable("idUser") long idUser) {
        users.delete(idUser);
        return "users/moderator";
    }
}