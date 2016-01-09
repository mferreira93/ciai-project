package hotel.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long idRoom;
    private double price;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    @ManyToOne
    private Hotel hotel;
    
    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="room", orphanRemoval=true)
    private List<Booking> bookings = new ArrayList<Booking>();

    public Room() {}
    
    public Room(RoomType roomType, Hotel hotel, double price) {
    	this.roomType = roomType;
    	
    	this.hotel = hotel;
    	hotel.getRooms().add(this);

    	this.price = price;
    }

    public long getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(long idRoom) {
        this.idRoom = idRoom;
    }

    public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
    
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public Hotel getHotel() {
		return hotel;
	}
	
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	
	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
	
	@Override
    public String toString() {
    	return "Id: " + getIdRoom() + "Room Type: " + getRoomType() + "Price: " + getPrice() ;
    }
}

