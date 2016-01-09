package hotel.models;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;

@Entity
public class Manager extends User {

    private boolean approved;
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="manager", orphanRemoval = true)
    private Collection<Hotel> hotels = new ArrayList<Hotel>();
    
    public Manager() {}
    
    public Manager(String username, String email, String name, String password, boolean approved){
		super(username, email, name, password, Roles.ROLE_MANAGER);
		this.approved = approved;
	}
    
    public void setApproved(boolean approved) {
    	this.approved = approved;
    }
    
    public boolean getApproved() {
    	return approved;
    }
   
	@Override
    public String toString() {
    	return "Username: " + getUsername() + "\nEmail: " + getEmail() + "Name: " + getName() + "\nPassword: " + getPassword() + "\nRole: " + "\nApproved:" + getApproved();
    }
	
	public Collection<Hotel> getHotels() {
		return hotels;
	}
	
	public void setHotels(Collection<Hotel> hotels) {
		this.hotels = hotels;
	}
}