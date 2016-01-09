package hotel.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(AllowedForEditHotel.Condition)
public @interface AllowedForEditHotel {
	
	// Should be only one expression.
	// The binding of the variables in the expression should not be dynamic
	// It should be represented by parameters.
    String Condition = "@mySecurityService.canEditHotel(principal,#id) or " + AllowedForHotelCreation.Condition;
    String Condition2 = "@mySecurityService.canEditHotel(principal,#vars.hotel.id) or " + AllowedForHotelCreation.Condition;    
}