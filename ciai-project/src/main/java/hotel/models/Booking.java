package hotel.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Future;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Booking {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@ManyToOne
	private Hotel hotel;
	
	@ManyToOne
	private Room room;
	
	@ManyToOne
	private User user;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start;
	
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end;
	
	@OneToOne(optional = true, orphanRemoval = true)
	private Booking bookingEdited;
	
	private boolean approved; 
	private boolean edited;
	
	
	public Booking(Hotel hotel, Room room, User user, Date start, Date end, boolean approved) {
		this.hotel = hotel;
		hotel.getBookings().add(this);
		this.room = room;
     	room.getBookings().add(this);
		this.user = user;
    	user.getBookings().add(this);
		this.start = start;
		this.end = end;
		this.approved = approved;
	}

	public Booking(){};

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Booking getBookingEdited() {
		return bookingEdited;
	}

	public void setBookingEdited(Booking bookingEdited) {
		this.bookingEdited = bookingEdited;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setApproved(boolean approved) {
    	this.approved = approved;
    }
    
    public boolean getApproved() {
    	return approved;
    }
    
    public boolean getEdited() {
    	return edited;
    }
    
    public void setEdited(boolean edited) {
    	this.edited = edited;
    }
	
	@Override
	public String toString()
	{
		return "id: " + id + " startDate: " + start + " endDate:" + end + " hotel:" + hotel + " room:" + room + " guest: " + user;
	}
}
