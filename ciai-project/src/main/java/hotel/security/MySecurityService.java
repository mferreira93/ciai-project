package hotel.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import hotel.models.Comment;
import hotel.models.Hotel;
import hotel.models.Room;
import hotel.repositories.CommentRepository;
import hotel.repositories.HotelRepository;
import hotel.repositories.RoomRepository;

@Component("mySecurityService")
public class MySecurityService {
	
	@Autowired
	HotelRepository hotels;
	@Autowired
	RoomRepository rooms;
	@Autowired
	CommentRepository comments;
	
    public boolean canEditHotel(User user, long hotelId) {
		Hotel hotel = hotels.findOne(hotelId);
		return hotel != null && hotel.getManager() != null && user.getUsername().equals(hotel.getManager().getName()); 
    }
    
    public boolean canDeleteHotel(User user, long hotelId) {
		Hotel hotel = hotels.findOne(hotelId);
		return hotel != null && hotel.getManager() != null && user.getUsername().equals(hotel.getManager().getName()); 
    }
    
    public boolean canRoomCreate(User user, long hotelId) {
		Hotel hotel = hotels.findOne(hotelId);
		return hotel != null && hotel.getManager() != null && user.getUsername().equals(hotel.getManager().getName()); 
    }
    
    public boolean canDeleteRoom(User user, long hotelId, long roomId) {
		Hotel hotel = hotels.findOne(hotelId);
		Room room = rooms.findOne(roomId);
		return hotel != null && room != null && hotel.getManager() != null && hotel.getRooms().contains(room) && user.getUsername().equals(hotel.getManager().getName()); 
    }
    
    public boolean canAnswerCreate(User user, long commentId) { 
    	Comment comment = comments.findOne(commentId);
    	Hotel hotel = comment.getHotel();
    	return comment != null && hotel != null && hotel.getManager() != null && user.getUsername().equals(hotel.getManager().getName());
    }

    public boolean canCommentCreate(User user, long hotelId) { 
    	Hotel hotel = hotels.findOne(hotelId);
    	return hotel != null && user != null;
    }
    
    public boolean canDeleteRoom(User user, long commentId) { 
    	Comment comment = comments.findOne(commentId);
    	return comment != null && user != null;
    }
}

