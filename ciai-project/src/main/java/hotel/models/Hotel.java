package hotel.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String name;
    private String address;
    @Enumerated(EnumType.STRING)
    private HotelType hotelType;
    
	private int numStars;
	private boolean approved;
	private boolean edited;
	@ManyToOne
	private Manager manager;
	@OneToOne(optional = true, orphanRemoval = true)
    private Hotel hotelEdited;
	
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="hotel", orphanRemoval = true)
    private Collection<Room> rooms = new ArrayList<Room>();
    
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="hotel", orphanRemoval = true)
    private Collection<Comment> comments = new ArrayList<Comment>();
    
    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="hotel", orphanRemoval=true)
    private List<Booking> bookings = new ArrayList<Booking>();
	
    public Hotel() {}
    
    public Hotel(String name, String address, HotelType hotelType, int numStars, boolean approved, Manager manager){
		this.name = name;
		this.address = address;
		this.hotelType = hotelType;
		this.approved = approved;
		
		this.manager = manager; 
		manager.getHotels().add(this);
	}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setAddress(String address){
		this.address = address;
	}
	
	public String getAddress(){
		return address;
	}
	
    public void setHotelType(HotelType hotelType){
		this.hotelType = hotelType;
	}
	
	public HotelType getHotelType(){
		return hotelType;
	}
	
	public void setNumStars(int numStars){
		this.numStars = numStars;
	}
	
	public int getNumStars(){
		return numStars;
	}
	
	public boolean getApproved() {
    	return approved;
    }
    
    public void setApproved(boolean approved) {
    	this.approved = approved;
    }
    
    public boolean getEdited() {
    	return edited;
    }
    
    public void setEdited(boolean edited) {
    	this.edited = edited;
    }

	@Override
    public String toString() {
    	return "Id: " + getId() + "Name: " + getName() + "\nLocation: " + getAddress() + "\nType: " + getHotelType() + "\nnumStars: " + getNumStars() + "\napproved: " + getApproved()  + "\nedited: " + getEdited();
    }
	
	public Collection<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Collection<Room> rooms) {
		this.rooms = rooms;
	}
	
	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}
	
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
	public Hotel getHotelEdited() {
		return hotelEdited;
	}

	public void setHotelEdited(Hotel hotelEdited) {
		this.hotelEdited = hotelEdited;
	}
	
	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
}