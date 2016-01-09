package hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hotel.models.Booking;

public interface BookingRepository extends CrudRepository<Booking, Long> {

	@Query(value="Select * from booking b where b.user_idUser=?1 and b.approved=?2 and b.edited=?3",nativeQuery=true)
	List<Booking> findByUserBooking(long id, boolean approved, boolean edited);
	
	@Query(value="Select * from booking b where b.user_idUser=?",nativeQuery=true)
	List<Booking> findFreeRooms(long id);

	@Query(value="Select * FROM booking u where approved=?2 and edited=?3 and hotel_id in (SELECT id FROM HOTEL u WHERE manager_idUser=?1)",nativeQuery=true)
	List<Booking> findByManagerBookings(long idUser, boolean approved, boolean edited);

	@Query(value="Select * from booking u where booking_edited_id=?",nativeQuery=true)
	Booking findByBookingEditId(long id);
}