$(function() {
	$('.disapproveManager').click(
			function() {
				var deleteURL = "users/" + $(this).data("url");
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Hotel Manager has been disapproved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.disapproveComment').click(
			function() {
				var deleteURL = "comments/" + $(this).data("url");
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Comment has been disapproved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.disapproveHotel').click(
			function() {
				var deleteURL = "users/hotels/" + $(this).data("url");
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Hotel has been disapproved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.disapproveEdit').click(
			function() {
				var postURL = "users/decline_update/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("New version of the hotel has been disapproved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.disapproveBooking').click(
			function() {
				var postURL = "/hotels/bookings_wait_list/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("Booking has been disapproved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.disapproveEditBooking').click(
			function() {
				var postURL = "/hotels/bookings_wait_list/decline_update/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("New version of the booking has been disapproved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})