package hotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.beans.factory.annotation.Autowired;

import hotel.exceptions.CommentNotFoundException;
import hotel.exceptions.HotelNotFoundException;
import hotel.models.Comment;
import hotel.models.Hotel;
import hotel.repositories.CommentRepository;
import hotel.repositories.HotelRepository;
import hotel.security.AllowedForDeleteComment;

/*
 * Mapping
 * GET  	/comments 					- list comments non-approved by hotel
 * DELETE 	/comments/{id}	 			- disapprove the comment with identifier {id}
 * POST 	/comments/{id}				- approve the comment with identifier {id}
 * GET  	/comments/hotels 			- list comments not approved by hotel
 * GET		/comments/hotels/{id}		- see hotels basic info and all its comments
 */

@Controller
@RequestMapping(value="/comments")
public class CommentController {
	
	@Autowired
    CommentRepository comments;
	@Autowired
	HotelRepository hotels;
	
	//GET  /comments 					- list comments not approved by hotel
	@RequestMapping(method=RequestMethod.GET)
   	public String commentsNotApproved(Model model) {
   		model.addAttribute("comments",comments.findByStatus(false));
   	    return "comments/non_approved";
   	}
	
	// DELETE /comments/{id}	 	- disapprove the comment with identifier {id}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@AllowedForDeleteComment
    public String disapprove(@PathVariable("id") long id) {
    	comments.delete(id);
    	return "comments/non_approved";
    }
    
    //POST /comments/{id}			- approve the comment with identifier {id}
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String approve(@PathVariable("id") long id, Comment comment) {
    	comment = comments.findOne(id);
    	if( comment == null ) {
    		throw new CommentNotFoundException();
    	}
    	comment.setApproved(true);
    	comments.save(comment);
        return "comments/non_approved";
    }
	
	//GET  /comments/hotels 			- list comments not approved by hotel
	@RequestMapping(value="/hotels", method=RequestMethod.GET)
   	public String listHotels(Model model) {
		model.addAttribute("hotel", hotels.findApproved(true));
   	    return "comments/index";
   	}
	
	//GET	/comments/hotels/{id}		- see hotels basic info and all its comments
	@RequestMapping(value="/hotels/{id}", method=RequestMethod.GET)
	public String show(@PathVariable("id") long id, Model model) {
    	Hotel hotel = hotels.findOne(id);
    	if( hotel == null ) {
    		throw new HotelNotFoundException();
    	}
    	model.addAttribute("hotel", hotel);
    	model.addAttribute("rooms", hotel.getRooms());
    	model.addAttribute("comment", comments.findByHotel(id, true));
    	model.addAttribute("commentsNotApproved", comments.findByHotel(id, false));
    	return "comments/show";
    }
}