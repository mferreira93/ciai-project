package hotel.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Comment {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	@OneToMany(cascade=CascadeType.PERSIST, orphanRemoval=true)
	private List<Comment> answers = new ArrayList<Comment>();
	@ManyToOne
	private User user;
	@ManyToOne
	private Hotel hotel;
	private String description;
	private boolean approved; 
	
	public Comment(){}
	
	public Comment(String comment, User user, Hotel hotel, boolean approved) {
		this.description = comment;
		
		this.user = user; 
		user.getComments().add(this);
		
		this.hotel = hotel;
		hotel.getComments().add(this);
		
		answers.add(this);
		
		this.approved = approved;
	}
	
	public long getId() {
		 return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getDescription() {
    	return description;
    }
	
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	
	public void setApproved(boolean approved) {
    	this.approved = approved;
    }
    
    public boolean getApproved() {
    	return approved;
    }
    
    public List<Comment> getAnswers() {
    	return answers;
    }
    
    public void setAnswers(List<Comment> answers) {
    	this.answers = answers;
    }
	
	@Override
    public String toString() {
    	return "Id: " + getId() + " Comment: " + getDescription() + "\nApproved:" + getApproved();
    }
}
