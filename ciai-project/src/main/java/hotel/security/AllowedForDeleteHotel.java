package hotel.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(AllowedForDeleteHotel.Condition)
public @interface AllowedForDeleteHotel {
	
	String Condition = "@mySecurityService.canDeleteHotel(principal,#vars.hotel.id) or " + AllowedForHotelCreation.Condition;

}
