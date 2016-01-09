package hotel.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import hotel.models.Hotel;
import hotel.models.Room;

@RepositoryRestResource(collectionResourceRel="hotelsautorest", path="hotelsautorest")
public interface RestHotelRepository extends CrudRepository<Hotel, Long> {
	
	@Query(value="SELECT * FROM HOTEL u WHERE APPROVED=TRUE AND EDITED=FALSE AND manager_idUser=?",nativeQuery=true)
	List<Hotel> findAllHotels(long idUser);
	
	@Query(value="SELECT * FROM HOTEL u WHERE APPROVED=? AND EDITED=FALSE",nativeQuery=true)
	List<Hotel> findApproved(boolean approved);
	
	@Query(value="SELECT * FROM HOTEL u WHERE APPROVED=FALSE AND EDITED=?",nativeQuery=true)
	List<Hotel> findEdited(boolean edited);
	
	@Query(value="SELECT * FROM HOTEL h WHERE APPROVED=TRUE AND EDITED=FALSE and hotel_type=?",nativeQuery=true)
	List<Hotel> findByType(String type);

	@Query(value="Select * from hotel u where hotel_edited_id=?",nativeQuery=true)
	Hotel findByHotelEditId(long id);
	
	@Query(value="SELECT * FROM HOTEL WHERE id NOT IN (SELECT h.id FROM HOTEL h INNER JOIN BOOKING b INNER JOIN ROOM r WHERE h.id = b.hotel_id AND r.id_room = b.room_idRoom AND "
   + "(((?1 BETWEEN b.start AND b.end) or (?2 BETWEEN b.start AND b.end)) or (b.start BETWEEN ?1 AND ?2)))", nativeQuery=true)
	List<Hotel>  findBookingAvailable(Date begin, Date end);
	
	Hotel findByName(String name);
	
	//@Query(value="SELECT * FROM ROOM WHERE id NOT IN (SELECT h.id FROM HOTEL h INNER JOIN BOOKING b INNER JOIN ROOM r WHERE h.id = b.hotel_id AND r.id_room = b.room_idRoom AND "
	//		+ "(((?1 BETWEEN b.start AND b.end) or (?2 BETWEEN b.start AND b.end)) or (b.start BETWEEN ?1 AND ?2)))", nativeQuery=true)
	//List<Room> findAllFreeRooms(String hotel, String initial, String end);
}