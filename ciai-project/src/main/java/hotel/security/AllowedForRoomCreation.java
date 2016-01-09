package hotel.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(AllowedForRoomCreation.Condition)
public @interface AllowedForRoomCreation {
	
	String Condition = "@mySecurityService.canRoomCreate(principal,#vars.hotel.id) or " + AllowedForHotelCreation.Condition;

}
