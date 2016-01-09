package hotel.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(AllowedForDeleteRoom.Condition)
public @interface AllowedForDeleteRoom {
	
	String Condition = "@mySecurityService.canDeleteRoom(principal,#vars.hotel.id, #vars.room.idRoom) or " + AllowedForHotelCreation.Condition;

}
