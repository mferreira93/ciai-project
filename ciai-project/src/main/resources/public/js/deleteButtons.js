$(function() {
	$('.deleteGuest').click(
			function() {
				var deleteURL = "guests/" + $(this).data("url");
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Guest deleted successfuly.");
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.deleteModerator').click(
			function() {
				var deleteURL = "moderators/" + $(this).data("url");
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Comment Moderator deleted successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.deleteManager').click(
			function() {
				var deleteURL = "managers/" + $(this).data("url");
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Hotel Manager deleted successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.deleteBooking').click(
			function() {
				var deleteURL = "/hotels/bookings/" + $(this).data("url") + "/delete";
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Reservation canceled successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.cancelBooking').click(
			function() {
				var deleteURL = "/guests/" + $(this).data("url");
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Reservation canceled successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})

$(function() {
	$('.deleteRoom').click(
			function() {
				var deleteURL = "/hotels/" + $(this).data("id") + "/room/" + $(this).data("url");
				$.ajax({ url: deleteURL, type:"DELETE", success: function() {
					$('#notice').html("Rooms deleted successfuly.")
					window.setTimeout('location.reload()', 250);}});
			});	
})