$(function() {
	$('.approveManager').click(
			function() {
				var postURL = "users/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("Hotel Manager has been approved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.approveComment').click(
			function() {
				var postURL = "comments/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("Comment has been approved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.approveHotel').click(
			function() {
				var postURL = "users/hotels/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("Hotel has been approved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.approveEdit').click(
			function() {
				var postURL = "users/approve_edit/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("New version of the hotel has been approved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.approveBooking').click(
			function() {
				var postURL = "/hotels/bookings_wait_list/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("Booking has been approved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.approveEditBooking').click(
			function() {
				var postURL = "/bookings_wait_list/approve_edit/" + $(this).data("url");
				$.ajax({ url: postURL, type:"POST", success: function() {
					$('#notice').html("New version of the booking has been approved successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})