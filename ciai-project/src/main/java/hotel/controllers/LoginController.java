package hotel.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hotel.models.Manager;
import hotel.models.Roles;
import hotel.models.User;
import hotel.repositories.UserRepository;

/*
 * Mapping
 * GET  				/login				- login page
 * HttpServletRequest  	/default			- redirect to page depending of the user role
 * GET					/sign_up_guest		- the form to fill the data for a new guest
 * POST 				/sign_up_guest		- creates a new guest
 * GET  				/sign_up_manager	- the form to fill the data for a new hotel manager
 * POST 				/sign_up_manager	- creates a new hotel manager
 */

@Controller
public class LoginController {
	
	@Autowired
    UserRepository users;
	
	// GET  /login				- login page
	@RequestMapping(value="/login", method=RequestMethod.GET)
    public String loginPage(ModelMap map) {
        return "homepage/login";
    }
	
	// HttpServletRequest  /default				- redirect to page depending of the user role
    @RequestMapping(value="/default")
    public String defaultAfterLogin(HttpServletRequest request) {
    	if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/users";
        } 
    	if (request.isUserInRole("ROLE_MODERATOR")) {
            return "redirect:/comments";
        } 
    	if (request.isUserInRole("ROLE_MANAGER")) {
            return "redirect:/hotels";
        } 
    	if (request.isUserInRole("ROLE_GUEST")) {
            return "redirect:/guests";
        } 
        return "error.html";
    }
    
    // GET  /sign_up_guest			- the form to fill the data for a new guest
    @RequestMapping(value="/sign_up_guest", method=RequestMethod.GET)
    public String signUpGuest(Model model) {
    	model.addAttribute("user", new User());
    	return "homepage/create_guest";
    }
    
    // POST /sign_up_guest			- creates a new guest
    @RequestMapping(value="/sign_up_guest", method=RequestMethod.POST)
    public String saveGuest(@ModelAttribute User user, Model model) {
    	user.setRole(Roles.ROLE_GUEST);
    	users.save(user);
    	return "redirect:/";
    }
    
    // GET  /sign_up_manager			- the form to fill the data for a new hotel manager
    @RequestMapping(value="/sign_up_manager", method=RequestMethod.GET)
    public String signUpManager(Model model) {
    	model.addAttribute("user", new User());
    	return "homepage/create_manager";
    }
    
    // POST /sign_up_manager				- creates a new hotel manager
    @RequestMapping(value="/sign_up_manager", method=RequestMethod.POST)
    public String saveManager(@ModelAttribute Manager manager, Model model) {
    	manager.setRole(Roles.ROLE_MANAGER);
    	manager.setApproved(false);
    	users.save(manager);
    	return "redirect:/";
    }
}
